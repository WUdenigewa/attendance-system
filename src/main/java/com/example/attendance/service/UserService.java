package com.example.attendance.service;

import com.example.attendance.entity.User;
import java.util.List;

public interface UserService {

    // ========== 增删改查方法 ==========

    boolean addUser(User user);
    User getUserById(Long id);
    User getUserByUsername(String username);
    List<User> getAllTeachers();
    List<User> getAllUsers();
    boolean updateUser(User user);
    boolean deleteUser(Long id);

    // ========== 新增：根据学号查询用户 ==========
    User getUserByStudentId(String studentId);

    // ========== 认证相关方法 ==========

    User login(String username, String password);
    boolean register(User user);
    boolean existsByUsername(String username);
}