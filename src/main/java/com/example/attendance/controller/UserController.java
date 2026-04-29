package com.example.attendance.controller;

import com.example.attendance.common.Result;
import com.example.attendance.entity.LoginRequest;
import com.example.attendance.entity.RegisterRequest;
import com.example.attendance.entity.User;
import com.example.attendance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    // ========== 认证接口（公开） ==========

    /**
     * 用户注册
     * POST /api/user/register
     */
    @PostMapping("/register")
    public Result<String> register(@RequestBody RegisterRequest request) {
        // 1. 检查用户名是否已存在
        if (userService.existsByUsername(request.getUsername())) {
            return Result.error("用户名已存在");
        }

        // 2. 创建用户对象
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

        // 3. 保存用户（密码会在 Service 层加密）
        boolean success = userService.register(user);

        // 4. 返回结果
        if (success) {
            return Result.success("注册成功，用户名：" + request.getUsername());
        } else {
            return Result.error("注册失败，请稍后重试");
        }
    }

    /**
     * 用户登录（POST 方式，接收 JSON 体）
     * POST /api/user/login
     */
    @PostMapping("/login")
    public Result<User> login(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        // 1. 参数校验
        if (username == null || username.trim().isEmpty()) {
            return Result.error("用户名不能为空");
        }
        if (password == null || password.trim().isEmpty()) {
            return Result.error("密码不能为空");
        }

        // 2. 调用 Service 验证登录
        User user = userService.login(username, password);

        // 3. 返回结果
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

    // ========== 用户管理接口 ==========

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

    // ========== 权限测试接口（需要特定角色） ==========

    /**
     * 管理员专用接口
     * 只有 ADMIN 角色可以访问
     * GET /api/user/admin/only
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/only")
    public Result<String> adminOnly() {
        return Result.success("管理员权限验证通过，可以访问此接口");
    }

    /**
     * 教师专用接口
     * ADMIN 和 TEACHER 角色可以访问
     * GET /api/user/teacher/only
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @GetMapping("/teacher/only")
    public Result<String> teacherOnly() {
        return Result.success("教师权限验证通过，可以访问此接口");
    }

    /**
     * 学生专用接口
     * 所有角色都可以访问
     * GET /api/user/student/only
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    @GetMapping("/student/only")
    public Result<String> studentOnly() {
        return Result.success("学生权限验证通过，可以访问此接口");
    }

    /**
     * 获取当前登录用户信息
     * GET /api/user/me
     */
    @GetMapping("/me")
    public Result<User> getCurrentUser(@RequestParam String username) {
        User user = userService.getUserByUsername(username);
        if (user != null) {
            user.setPassword(null);
            return Result.success(user);
        } else {
            return Result.error("用户不存在");
        }
    }
}