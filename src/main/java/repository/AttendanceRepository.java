package com.example.attendance.repository;

import com.example.attendance.entity.Attendance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long>, JpaSpecificationExecutor<Attendance> {

    // ========== 基础查询方法 ==========

    List<Attendance> findByStudentId(String studentId);
    List<Attendance> findByStudentId(String studentId, Sort sort);
    List<Attendance> findByCourseId(Long courseId);
    List<Attendance> findByCourseId(Long courseId, Sort sort);
    List<Attendance> findByAttendanceDate(LocalDate date);
    List<Attendance> findByStudentIdAndCourseId(String studentId, Long courseId);
    List<Attendance> findByStudentIdAndAttendanceDate(String studentId, LocalDate date);
    List<Attendance> findByCourseIdAndAttendanceDate(Long courseId, LocalDate date);
    List<Attendance> findByStatus(String status);

    // ========== 分页查询方法 ==========

    Page<Attendance> findAll(Pageable pageable);
    Page<Attendance> findByStudentId(String studentId, Pageable pageable);
    Page<Attendance> findByCourseId(Long courseId, Pageable pageable);
    Page<Attendance> findByAttendanceDate(LocalDate date, Pageable pageable);
    Page<Attendance> findByStatus(String status, Pageable pageable);
    Page<Attendance> findByStudentIdAndCourseId(String studentId, Long courseId, Pageable pageable);

    // ========== 统计方法 ==========

    int countByCourseIdAndAttendanceDate(Long courseId, LocalDate date);
    int countByStudentIdAndCourseId(String studentId, Long courseId);
    int countByStudentIdAndCourseIdAndStatus(String studentId, Long courseId, String status);

    // ========== 日期范围查询 ==========

    List<Attendance> findByStudentIdAndAttendanceDateBetween(String studentId, LocalDate startDate, LocalDate endDate);
    List<Attendance> findByCourseIdAndAttendanceDateBetween(Long courseId, LocalDate startDate, LocalDate endDate);
    List<Attendance> findByStudentIdAndCourseIdAndAttendanceDateBetween(String studentId, Long courseId, LocalDate startDate, LocalDate endDate);

    // ========== JPQL 自定义查询 ==========

    @Query("SELECT a FROM Attendance a WHERE a.attendanceDate BETWEEN :startDate AND :endDate")
    Page<Attendance> findByDateRange(@Param("startDate") LocalDate startDate,
                                     @Param("endDate") LocalDate endDate,
                                     Pageable pageable);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.courseId = :courseId AND a.attendanceDate = CURRENT_DATE")
    int countTodayAttendanceByCourse(@Param("courseId") Long courseId);

    @Query("SELECT a FROM Attendance a WHERE a.studentId = :studentId AND a.attendanceDate = CURRENT_DATE")
    List<Attendance> findTodayAttendanceByStudent(@Param("studentId") String studentId);

    @Query("SELECT a.status, COUNT(a) FROM Attendance a WHERE a.courseId = :courseId AND a.attendanceDate = :date GROUP BY a.status")
    List<Object[]> countAttendanceStatusByCourseAndDate(@Param("courseId") Long courseId, @Param("date") LocalDate date);

    @Query("SELECT a FROM Attendance a WHERE a.courseId = :courseId AND a.attendanceDate = CURRENT_DATE AND a.status = '迟到'")
    List<Attendance> findLateStudentsToday(@Param("courseId") Long courseId);

    @Query("SELECT a.status, COUNT(a) FROM Attendance a WHERE a.studentId = :studentId AND MONTH(a.attendanceDate) = :month GROUP BY a.status")
    List<Object[]> countMonthlyAttendanceByStudent(@Param("studentId") String studentId, @Param("month") int month);

    @Query("SELECT a FROM Attendance a ORDER BY a.attendanceDate DESC")
    List<Attendance> findAllOrderByDateDesc();

    @Query("SELECT a FROM Attendance a ORDER BY a.studentId ASC")
    List<Attendance> findAllOrderByStudentIdAsc();
}