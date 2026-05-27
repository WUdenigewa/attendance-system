package com.example.attendance.repository;

import com.example.attendance.entity.Attendance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long>, JpaSpecificationExecutor<Attendance> {

    // ========== 基础查询方法 ==========

    List<Attendance> findByStudentId(String studentId);
    List<Attendance> findByStudentIdAndCourseId(String studentId, Long courseId);
    List<Attendance> findByStudentIdAndCheckInTimeBetween(String studentId, LocalDateTime start, LocalDateTime end);
    List<Attendance> findByStudentIdAndCourseIdAndCheckInTimeBetween(String studentId, Long courseId, LocalDateTime start, LocalDateTime end);
    Page<Attendance> findByStudentId(String studentId, Pageable pageable);
    List<Attendance> findByStudentIdAndStatus(String studentId, String status);

    // ========== 分页查询（支持 Specification） ==========

    Page<Attendance> findAll(Specification<Attendance> spec, Pageable pageable);

    // ========== 统计方法 ==========

    /**
     * 统计学生总考勤次数
     */
    long countByStudentId(String studentId);

    /**
     * 统计学生某状态的考勤次数
     */
    long countByStudentIdAndStatus(String studentId, String status);

    /**
     * 按时间范围统计考勤次数
     */
    long countByStudentIdAndCheckInTimeBetween(String studentId, LocalDateTime start, LocalDateTime end);

    /**
     * 按时间范围和状态统计考勤次数
     */
    long countByStudentIdAndStatusAndCheckInTimeBetween(String studentId, String status, LocalDateTime start, LocalDateTime end);

    /**
     * 按状态分组统计
     */
    @Query("SELECT a.status, COUNT(a) FROM Attendance a WHERE a.studentId = :studentId GROUP BY a.status")
    List<Object[]> countGroupByStatus(@Param("studentId") String studentId);

    /**
     * 统计某学生某课程的总考勤次数
     */
    long countByStudentIdAndCourseId(String studentId, Long courseId);

    /**
     * 统计某学生某课程某状态的次数
     */
    long countByStudentIdAndCourseIdAndStatus(String studentId, Long courseId, String status);
}