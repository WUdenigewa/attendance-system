package com.example.attendance.controller;

import com.example.attendance.common.Result;
import com.example.attendance.entity.Student;
import com.example.attendance.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class StudentController {

    @Autowired
    private StudentService studentService;  // 注入 Service 层

    // ========== 学生管理接口 ==========

    /**
     * 新增学生
     * POST /student/add
     */
    @PostMapping("/student/add")
    public Result<String> addStudent(@RequestBody Student student) {
        boolean success = studentService.addStudent(student);
        if (success) {
            return Result.success("学生添加成功：" + student.getName());
        } else {
            return Result.error("学生添加失败，请检查学号格式或是否重复");
        }
    }

    /**
     * 根据学号查询学生
     * GET /student/info/{studentId}
     */
    @GetMapping("/student/info/{studentId}")
    public Result<Student> getStudentInfo(@PathVariable String studentId) {
        Student student = studentService.getStudentByStudentId(studentId);
        if (student != null) {
            return Result.success(student);
        } else {
            return Result.error("学号为 " + studentId + " 的学生不存在");
        }
    }

    /**
     * 查询所有学生
     * GET /student/list/all
     */
    @GetMapping("/student/list/all")
    public Result<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return Result.success(students);
    }

    /**
     * 按班级查询学生
     * GET /student/list/byClass?className=软件1班
     */
    @GetMapping("/student/list/byClass")
    public Result<List<Student>> getStudentsByClass(@RequestParam String className) {
        List<Student> students = studentService.getStudentsByClassName(className);
        return Result.success(students);
    }

    /**
     * 学生列表查询接口（带分页和班级过滤）
     * GET /student/list?className=软件1班&page=1
     */
    @GetMapping("/student/list")
    public Result<List<Student>> getStudentList(
            @RequestParam(required = false) String className,
            @RequestParam(defaultValue = "1") int page) {

        List<Student> allStudents;
        if (className != null && !className.isEmpty()) {
            allStudents = studentService.getStudentsByClassName(className);
        } else {
            allStudents = studentService.getAllStudents();
        }

        // 分页处理
        int pageSize = 3;
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, allStudents.size());

        if (start >= allStudents.size()) {
            return Result.success(List.of());
        }

        List<Student> pageResult = allStudents.subList(start, end);
        return Result.success(pageResult);
    }

    /**
     * 更新学生信息
     * PUT /student/update
     */
    @PutMapping("/student/update")
    public Result<String> updateStudent(@RequestBody Student student) {
        boolean success = studentService.updateStudent(student);
        if (success) {
            return Result.success("学生信息更新成功：" + student.getName());
        } else {
            return Result.error("学生信息更新失败，请检查学号是否存在");
        }
    }

    /**
     * 删除学生
     * DELETE /student/delete/{studentId}
     */
    @DeleteMapping("/student/delete/{studentId}")
    public Result<String> deleteStudent(@PathVariable String studentId) {
        boolean success = studentService.deleteStudent(studentId);
        if (success) {
            return Result.success("学生删除成功：" + studentId);
        } else {
            return Result.error("学生删除失败，请检查学号是否存在");
        }
    }

    /**
     * 考勤记录更新接口（JSON体参数）
     * POST /attendance/update
     */
    @PostMapping("/attendance/update")
    public Result<String> updateAttendance(@RequestBody Map<String, String> attendance) {
        System.out.println("收到考勤更新请求：" + attendance);
        String studentId = attendance.get("studentId");
        String status = attendance.get("status");

        if (studentId == null || status == null) {
            return Result.error("学号和考勤状态不能为空");
        }

        // 验证学生是否存在
        Student student = studentService.getStudentByStudentId(studentId);
        if (student == null) {
            return Result.error("学号为 " + studentId + " 的学生不存在");
        }

        String message = String.format("学生 %s(%s) 的考勤记录更新成功，状态：%s",
                student.getName(), studentId, status);
        return Result.success(message);
    }

    /**
     * 之前的/about接口
     */
    @GetMapping("/about")
    public Map<String, String> getAboutInfo() {
        Map<String, String> info = new HashMap<>();
        info.put("name", "吾德尼格瓦");
        info.put("major", "软件工程");
        info.put("studentId", "20240001");
        return info;
    }
}