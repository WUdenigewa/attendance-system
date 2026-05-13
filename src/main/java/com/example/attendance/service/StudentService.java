package com.example.attendance.service;

import com.example.attendance.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface StudentService {

    // ========== 基础增删改查 ==========

    /**
     * 新增学生
     */
    boolean addStudent(Student student);

    /**
     * 根据ID查询学生
     */
    Student getStudentById(Long id);

    /**
     * 根据学号查询学生
     */
    Student getStudentByStudentId(String studentId);

    /**
     * 查询所有学生
     */
    List<Student> getAllStudents();

    /**
     * 根据班级查询学生
     */
    List<Student> getStudentsByClassName(String className);

    /**
     * 更新学生信息
     */
    boolean updateStudent(Student student);

    /**
     * 根据学号删除学生
     */
    boolean deleteStudent(String studentId);

    /**
     * 根据ID删除学生
     */
    boolean deleteStudentById(Long id);

    // ========== 分页查询方法 ==========

    /**
     * 分页查询所有学生
     */
    Page<Student> getAllStudentsPage(Pageable pageable);

    /**
     * 根据关键词搜索学生（姓名或学号模糊匹配）
     */
    Page<Student> searchByKeyword(String keyword, Pageable pageable);
}