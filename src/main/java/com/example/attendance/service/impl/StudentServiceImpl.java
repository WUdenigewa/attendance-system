package com.example.attendance.service.impl;

import com.example.attendance.dao.StudentDao;
import com.example.attendance.entity.Student;
import com.example.attendance.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentDao studentDao;  // 注入 Dao 层

    @Override
    public boolean addStudent(Student student) {
        // 业务校验1：学号不能为空
        if (student.getStudentId() == null || student.getStudentId().trim().isEmpty()) {
            System.out.println("业务校验失败：学号不能为空");
            return false;
        }

        // 业务校验2：姓名不能为空
        if (student.getName() == null || student.getName().trim().isEmpty()) {
            System.out.println("业务校验失败：姓名不能为空");
            return false;
        }

        // 业务校验3：专业不能为空
        if (student.getMajor() == null || student.getMajor().trim().isEmpty()) {
            System.out.println("业务校验失败：专业不能为空");
            return false;
        }

        // 业务校验4：班级不能为空
        if (student.getClassName() == null || student.getClassName().trim().isEmpty()) {
            System.out.println("业务校验失败：班级不能为空");
            return false;
        }

        // 业务校验5：电话不能为空
        if (student.getPhone() == null || student.getPhone().trim().isEmpty()) {
            System.out.println("业务校验失败：电话不能为空");
            return false;
        }

        // 业务校验6：邮箱不能为空
        if (student.getEmail() == null || student.getEmail().trim().isEmpty()) {
            System.out.println("业务校验失败：邮箱不能为空");
            return false;
        }

        // 业务校验7：学号格式校验（8位学号：2024开头+4位数字）
        if (!student.getStudentId().matches("^2024\\d{4}$")) {
            System.out.println("业务校验失败：学号格式不正确，应为2024开头+4位数字（共8位）");
            return false;
        }

        // 业务校验8：学号是否已存在
        if (studentDao.existsByStudentId(student.getStudentId())) {
            System.out.println("业务校验失败：学号 " + student.getStudentId() + " 已存在");
            return false;
        }

        // 调用 Dao 层保存数据
        int result = studentDao.insert(student);
        return result > 0;
    }

    @Override
    public Student getStudentByStudentId(String studentId) {
        // 业务校验：学号不能为空
        if (studentId == null || studentId.trim().isEmpty()) {
            return null;
        }
        // 调用 Dao 层查询
        return studentDao.findByStudentId(studentId);
    }

    @Override
    public List<Student> getAllStudents() {
        // 调用 Dao 层查询所有
        return studentDao.findAll();
    }

    @Override
    public List<Student> getStudentsByClassName(String className) {
        // 业务校验：班级名称为空时返回空列表
        if (className == null || className.trim().isEmpty()) {
            return List.of();
        }
        // 调用 Dao 层按班级查询
        return studentDao.findByClassName(className);
    }

    @Override
    public boolean updateStudent(Student student) {
        // 业务校验：学号不能为空
        if (student.getStudentId() == null || student.getStudentId().trim().isEmpty()) {
            return false;
        }

        // 业务校验：学生是否存在
        if (!studentDao.existsByStudentId(student.getStudentId())) {
            return false;
        }

        // 调用 Dao 层更新
        int result = studentDao.update(student);
        return result > 0;
    }

    @Override
    public boolean deleteStudent(String studentId) {
        // 业务校验：学号不能为空
        if (studentId == null || studentId.trim().isEmpty()) {
            return false;
        }

        // 业务校验：学生是否存在
        if (!studentDao.existsByStudentId(studentId)) {
            return false;
        }

        // 调用 Dao 层删除
        int result = studentDao.deleteByStudentId(studentId);
        return result > 0;
    }
}