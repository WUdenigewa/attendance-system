package com.example.attendance.controller;

import com.example.attendance.common.Result;
import com.example.attendance.entity.User;
import com.example.attendance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册/新增用户
     * POST /api/user/register
     */
    @PostMapping("/register")
    public Result<String> register(@RequestBody User user) {
        boolean success = userService.addUser(user);
        if (success) {
            return Result.success("用户注册成功：" + user.getRealName());
        } else {
            return Result.error("用户注册失败，请检查用户名是否已存在");
        }
    }

    /**
     * 用户登录（支持 GET 请求，方便浏览器测试）
     * GET /api/user/login?username=xxx&password=xxx
     */
    @GetMapping("/login")
    public Result<User> login(@RequestParam String username, @RequestParam String password) {
        User user = userService.login(username, password);
        if (user != null) {
            user.setPassword(null);  // 隐藏密码
            return Result.success(user);
        } else {
            return Result.error("用户名或密码错误");
        }
    }

    /**
     * 根据ID查询用户
     * GET /api/user/{id}
     */
    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            user.setPassword(null);
            return Result.success(user);
        } else {
            return Result.error("用户不存在");
        }
    }

    /**
     * 根据用户名查询用户
     * GET /api/user/username/{username}
     */
    @GetMapping("/username/{username}")
    public Result<User> getUserByUsername(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        if (user != null) {
            user.setPassword(null);
            return Result.success(user);
        } else {
            return Result.error("用户不存在");
        }
    }

    /**
     * 查询所有教师
     * GET /api/user/teachers
     */
    @GetMapping("/teachers")
    public Result<List<User>> getAllTeachers() {
        List<User> teachers = userService.getAllTeachers();
        teachers.forEach(t -> t.setPassword(null));
        return Result.success(teachers);
    }

    /**
     * 查询所有用户
     * GET /api/user/all
     */
    @GetMapping("/all")
    public Result<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        users.forEach(u -> u.setPassword(null));
        return Result.success(users);
    }

    /**
     * 更新用户信息
     * PUT /api/user/update
     */
    @PutMapping("/update")
    public Result<String> updateUser(@RequestBody User user) {
        boolean success = userService.updateUser(user);
        if (success) {
            return Result.success("用户信息更新成功");
        } else {
            return Result.error("用户信息更新失败");
        }
    }

    /**
     * 删除用户
     * DELETE /api/user/delete/{id}
     */
    @DeleteMapping("/delete/{id}")
    public Result<String> deleteUser(@PathVariable Long id) {
        boolean success = userService.deleteUser(id);
        if (success) {
            return Result.success("用户删除成功");
        } else {
            return Result.error("用户删除失败");
        }
    }
}