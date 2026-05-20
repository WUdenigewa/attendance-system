package com.example.attendance.repository;

import com.example.attendance.entity.Attendance;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AttendanceSpecification {

    public static Specification<Attendance> buildQuery(
            String studentId,
            Long courseId,
            String status,
            LocalDate startDate,
            LocalDate endDate) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (studentId != null && !studentId.trim().isEmpty()) {
                predicates.add(cb.equal(root.get("studentId"), studentId));
            }
            if (courseId != null) {
                predicates.add(cb.equal(root.get("courseId"), courseId));
            }
            if (status != null && !status.trim().isEmpty()) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            if (startDate != null) {
                LocalDateTime start = startDate.atStartOfDay();
                predicates.add(cb.greaterThanOrEqualTo(root.get("checkInTime"), start));
            }
            if (endDate != null) {
                LocalDateTime end = endDate.atStartOfDay().plusDays(1);
                predicates.add(cb.lessThan(root.get("checkInTime"), end));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}