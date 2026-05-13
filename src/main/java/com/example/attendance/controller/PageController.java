package com.example.attendance.controller;

import com.example.attendance.entity.User;
import com.example.attendance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
     */
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    /**
     * 处理注册请求
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

        // 检查两次密码是否一致
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", true);
            model.addAttribute("errorMsg", "两次输入的密码不一致");
            return "register";
        }

        // 检查用户名是否已存在
        if (userService.existsByUsername(username)) {
            model.addAttribute("error", true);
            model.addAttribute("errorMsg", "用户名已存在");
            return "register";
        }

        // 创建用户对象
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

        // 保存用户（密码会在 Service 层加密）
        boolean success = userService.register(user);

        if (success) {
            return "redirect:/login?registered=true";
        } else {
            model.addAttribute("error", true);
            model.addAttribute("errorMsg", "注册失败，请稍后重试");
            return "register";
        }
    }

    /**
     * 首页（仪表盘）
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // 获取当前登录用户
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User user = userService.getUserByUsername(username);
        if (user != null) {
            user.setPassword(null);
            model.addAttribute("user", user);
        }

        return "dashboard";
    }

    /**
     * 退出登录
     */
    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login";
    }
}