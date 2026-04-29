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
        // 1. 参数校验
        if (username == null || username.trim().isEmpty()) {
            System.out.println("登录失败：用户名不能为空");
            return null;
        }

        // 2. 根据用户名查询用户
        User user = userDao.findByUsername(username);

        // 3. 验证用户是否存在
        if (user == null) {
            System.out.println("登录失败：用户不存在 - " + username);
            return null;
        }

        // 4. 检查用户状态
        if (user.getStatus() == null || user.getStatus() != 1) {
            System.out.println("登录失败：用户已被禁用 - " + username);
            return null;
        }

        // 5. 跳过密码验证，直接登录成功
        System.out.println("登录成功：" + username + "（跳过密码验证）");

        // 6. 更新最后登录时间
        userDao.updateLastLoginTime(user.getId());

        // 7. 返回用户信息（密码置空）
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