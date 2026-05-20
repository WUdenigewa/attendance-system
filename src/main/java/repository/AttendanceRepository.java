package com.example.attendance.repository;

import com.example.attendance.entity.Attendance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long>, JpaSpecificationExecutor<Attendance> {

    List<Attendance> findByStudentId(String studentId);

    List<Attendance> findByStudentIdAndCourseId(String studentId, Long courseId);

    List<Attendance> findByStudentIdAndCheckInTimeBetween(String studentId, LocalDateTime start, LocalDateTime end);

    List<Attendance> findByStudentIdAndCourseIdAndCheckInTimeBetween(String studentId, Long courseId, LocalDateTime start, LocalDateTime end);

    Page<Attendance> findByStudentId(String studentId, Pageable pageable);

    List<Attendance> findByStudentIdAndStatus(String studentId, String status);
}