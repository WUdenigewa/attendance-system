package com.example.attendance.repository;

import com.example.attendance.entity.Attendance;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class AttendanceSpecification {

    /**
     * 多条件动态查询
     * @param studentId 学生学号（可选）
     * @param courseId 课程ID（可选）
     * @param status 考勤状态（可选）
     * @param startDate 开始日期（可选）
     * @param endDate 结束日期（可选）
     * @return Specification 对象
     */
    public static Specification<Attendance> buildQuery(
            String studentId,
            Long courseId,
            String status,
            LocalDate startDate,
            LocalDate endDate) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. 学生学号条件（精确匹配）
            if (studentId != null && !studentId.trim().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("studentId"), studentId));
            }

            // 2. 课程ID条件（精确匹配）
            if (courseId != null) {
                predicates.add(criteriaBuilder.equal(root.get("courseId"), courseId));
            }

            // 3. 考勤状态条件（精确匹配）
            if (status != null && !status.trim().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            // 4. 开始日期条件（大于等于）
            if (startDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("attendanceDate"), startDate));
            }

            // 5. 结束日期条件（小于等于）
            if (endDate != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("attendanceDate"), endDate));
            }

            // 组合所有条件
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * 按学生学号和日期范围查询
     */
    public static Specification<Attendance> findByStudentIdAndDateRange(
            String studentId,
            LocalDate startDate,
            LocalDate endDate) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (studentId != null && !studentId.trim().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("studentId"), studentId));
            }

            if (startDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("attendanceDate"), startDate));
            }

            if (endDate != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("attendanceDate"), endDate));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * 按课程ID和日期范围查询
     */
    public static Specification<Attendance> findByCourseIdAndDateRange(
            Long courseId,
            LocalDate startDate,
            LocalDate endDate) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (courseId != null) {
                predicates.add(criteriaBuilder.equal(root.get("courseId"), courseId));
            }

            if (startDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("attendanceDate"), startDate));
            }

            if (endDate != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("attendanceDate"), endDate));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * 查询缺勤学生（某课程某日期没有考勤记录）
     * 注意：这个需要关联学生表，实际实现可能需要更复杂的查询
     */
    public static Specification<Attendance> findAbsentStudents(
            Long courseId,
            LocalDate date) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (courseId != null) {
                predicates.add(criteriaBuilder.equal(root.get("courseId"), courseId));
            }

            if (date != null) {
                predicates.add(criteriaBuilder.equal(root.get("attendanceDate"), date));
            }

            // 状态为旷课
            predicates.add(criteriaBuilder.equal(root.get("status"), "旷课"));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}