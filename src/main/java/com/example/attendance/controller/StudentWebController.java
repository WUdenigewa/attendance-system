package com.example.attendance.controller;

import com.example.attendance.entity.Student;
import com.example.attendance.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/student-web")
public class StudentWebController {

    @Autowired
    private StudentService studentService;

    /**
     * 学生列表页面（带分页和搜索）
     * GET /student/list
     */
    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(required = false) String keyword,
                       Model model) {

        // 分页参数（JPA 从0开始，所以 page-1）
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Student> studentPage;

        if (keyword != null && !keyword.isEmpty()) {
            // 有关键词则搜索
            studentPage = studentService.searchByKeyword(keyword, pageable);
            model.addAttribute("keyword", keyword);
        } else {
            studentPage = studentService.getAllStudentsPage(pageable);
        }

        model.addAttribute("students", studentPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", studentPage.getTotalPages());
        model.addAttribute("totalElements", studentPage.getTotalElements());
        model.addAttribute("pageSize", size);

        return "student-list";
    }

    /**
     * 新增学生页面
     * GET /student/add
     */
    @GetMapping("/add")
    public String addPage(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("title", "新增学生");
        return "student-form";
    }

    /**
     * 编辑学生页面
     * GET /student/edit/{id}
     */
    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable Long id, Model model) {
        Student student = studentService.getStudentById(id);
        if (student == null) {
            return "redirect:/student/list";
        }
        model.addAttribute("student", student);
        model.addAttribute("title", "编辑学生");
        return "student-form";
    }

    /**
     * 保存学生（新增或更新）
     * POST /student/save
     */
    @PostMapping("/save")
    public String save(@ModelAttribute Student student, RedirectAttributes redirectAttributes) {
        try {
            if (student.getId() == null) {
                // 新增
                boolean success = studentService.addStudent(student);
                if (success) {
                    redirectAttributes.addFlashAttribute("successMsg", "学生添加成功！");
                } else {
                    redirectAttributes.addFlashAttribute("errorMsg", "学生添加失败，学号可能已存在");
                }
            } else {
                // 更新
                boolean success = studentService.updateStudent(student);
                if (success) {
                    redirectAttributes.addFlashAttribute("successMsg", "学生信息更新成功！");
                } else {
                    redirectAttributes.addFlashAttribute("errorMsg", "学生信息更新失败");
                }
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "操作失败：" + e.getMessage());
        }
        return "redirect:/student/list";
    }

    /**
     * 删除学生
     * GET /student/delete/{id}
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            boolean success = studentService.deleteStudentById(id);
            if (success) {
                redirectAttributes.addFlashAttribute("successMsg", "学生删除成功！");
            } else {
                redirectAttributes.addFlashAttribute("errorMsg", "学生删除失败");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "删除失败：" + e.getMessage());
        }
        return "redirect:/student/list";
    }
}