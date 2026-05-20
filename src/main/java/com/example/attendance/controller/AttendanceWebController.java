package com.example.attendance.controller;

import com.example.attendance.entity.Attendance;
import com.example.attendance.entity.Course;
import com.example.attendance.entity.User;
import com.example.attendance.repository.AttendanceRepository;
import com.example.attendance.repository.CourseRepository;
import com.example.attendance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/attendance")
public class AttendanceWebController {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserService userService;

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userService.getUserByUsername(username);
    }

    @GetMapping("/checkIn")
    public String checkInPage(Model model) {
        User currentUser = getCurrentUser();
        if (currentUser == null || !"student".equals(currentUser.getRole())) {
            return "redirect:/dashboard";
        }
        List<Course> courses = courseRepository.findByStatus(1);
        model.addAttribute("courses", courses);
        return "attendance-checkin";
    }

    @PostMapping("/checkIn")
    public String checkIn(@RequestParam Long courseId,
                          @RequestParam(required = false) Integer seatRow,
                          @RequestParam(required = false) Integer seatCol,
                          @RequestParam(required = false) String remark,
                          Model model) {

        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return "redirect:/login";
        }

        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null) {
            model.addAttribute("errorMsg", "课程不存在");
            return checkInPage(model);
        }

        String studentId = currentUser.getStudentId();
        if (studentId == null) {
            model.addAttribute("errorMsg", "用户学号未设置，请联系管理员");
            return checkInPage(model);
        }

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        List<Attendance> existingRecords = attendanceRepository.findByStudentIdAndCourseIdAndCheckInTimeBetween(
                studentId, courseId, startOfDay, endOfDay);

        if (!existingRecords.isEmpty()) {
            model.addAttribute("errorMsg", "今天已经打卡过了！");
            return checkInPage(model);
        }

        Attendance attendance = new Attendance();
        attendance.setStudentId(studentId);
        attendance.setStudentName(currentUser.getRealName());
        attendance.setCourseId(courseId);
        attendance.setCourseName(course.getCourseName());
        attendance.setCheckInTime(LocalDateTime.now());
        attendance.setAttendanceDate(LocalDate.now());
        attendance.setSeatRow(seatRow);
        attendance.setSeatCol(seatCol);
        attendance.setRemark(remark);

        LocalTime now = LocalTime.now();
        LocalTime classStartTime = course.getClassStartTime();

        if (classStartTime == null) {
            attendance.setStatus("NORMAL");
        } else if (now.isAfter(classStartTime.plusMinutes(15))) {
            attendance.setStatus("LATE");
        } else {
            attendance.setStatus("NORMAL");
        }

        attendance.setCreateTime(LocalDateTime.now());

        attendanceRepository.save(attendance);

        String statusText = "LATE".equals(attendance.getStatus()) ? "迟到" : "正常";
        model.addAttribute("successMsg", "打卡成功！状态：" + statusText);

        return checkInPage(model);
    }

    @GetMapping("/list")
    public String list(@RequestParam(required = false) String startDate,
                       @RequestParam(required = false) String endDate,
                       @RequestParam(required = false) Long courseId,
                       @RequestParam(required = false) String status,
                       @RequestParam(required = false) String today,
                       @RequestParam(required = false) String week,
                       @RequestParam(required = false) String month,
                       @RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "10") int size,
                       Model model) {

        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return "redirect:/login";
        }

        String studentId = currentUser.getStudentId();

        String finalStartDate = startDate;
        String finalEndDate = endDate;
        if (today != null) {
            finalStartDate = LocalDate.now().toString();
            finalEndDate = LocalDate.now().toString();
        } else if (week != null) {
            LocalDate now = LocalDate.now();
            finalStartDate = now.minusDays(now.getDayOfWeek().getValue() - 1).toString();
            finalEndDate = now.plusDays(7 - now.getDayOfWeek().getValue()).toString();
        } else if (month != null) {
            finalStartDate = LocalDate.now().withDayOfMonth(1).toString();
            finalEndDate = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).toString();
        }

        final String finalStudentId = studentId;
        final Long finalCourseId = courseId;
        final String finalStatus = status;
        final String fsd = finalStartDate;
        final String fed = finalEndDate;

        Specification<Attendance> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("studentId"), finalStudentId));

            if (fsd != null && !fsd.isEmpty()) {
                LocalDateTime start = LocalDate.parse(fsd).atStartOfDay();
                predicates.add(cb.greaterThanOrEqualTo(root.get("checkInTime"), start));
            }
            if (fed != null && !fed.isEmpty()) {
                LocalDateTime end = LocalDate.parse(fed).atStartOfDay().plusDays(1);
                predicates.add(cb.lessThan(root.get("checkInTime"), end));
            }
            if (finalCourseId != null && finalCourseId > 0) {
                predicates.add(cb.equal(root.get("courseId"), finalCourseId));
            }
            if (finalStatus != null && !finalStatus.isEmpty()) {
                predicates.add(cb.equal(root.get("status"), finalStatus));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "checkInTime"));
        Page<Attendance> attendancePage = attendanceRepository.findAll(spec, pageable);

        List<Course> courses = courseRepository.findAll();

        model.addAttribute("records", attendancePage.getContent());
        model.addAttribute("courses", courses);
        model.addAttribute("startDate", finalStartDate);
        model.addAttribute("endDate", finalEndDate);
        model.addAttribute("courseId", courseId);
        model.addAttribute("status", status);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", attendancePage.getTotalPages());
        model.addAttribute("totalElements", attendancePage.getTotalElements());
        model.addAttribute("pageSize", size);

        return "attendance-list";
    }
}