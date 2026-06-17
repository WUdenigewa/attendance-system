package com.example.attendance.controller;

import com.example.attendance.entity.Attendance;
import com.example.attendance.entity.Course;
import com.example.attendance.entity.User;
import com.example.attendance.repository.AttendanceRepository;
import com.example.attendance.repository.CourseRepository;
import com.example.attendance.service.UserService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.persistence.criteria.Predicate;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

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

    /**
     * 教师考勤管理页面
     */
    @GetMapping("/attendance")
    public String teacherAttendance(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String studentId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            Model model) {

        User currentUser = getCurrentUser();
        if (currentUser == null || (!"teacher".equals(currentUser.getRole()) && !"admin".equals(currentUser.getRole()))) {
            return "redirect:/login";
        }

        // 构建查询条件
        Specification<Attendance> spec = buildSpecification(startDate, endDate, courseId, status, studentId);

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "attendanceDate"));
        Page<Attendance> attendancePage = attendanceRepository.findAll(spec, pageable);

        // 统计
        List<Attendance> allRecords = attendanceRepository.findAll(spec);
        long totalCount = allRecords.size();
        long normalCount = allRecords.stream().filter(a -> "NORMAL".equals(a.getStatus())).count();
        long lateCount = allRecords.stream().filter(a -> "LATE".equals(a.getStatus())).count();
        long leaveCount = allRecords.stream().filter(a -> "LEAVE".equals(a.getStatus())).count();

        List<Course> courses = courseRepository.findAll();

        model.addAttribute("records", attendancePage.getContent());
        model.addAttribute("courses", courses);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("courseId", courseId);
        model.addAttribute("status", status);
        model.addAttribute("studentId", studentId);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", attendancePage.getTotalPages());
        model.addAttribute("totalElements", attendancePage.getTotalElements());
        model.addAttribute("pageSize", size);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("normalCount", normalCount);
        model.addAttribute("lateCount", lateCount);
        model.addAttribute("leaveCount", leaveCount);

        return "teacher-attendance";
    }

    /**
     * 导出考勤记录为 Excel
     */
    @GetMapping("/attendance/export")
    public void exportAttendance(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String studentId,
            HttpServletResponse response) throws IOException {

        // 构建查询条件
        Specification<Attendance> spec = buildSpecification(startDate, endDate, courseId, status, studentId);
        List<Attendance> records = attendanceRepository.findAll(spec, Sort.by(Sort.Direction.DESC, "attendanceDate"));

        // 创建 Excel 工作簿
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("考勤记录");

        // 创建标题行
        Row headerRow = sheet.createRow(0);
        String[] headers = {"学号", "姓名", "课程", "打卡日期", "打卡时间", "状态", "座位", "备注"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(getHeaderStyle(workbook));
        }

        // 填充数据
        int rowIndex = 1;
        for (Attendance record : records) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(record.getStudentId() != null ? record.getStudentId() : "");
            row.createCell(1).setCellValue(record.getStudentName() != null ? record.getStudentName() : "");
            row.createCell(2).setCellValue(record.getCourseName() != null ? record.getCourseName() : "");
            row.createCell(3).setCellValue(record.getAttendanceDate() != null ? record.getAttendanceDate().toString() : "");
            row.createCell(4).setCellValue(record.getCheckInTime() != null ? record.getCheckInTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")) : "");

            String statusText = "";
            if ("NORMAL".equals(record.getStatus())) statusText = "正常";
            else if ("LATE".equals(record.getStatus())) statusText = "迟到";
            else if ("LEAVE".equals(record.getStatus())) statusText = "请假";
            else if ("ABSENT".equals(record.getStatus())) statusText = "缺勤";
            row.createCell(5).setCellValue(statusText);

            String seat = "";
            if (record.getSeatRow() != null && record.getSeatCol() != null) {
                seat = record.getSeatRow() + "排" + record.getSeatCol() + "座";
            }
            row.createCell(6).setCellValue(seat);
            row.createCell(7).setCellValue(record.getRemark() != null ? record.getRemark() : "");
        }

        // 自动调整列宽
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String filename = URLEncoder.encode("考勤记录_" + LocalDate.now().toString(), "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + filename + ".xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }

    /**
     * 构建查询条件
     */
    private Specification<Attendance> buildSpecification(String startDate, String endDate, Long courseId, String status, String studentId) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (startDate != null && !startDate.isEmpty()) {
                LocalDateTime start = LocalDate.parse(startDate).atStartOfDay();
                predicates.add(cb.greaterThanOrEqualTo(root.get("attendanceDate"), start.toLocalDate()));
            }
            if (endDate != null && !endDate.isEmpty()) {
                LocalDateTime end = LocalDate.parse(endDate).atStartOfDay().plusDays(1);
                predicates.add(cb.lessThan(root.get("attendanceDate"), end.toLocalDate()));
            }
            if (courseId != null && courseId > 0) {
                predicates.add(cb.equal(root.get("courseId"), courseId));
            }
            if (status != null && !status.isEmpty()) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            if (studentId != null && !studentId.isEmpty()) {
                predicates.add(cb.equal(root.get("studentId"), studentId));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * 标题样式
     */
    private CellStyle getHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }
}