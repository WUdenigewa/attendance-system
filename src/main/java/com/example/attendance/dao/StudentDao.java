package com.example.attendance.dao;

import com.example.attendance.entity.Student;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class StudentDao {

    // 模拟数据库：用 Map 存储学生数据
    private static Map<String, Student> studentDB = new ConcurrentHashMap<>();

    // 模拟自增ID
    private static AtomicLong idGenerator = new AtomicLong(1);

    // 静态初始化：添加测试数据
    static {
        Student s1 = new Student("20240001", "张三", "软件工程", "软件1班", "13800138001", "zhangsan@example.com");
        Student s2 = new Student("20240002", "李四", "软件工程", "软件1班", "13800138002", "lisi@example.com");
        Student s3 = new Student("20240003", "王五", "软件工程", "软件2班", "13800138003", "wangwu@example.com");

        studentDB.put(s1.getStudentId(), s1);
        studentDB.put(s2.getStudentId(), s2);
        studentDB.put(s3.getStudentId(), s3);
    }

    /**
     * 新增学生
     * @param student 学生对象
     * @return 影响行数（模拟：1表示成功，0表示失败）
     */
    public int insert(Student student) {
        // 检查学号是否已存在
        if (studentDB.containsKey(student.getStudentId())) {
            return 0; // 学号已存在，插入失败
        }
        studentDB.put(student.getStudentId(), student);
        return 1; // 插入成功
    }

    /**
     * 根据学号查询学生
     * @param studentId 学号
     * @return 学生对象
     */
    public Student findByStudentId(String studentId) {
        return studentDB.get(studentId);
    }

    /**
     * 查询所有学生
     * @return 学生列表
     */
    public List<Student> findAll() {
        return new ArrayList<>(studentDB.values());
    }

    /**
     * 根据班级查询学生
     * @param className 班级名称
     * @return 学生列表
     */
    public List<Student> findByClassName(String className) {
        List<Student> result = new ArrayList<>();
        for (Student student : studentDB.values()) {
            if (className.equals(student.getClassName())) {
                result.add(student);
            }
        }
        return result;
    }

    /**
     * 更新学生信息
     * @param student 学生对象
     * @return 影响行数
     */
    public int update(Student student) {
        if (!studentDB.containsKey(student.getStudentId())) {
            return 0; // 学生不存在
        }
        studentDB.put(student.getStudentId(), student);
        return 1;
    }

    /**
     * 根据学号删除学生
     * @param studentId 学号
     * @return 影响行数
     */
    public int deleteByStudentId(String studentId) {
        Student removed = studentDB.remove(studentId);
        return removed != null ? 1 : 0;
    }

    /**
     * 检查学号是否存在
     * @param studentId 学号
     * @return 是否存在
     */
    public boolean existsByStudentId(String studentId) {
        return studentDB.containsKey(studentId);
    }
}