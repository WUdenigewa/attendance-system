package com.example.attendance.controller;

import com.example.attendance.common.Result;
import com.example.attendance.entity.Student;
import com.example.attendance.entity.Attendance;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

@RestController
public class StudentController {

    private static Map<String, Student> studentDB = new HashMap<>();

    static {
        studentDB.put("2024001", new Student("2024001", "张三", "软件工程", "软件1班", "13800138001", "zhangsan@example.com"));
        studentDB.put("2024002", new Student("2024002", "李四", "软件工程", "软件1班", "13800138002", "lisi@example.com"));
        studentDB.put("2024003", new Student("2024003", "王五", "软件工程", "软件2班", "13800138003", "wangwu@example.com"));
        studentDB.put("2024004", new Student("2024004", "赵六", "软件工程", "软件2班", "13800138004", "zhaoliu@example.com"));
        studentDB.put("2024005", new Student("2024005", "孙七", "软件工程", "软件1班", "13800138005", "sunqi@example.com"));
        studentDB.put("2024006", new Student("2024006", "周八", "软件工程", "软件2班", "13800138006", "zhouba@example.com"));
        studentDB.put("2024007", new Student("2024007", "吴九", "软件工程", "软件1班", "13800138007", "wujiu@example.com"));
        studentDB.put("2024008", new Student("2024008", "郑十", "软件工程", "软件2班", "13800138008", "zhengshi@example.com"));
    }

    // 任务一：学生信息查询接口（路径参数）
    @GetMapping("/student/info/{studentId}")
    public Result<Student> getStudentInfo(@PathVariable String studentId) {
        Student student = studentDB.get(studentId);
        if (student != null) {
            return Result.success(student);
        } else {
            return Result.error("学号为 " + studentId + " 的学生不存在");
        }
    }

    // 任务二：学生列表查询接口（查询参数）
    @RequestMapping("/student/list")
    public Result<List<Student>> getStudentList(
            @RequestParam(required = false) String className,
            @RequestParam(defaultValue = "1") int page
    ) {
        List<Student> allStudents = new ArrayList<>(studentDB.values());
        List<Student> filteredStudents = new ArrayList<>();

        if (className != null && !className.isEmpty()) {
            for (Student student : allStudents) {
                if (className.equals(student.getClassName())) {
                    filteredStudents.add(student);
                }
            }
        } else {
            filteredStudents = allStudents;
        }

        int pageSize = 3;
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, filteredStudents.size());

        if (start >= filteredStudents.size()) {
            return Result.success(new ArrayList<>());
        }

        List<Student> pageResult = filteredStudents.subList(start, end);
        return Result.success(pageResult);
    }

    // 任务三：考勤记录更新接口（JSON体参数）
    @PostMapping("/attendance/update")
    public Result<String> updateAttendance(@RequestBody Attendance attendance) {
        // 打印接收到的数据
        System.out.println("========== 收到考勤更新请求 ==========");
        System.out.println("学号：" + attendance.getStudentId());
        System.out.println("姓名：" + attendance.getStudentName());
        System.out.println("状态：" + attendance.getStatus());
        System.out.println("课程：" + attendance.getCourseName());
        System.out.println("签到时间：" + attendance.getCheckInTime());
        System.out.println("备注：" + attendance.getRemark());
        System.out.println("=====================================");

        // 验证必要字段
        if (attendance.getStudentId() == null || attendance.getStudentId().isEmpty()) {
            return Result.error("学号不能为空");
        }
        if (attendance.getStatus() == null || attendance.getStatus().isEmpty()) {
            return Result.error("考勤状态不能为空");
        }

        // 在实际项目中，这里会保存到数据库
        // 我们模拟保存成功

        String successMessage = String.format(
                "学生 %s(%s) 的考勤记录更新成功，状态：%s",
                attendance.getStudentName(),
                attendance.getStudentId(),
                attendance.getStatus()
        );

        return Result.success(successMessage);
    }

    // 之前的/about接口
    @GetMapping("/about")
    public Map<String, String> getAboutInfo() {
        Map<String, String> info = new HashMap<>();
        info.put("name", "张三");
        info.put("major", "软件工程");
        info.put("studentId", "2024001");
        return info;
    }
}