package com.example.attendance.repository;

import com.example.attendance.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // ========== 基础查询方法 ==========

    /**
     * 根据学号查询学生
     */
    Optional<Student> findByStudentId(String studentId);

    /**
     * 根据班级查询学生列表
     */
    List<Student> findByClassName(String className);

    /**
     * 判断学号是否存在
     */
    boolean existsByStudentId(String studentId);

    /**
     * 根据学号删除学生
     */
    void deleteByStudentId(String studentId);

    // ========== 分页查询方法 ==========

    /**
     * 分页查询所有学生
     */
    Page<Student> findAll(Pageable pageable);

    /**
     * 根据姓名或学号模糊搜索（分页）
     * @param name 姓名关键词
     * @param studentId 学号关键词
     * @param pageable 分页参数
     * @return 分页结果
     */
    Page<Student> findByNameContainingOrStudentIdContaining(String name, String studentId, Pageable pageable);
}