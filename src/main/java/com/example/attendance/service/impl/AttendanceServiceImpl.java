package com.example.attendance.service.impl;

import com.example.attendance.entity.Attendance;
import com.example.attendance.repository.AttendanceRepository;
import com.example.attendance.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Override
    public boolean addAttendance(Attendance attendance) {
        if (attendance.getStudentId() == null || attendance.getStudentId().trim().isEmpty()) {
            System.out.println("添加失败：学号不能为空");
            return false;
        }
        if (attendance.getCourseId() == null) {
            System.out.println("添加失败：课程ID不能为空");
            return false;
        }
        if (attendance.getStatus() == null || attendance.getStatus().trim().isEmpty()) {
            System.out.println("添加失败：考勤状态不能为空");
            return false;
        }

        if (attendance.getAttendanceDate() == null) {
            attendance.setAttendanceDate(LocalDate.now());
        }

        attendanceRepository.save(attendance);
        System.out.println("考勤记录添加成功：" + attendance.getStudentName());
        return true;
    }

    @Override
    public Attendance getAttendanceById(Long id) {
        Optional<Attendance> attendance = attendanceRepository.findById(id);
        return attendance.orElse(null);
    }

    @Override
    public List<Attendance> getAttendanceByStudentId(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            return List.of();
        }
        return attendanceRepository.findByStudentId(studentId);
    }

    @Override
    public List<Attendance> getAttendanceByCourseId(Long courseId) {
        if (courseId == null) {
            return List.of();
        }
        return attendanceRepository.findByCourseId(courseId);
    }

    @Override
    public List<Attendance> getAttendanceByDate(LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        return attendanceRepository.findByAttendanceDate(date);
    }

    @Override
    public List<Attendance> getStudentCourseAttendance(String studentId, Long courseId) {
        if (studentId == null || courseId == null) {
            return List.of();
        }
        return attendanceRepository.findByStudentIdAndCourseId(studentId, courseId);
    }

    @Override
    public int countAttendanceByCourseAndDate(Long courseId, LocalDate date) {
        if (courseId == null) {
            return 0;
        }
        if (date == null) {
            date = LocalDate.now();
        }
        return attendanceRepository.countByCourseIdAndAttendanceDate(courseId, date);
    }

    @Override
    public int countStudentLate(String studentId, Long courseId) {
        if (studentId == null || courseId == null) {
            return 0;
        }
        return attendanceRepository.countByStudentIdAndCourseIdAndStatus(studentId, courseId, "迟到");
    }

    @Override
    public boolean updateAttendance(Attendance attendance) {
        if (attendance.getId() == null) {
            System.out.println("更新失败：ID不能为空");
            return false;
        }

        Optional<Attendance> existing = attendanceRepository.findById(attendance.getId());
        if (existing.isEmpty()) {
            System.out.println("更新失败：考勤记录不存在");
            return false;
        }

        attendanceRepository.save(attendance);
        System.out.println("考勤记录更新成功，ID：" + attendance.getId());
        return true;
    }

    @Override
    public boolean deleteAttendance(Long id) {
        if (id == null) {
            System.out.println("删除失败：ID不能为空");
            return false;
        }

        if (!attendanceRepository.existsById(id)) {
            System.out.println("删除失败：考勤记录不存在");
            return false;
        }

        attendanceRepository.deleteById(id);
        System.out.println("考勤记录删除成功，ID：" + id);
        return true;
    }

    @Override
    public boolean checkIn(String studentId, Long courseId, String status) {
        if (studentId == null || courseId == null) {
            return false;
        }

        LocalDate today = LocalDate.now();
        List<Attendance> todayRecords = attendanceRepository.findByStudentIdAndAttendanceDate(studentId, today);

        for (Attendance record : todayRecords) {
            if (record.getCourseId().equals(courseId)) {
                System.out.println("签到失败：今日已签到过该课程");
                return false;
            }
        }

        Attendance attendance = new Attendance();
        attendance.setStudentId(studentId);
        attendance.setCourseId(courseId);
        attendance.setCourseName("待补充");
        attendance.setStudentName("待补充");
        attendance.setAttendanceDate(today);
        attendance.setStatus(status);
        attendance.setCheckInTime(LocalDateTime.now());

        attendanceRepository.save(attendance);
        System.out.println("签到成功：" + studentId + " - " + status);
        return true;
    }
}