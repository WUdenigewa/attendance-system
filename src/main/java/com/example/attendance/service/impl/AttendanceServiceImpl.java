package com.example.attendance.service.impl;

import com.example.attendance.entity.Attendance;
import com.example.attendance.repository.AttendanceRepository;
import com.example.attendance.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        if (attendance.getStudentId() == null) {
            return false;
        }
        attendanceRepository.save(attendance);
        return true;
    }

    @Override
    public Attendance getAttendanceById(Long id) {
        Optional<Attendance> attendance = attendanceRepository.findById(id);
        return attendance.orElse(null);
    }

    @Override
    public List<Attendance> getAttendanceByStudentId(String studentId) {
        return attendanceRepository.findByStudentId(studentId);
    }

    @Override
    public List<Attendance> getAttendanceByCourseId(Long courseId) {
        return attendanceRepository.findByStudentIdAndCourseId(null, courseId);
    }

    @Override
    public List<Attendance> getAttendanceByDate(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = start.plusDays(1);
        return attendanceRepository.findByStudentIdAndCheckInTimeBetween(null, start, end);
    }

    @Override
    public List<Attendance> getStudentCourseAttendance(String studentId, Long courseId) {
        return attendanceRepository.findByStudentIdAndCourseId(studentId, courseId);
    }

    @Override
    public int countAttendanceByCourseAndDate(Long courseId, LocalDate date) {
        List<Attendance> list = getAttendanceByDate(date);
        return (int) list.stream().filter(a -> a.getCourseId().equals(courseId)).count();
    }

    @Override
    public int countStudentLate(String studentId, Long courseId) {
        List<Attendance> list = attendanceRepository.findByStudentIdAndCourseId(studentId, courseId);
        return (int) list.stream().filter(a -> "LATE".equals(a.getStatus())).count();
    }

    @Override
    public boolean updateAttendance(Attendance attendance) {
        if (attendance.getId() == null) {
            return false;
        }
        attendanceRepository.save(attendance);
        return true;
    }

    @Override
    public boolean deleteAttendance(Long id) {
        attendanceRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean checkIn(String studentId, Long courseId, String status) {
        Attendance attendance = new Attendance();
        attendance.setStudentId(studentId);
        attendance.setCourseId(courseId);
        attendance.setCheckInTime(LocalDateTime.now());
        attendance.setStatus(status);
        attendanceRepository.save(attendance);
        return true;
    }

    @Override
    public Page<Attendance> getAttendancePage(Pageable pageable) {
        return attendanceRepository.findAll(pageable);
    }

    @Override
    public Page<Attendance> getAttendancePageByStudentId(String studentId, Pageable pageable) {
        return attendanceRepository.findByStudentId(studentId, pageable);
    }

    @Override
    public Page<Attendance> getAttendancePageByCourseId(Long courseId, Pageable pageable) {
        return attendanceRepository.findAll(pageable);
    }

    @Override
    public Page<Attendance> getAttendancePageByDate(LocalDate date, Pageable pageable) {
        return attendanceRepository.findAll(pageable);
    }

    @Override
    public Page<Attendance> getAttendancePageByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return attendanceRepository.findAll(pageable);
    }

    @Override
    public List<Attendance> getAllAttendanceSorted(Sort sort) {
        return attendanceRepository.findAll(sort);
    }

    @Override
    public List<Attendance> getStudentAttendanceSorted(String studentId, Sort sort) {
        return attendanceRepository.findByStudentId(studentId);
    }

    @Override
    public List<Attendance> getCourseAttendanceSorted(Long courseId, Sort sort) {
        return attendanceRepository.findByStudentIdAndCourseId(null, courseId);
    }

    @Override
    public List<Attendance> getAttendanceWithMultiSort(List<String> sorts) {
        return attendanceRepository.findAll();
    }

    @Override
    public List<Attendance> searchAttendance(String studentId, Long courseId, String status,
                                             LocalDate startDate, LocalDate endDate) {
        return attendanceRepository.findAll();
    }

    @Override
    public Page<Attendance> searchAttendancePage(String studentId, Long courseId, String status,
                                                 LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return attendanceRepository.findAll(pageable);
    }
}