package com.example.attendance.repository;

import com.example.attendance.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // ========== 基础查询方法 ==========

    Optional<Student> findByStudentId(String studentId);
    List<Student> findByClassName(String className);
    List<Student> findByNameContaining(String name);
    List<Student> findByMajor(String major);
    boolean existsByStudentId(String studentId);
    void deleteByStudentId(String studentId);

    // ========== 补充的条件查询方法 ==========

    /**
     * 根据班级和专业联合查询
     */
    List<Student> findByClassNameAndMajor(String className, String major);

    /**
     * 根据班级和姓名模糊查询
     */
    List<Student> findByClassNameAndNameContaining(String className, String name);

    /**
     * 根据专业和姓名模糊查询
     */
    List<Student> findByMajorAndNameContaining(String major, String name);

    /**
     * 查询某个班级的所有学生，按姓名排序
     */
    List<Student> findByClassNameOrderByNameAsc(String className);

    /**
     * 查询某个专业的学生数量
     */
    int countByMajor(String major);

    /**
     * 查询班级学生数量
     */
    int countByClassName(String className);

    /**
     * 查询学号在某范围的学生（大于指定学号）
     */
    List<Student> findByStudentIdGreaterThan(String studentId);

    /**
     * 查询姓名不为空的学生
     */
    List<Student> findByNameIsNotNull();

    // ========== JPQL 自定义查询 ==========

    /**
     * 查询指定班级的学生，按学号排序
     */
    @Query("SELECT s FROM Student s WHERE s.className = :className ORDER BY s.studentId")
    List<Student> findStudentsByClassNameOrderByStudentId(@Param("className") String className);

    /**
     * 模糊查询班级和专业
     */
    @Query("SELECT s FROM Student s WHERE s.className LIKE %:className% AND s.major LIKE %:major%")
    List<Student> searchByClassNameAndMajor(@Param("className") String className, @Param("major") String major);

    /**
     * 统计各班级学生数量
     */
    @Query("SELECT s.className, COUNT(s) FROM Student s GROUP BY s.className")
    List<Object[]> countStudentsByClass();
}