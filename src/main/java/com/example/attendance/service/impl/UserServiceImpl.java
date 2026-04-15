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

    @Override
    public boolean addUser(User user) {
        // 业务校验：用户名不能为空
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            System.out.println("用户名不能为空");
            return false;
        }

        // 业务校验：密码不能为空
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            System.out.println("密码不能为空");
            return false;
        }

        // 业务校验：用户名是否已存在
        if (userDao.existsByUsername(user.getUsername())) {
            System.out.println("用户名已存在：" + user.getUsername());
            return false;
        }

        // 调用 Dao 层插入
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
            System.out.println("用户ID不能为空");
            return false;
        }

        // 检查用户是否存在
        User existingUser = userDao.findById(user.getId());
        if (existingUser == null) {
            System.out.println("用户不存在，ID：" + user.getId());
            return false;
        }

        int result = userDao.update(user);
        return result > 0;
    }

    @Override
    public boolean deleteUser(Long id) {
        if (id == null || id <= 0) {
            System.out.println("用户ID不能为空");
            return false;
        }

        // 检查用户是否存在
        User existingUser = userDao.findById(id);
        if (existingUser == null) {
            System.out.println("用户不存在，ID：" + id);
            return false;
        }

        int result = userDao.deleteById(id);
        return result > 0;
    }

    @Override
    public User login(String username, String password) {
        // 参数校验
        if (username == null || username.trim().isEmpty()) {
            System.out.println("用户名不能为空");
            return null;
        }
        if (password == null || password.trim().isEmpty()) {
            System.out.println("密码不能为空");
            return null;
        }

        // 查询用户
        User user = userDao.findByUsername(username);
        if (user == null) {
            System.out.println("用户不存在：" + username);
            return null;
        }

        // 验证密码
        if (!password.equals(user.getPassword())) {
            System.out.println("密码错误");
            return null;
        }

        // 更新最后登录时间
        userDao.updateLastLoginTime(user.getId());

        return user;
    }
}