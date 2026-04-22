package com.example.attendance.service;

import com.example.attendance.entity.Attendance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {

    // ========== 基础方法 ==========

    boolean addAttendance(Attendance attendance);
    Attendance getAttendanceById(Long id);
    List<Attendance> getAttendanceByStudentId(String studentId);
    List<Attendance> getAttendanceByCourseId(Long courseId);
    List<Attendance> getAttendanceByDate(LocalDate date);
    List<Attendance> getStudentCourseAttendance(String studentId, Long courseId);
    int countAttendanceByCourseAndDate(Long courseId, LocalDate date);
    int countStudentLate(String studentId, Long courseId);
    boolean updateAttendance(Attendance attendance);
    boolean deleteAttendance(Long id);
    boolean checkIn(String studentId, Long courseId, String status);

    // ========== 分页方法 ==========

    Page<Attendance> getAttendancePage(Pageable pageable);
    Page<Attendance> getAttendancePageByStudentId(String studentId, Pageable pageable);
    Page<Attendance> getAttendancePageByCourseId(Long courseId, Pageable pageable);
    Page<Attendance> getAttendancePageByDate(LocalDate date, Pageable pageable);
    Page<Attendance> getAttendancePageByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable);

    // ========== 排序方法 ==========

    List<Attendance> getAllAttendanceSorted(Sort sort);
    List<Attendance> getStudentAttendanceSorted(String studentId, Sort sort);
    List<Attendance> getCourseAttendanceSorted(Long courseId, Sort sort);
    List<Attendance> getAttendanceWithMultiSort(List<String> sorts);

    // ========== 多条件查询方法 ==========

    List<Attendance> searchAttendance(String studentId, Long courseId, String status,
                                      LocalDate startDate, LocalDate endDate);
    Page<Attendance> searchAttendancePage(String studentId, Long courseId, String status,
                                          LocalDate startDate, LocalDate endDate, Pageable pageable);
}