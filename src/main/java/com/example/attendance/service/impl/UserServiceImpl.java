package com.example.attendance.service.impl;

import com.example.attendance.dao.UserDao;
import com.example.attendance.entity.User;
import com.example.attendance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    // ========== 原有的其他方法保持不变 ==========

    @Override
    public boolean addUser(User user) {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            System.out.println("添加失败：用户名不能为空");
            return false;
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            System.out.println("添加失败：密码不能为空");
            return false;
        }
        if (userDao.existsByUsername(user.getUsername())) {
            System.out.println("添加失败：用户名已存在 - " + user.getUsername());
            return false;
        }
        if (user.getStatus() == null) {
            user.setStatus(1);
        }
        int result = userDao.insert(user);
        return result > 0;
    }

    @Override
    public User getUserById(Long id) {
        if (id == null || id <= 0) {
            return null;
        }
        return userDao.findById(id);
    }

    @Override
    public User getUserByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return null;
        }
        return userDao.findByUsername(username);
    }

    // ========== 新增：根据学号查询用户 ==========
    @Override
    public User getUserByStudentId(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            return null;
        }
        return userDao.findByStudentId(studentId);
    }

    @Override
    public List<User> getAllTeachers() {
        return userDao.findAllTeachers();
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @Override
    public boolean updateUser(User user) {
        if (user.getId() == null || user.getId() <= 0) {
            System.out.println("更新失败：用户ID不能为空");
            return false;
        }
        User existingUser = userDao.findById(user.getId());
        if (existingUser == null) {
            System.out.println("更新失败：用户不存在，ID：" + user.getId());
            return false;
        }
        int result = userDao.update(user);
        return result > 0;
    }

    @Override
    public boolean deleteUser(Long id) {
        if (id == null || id <= 0) {
            System.out.println("删除失败：用户ID不能为空");
            return false;
        }
        User existingUser = userDao.findById(id);
        if (existingUser == null) {
            System.out.println("删除失败：用户不存在，ID：" + id);
            return false;
        }
        int result = userDao.deleteById(id);
        return result > 0;
    }

    @Override
    public User login(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            System.out.println("登录失败：用户名不能为空");
            return null;
        }
        if (password == null || password.trim().isEmpty()) {
            System.out.println("登录失败：密码不能为空");
            return null;
        }
        User user = userDao.findByUsername(username);
        if (user == null) {
            System.out.println("登录失败：用户不存在 - " + username);
            return null;
        }
        if (user.getStatus() == null || user.getStatus() != 1) {
            System.out.println("登录失败：用户已被禁用 - " + username);
            return null;
        }
        System.out.println("登录成功：" + username);
        userDao.updateLastLoginTime(user.getId());
        user.setPassword(null);
        return user;
    }

    @Override
    public boolean register(User user) {
        return addUser(user);
    }

    @Override
    public boolean existsByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        return userDao.existsByUsername(username);
    }
}