package com.example.attendance.service.impl;

import com.example.attendance.entity.Student;
import com.example.attendance.repository.StudentRepository;
import com.example.attendance.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    // ========== 基础增删改查实现 ==========

    @Override
    public boolean addStudent(Student student) {
        // 1. 校验学号
        if (student.getStudentId() == null || student.getStudentId().trim().isEmpty()) {
            System.out.println("添加失败：学号不能为空");
            return false;
        }
        // 2. 校验姓名
        if (student.getName() == null || student.getName().trim().isEmpty()) {
            System.out.println("添加失败：姓名不能为空");
            return false;
        }
        // 3. 校验专业
        if (student.getMajor() == null || student.getMajor().trim().isEmpty()) {
            System.out.println("添加失败：专业不能为空");
            return false;
        }
        // 4. 校验班级
        if (student.getClassName() == null || student.getClassName().trim().isEmpty()) {
            System.out.println("添加失败：班级不能为空");
            return false;
        }
        // 5. 检查学号是否已存在
        if (studentRepository.existsByStudentId(student.getStudentId())) {
            System.out.println("添加失败：学号已存在 - " + student.getStudentId());
            return false;
        }
        // 6. 保存学生
        studentRepository.save(student);
        System.out.println("学生添加成功：" + student.getName());
        return true;
    }

    @Override
    public Student getStudentById(Long id) {
        if (id == null || id <= 0) {
            return null;
        }
        Optional<Student> student = studentRepository.findById(id);
        return student.orElse(null);
    }

    @Override
    public Student getStudentByStudentId(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            return null;
        }
        Optional<Student> student = studentRepository.findByStudentId(studentId);
        return student.orElse(null);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public List<Student> getStudentsByClassName(String className) {
        if (className == null || className.trim().isEmpty()) {
            return List.of();
        }
        return studentRepository.findByClassName(className);
    }

    @Override
    public boolean updateStudent(Student student) {
        // 1. 校验ID
        if (student.getId() == null) {
            System.out.println("更新失败：学生ID不能为空");
            return false;
        }
        // 2. 检查学生是否存在
        Optional<Student> existing = studentRepository.findById(student.getId());
        if (existing.isEmpty()) {
            System.out.println("更新失败：学生不存在，ID：" + student.getId());
            return false;
        }
        // 3. 更新学生
        studentRepository.save(student);
        System.out.println("学生更新成功：" + student.getName());
        return true;
    }

    @Override
    public boolean deleteStudent(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            System.out.println("删除失败：学号不能为空");
            return false;
        }
        if (!studentRepository.existsByStudentId(studentId)) {
            System.out.println("删除失败：学生不存在，学号：" + studentId);
            return false;
        }
        studentRepository.deleteByStudentId(studentId);
        System.out.println("学生删除成功，学号：" + studentId);
        return true;
    }

    @Override
    public boolean deleteStudentById(Long id) {
        if (id == null || id <= 0) {
            System.out.println("删除失败：学生ID不能为空");
            return false;
        }
        if (!studentRepository.existsById(id)) {
            System.out.println("删除失败：学生不存在，ID：" + id);
            return false;
        }
        studentRepository.deleteById(id);
        System.out.println("学生删除成功，ID：" + id);
        return true;
    }

    // ========== 分页查询实现 ==========

    @Override
    public Page<Student> getAllStudentsPage(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }

    @Override
    public Page<Student> searchByKeyword(String keyword, Pageable pageable) {
        return studentRepository.findByNameContainingOrStudentIdContaining(keyword, keyword, pageable);
    }
}