package com.example.attendance.service;

import com.example.attendance.entity.Student;
import java.util.List;

public interface StudentService {

    /**
     * 新增学生
     * @param student 学生对象
     * @return 是否成功
     */
    boolean addStudent(Student student);

    /**
     * 根据学号查询学生
     * @param studentId 学号
     * @return 学生对象
     */
    Student getStudentByStudentId(String studentId);

    /**
     * 查询所有学生
     * @return 学生列表
     */
    List<Student> getAllStudents();

    /**
     * 根据班级查询学生
     * @param className 班级名称
     * @return 学生列表
     */
    List<Student> getStudentsByClassName(String className);

    /**
     * 更新学生信息
     * @param student 学生对象
     * @return 是否成功
     */
    boolean updateStudent(Student student);

    /**
     * 删除学生
     * @param studentId 学号
     * @return 是否成功
     */
    boolean deleteStudent(String studentId);
}