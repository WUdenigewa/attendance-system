package com.example.attendance.controller;

import com.example.attendance.entity.User;
import com.example.attendance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {

    @Autowired
    private UserService userService;

    /**
     * 登录页面
     * GET /login
     */
    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error, Model model) {
        model.addAttribute("title", "用户登录");
        if (error != null) {
            model.addAttribute("error", true);
            model.addAttribute("errorMsg", "用户名或密码错误");
        }
        return "login";
    }

    /**
     * 注册页面
     * GET /register
     */
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    /**
     * 处理注册请求
     * POST /register
     */
    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String confirmPassword,
                           @RequestParam String realName,
                           @RequestParam String role,
                           @RequestParam(required = false) String studentId,
                           @RequestParam(required = false) String email,
                           @RequestParam(required = false) String phone,
                           Model model) {

        // 1. 检查两次密码是否一致
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", true);
            model.addAttribute("errorMsg", "两次输入的密码不一致");
            model.addAttribute("username", username);
            model.addAttribute("realName", realName);
            model.addAttribute("studentId", studentId);
            model.addAttribute("email", email);
            model.addAttribute("phone", phone);
            return "register";
        }

        // 2. 检查用户名是否已存在
        if (userService.existsByUsername(username)) {
            model.addAttribute("error", true);
            model.addAttribute("errorMsg", "用户名已存在");
            model.addAttribute("username", username);
            model.addAttribute("realName", realName);
            model.addAttribute("studentId", studentId);
            model.addAttribute("email", email);
            model.addAttribute("phone", phone);
            return "register";
        }

        // 3. 创建用户对象
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);
        user.setRealName(realName);
        if ("student".equals(role)) {
            user.setStudentId(studentId);
        }
        user.setEmail(email);
        user.setPhone(phone);
        user.setStatus(1);

        // 4. 保存用户
        boolean success = userService.register(user);

        if (success) {
            // 注册成功，跳转到登录页
            return "redirect:/login?registered=true";
        } else {
            model.addAttribute("error", true);
            model.addAttribute("errorMsg", "注册失败，请稍后重试");
            model.addAttribute("username", username);
            model.addAttribute("realName", realName);
            model.addAttribute("studentId", studentId);
            model.addAttribute("email", email);
            model.addAttribute("phone", phone);
            return "register";
        }
    }

    /**
     * 处理登录请求
     * POST /login
     */
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        Model model) {

        User user = userService.login(username, password);

        if (user != null) {
            // 登录成功，跳转到首页
            return "redirect:/dashboard";
        } else {
            // 登录失败，返回登录页并显示错误
            model.addAttribute("error", true);
            model.addAttribute("errorMsg", "用户名或密码错误");
            return "login";
        }
    }

    /**
     * 首页（仪表盘）
     * GET /dashboard
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // TODO: 从 SecurityContext 获取当前登录用户
        // 临时方案：用固定用户测试
        User user = userService.getUserByUsername("test01");
        if (user != null) {
            user.setPassword(null);
            model.addAttribute("user", user);
        }
        return "dashboard";
    }

    /**
     * 退出登录
     * GET /logout
     */
    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login";
    }
}