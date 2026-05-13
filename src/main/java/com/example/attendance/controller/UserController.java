package com.example.attendance.controller;

import com.example.attendance.common.Result;
import com.example.attendance.entity.LoginRequest;
import com.example.attendance.entity.RegisterRequest;
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

    // ========== 认证接口 ==========

    /**
     * 用户注册
     * POST /api/user/register
     */
    @PostMapping("/register")
    public Result<String> register(@RequestBody RegisterRequest request) {
        if (userService.existsByUsername(request.getUsername())) {
            return Result.error("用户名已存在");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole());
        user.setRealName(request.getRealName());
        user.setStudentId(request.getStudentId());
        user.setTeacherNo(request.getTeacherNo());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setGender(request.getGender());
        user.setDepartment(request.getDepartment());
        user.setStatus(1);

        boolean success = userService.register(user);

        if (success) {
            return Result.success("注册成功，用户名：" + request.getUsername());
        } else {
            return Result.error("注册失败，请稍后重试");
        }
    }

    /**
     * 用户登录（POST 方式）
     * POST /api/user/login
     */
    @PostMapping("/login")
    public Result<User> login(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        if (username == null || username.trim().isEmpty()) {
            return Result.error("用户名不能为空");
        }
        if (password == null || password.trim().isEmpty()) {
            return Result.error("密码不能为空");
        }

        User user = userService.login(username, password);

        if (user != null) {
            return Result.success(user);
        } else {
            return Result.error("用户名或密码错误");
        }
    }

    /**
     * 用户登录（GET 方式，方便浏览器测试）
     * GET /api/user/login?username=xxx&password=xxx
     */
    @GetMapping("/login")
    public Result<User> loginGet(@RequestParam String username, @RequestParam String password) {
        User user = userService.login(username, password);
        if (user != null) {
            return Result.success(user);
        } else {
            return Result.error("用户名或密码错误");
        }
    }

    // ========== 用户查询接口 ==========

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

    @GetMapping("/teachers")
    public Result<List<User>> getAllTeachers() {
        List<User> teachers = userService.getAllTeachers();
        teachers.forEach(t -> t.setPassword(null));
        return Result.success(teachers);
    }

    @GetMapping("/all")
    public Result<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        users.forEach(u -> u.setPassword(null));
        return Result.success(users);
    }

    // ========== 用户管理接口 ==========

    @PutMapping("/update")
    public Result<String> updateUser(@RequestBody User user) {
        boolean success = userService.updateUser(user);
        if (success) {
            return Result.success("用户信息更新成功");
        } else {
            return Result.error("用户信息更新失败");
        }
    }

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