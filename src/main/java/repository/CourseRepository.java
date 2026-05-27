package com.example.attendance.repository;

import com.example.attendance.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByStatus(Integer status);

    /**
     * 根据课程名称查询课程ID
     */
    @Query("SELECT c.id FROM Course c WHERE c.courseName = :courseName")
    Long findIdByCourseName(@Param("courseName") String courseName);
}