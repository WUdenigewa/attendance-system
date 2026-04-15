package com.example.attendance.controller;

import com.example.attendance.common.Result;
import com.example.attendance.entity.Attendance;
import com.example.attendance.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    /**
     * 添加考勤记录
     * POST /api/attendance/add
     */
    @PostMapping("/add")
    public Result<String> addAttendance(@RequestBody Attendance attendance) {
        boolean success = attendanceService.addAttendance(attendance);
        if (success) {
            return Result.success("考勤记录添加成功");
        } else {
            return Result.error("考勤记录添加失败");
        }
    }

    /**
     * 签到（快速签到）
     * POST /api/attendance/checkin?studentId=xxx&courseId=1&status=正常
     */
    @PostMapping("/checkin")
    public Result<String> checkIn(@RequestParam String studentId,
                                  @RequestParam Long courseId,
                                  @RequestParam(defaultValue = "正常") String status) {
        boolean success = attendanceService.checkIn(studentId, courseId, status);
        if (success) {
            return Result.success("签到成功，状态：" + status);
        } else {
            return Result.error("签到失败，今日已签到或参数错误");
        }
    }

    /**
     * 根据ID查询考勤记录
     * GET /api/attendance/{id}
     */
    @GetMapping("/{id}")
    public Result<Attendance> getAttendanceById(@PathVariable Long id) {
        Attendance attendance = attendanceService.getAttendanceById(id);
        if (attendance != null) {
            return Result.success(attendance);
        } else {
            return Result.error("考勤记录不存在");
        }
    }

    /**
     * 根据学生学号查询考勤记录
     * GET /api/attendance/student/{studentId}
     */
    @GetMapping("/student/{studentId}")
    public Result<List<Attendance>> getAttendanceByStudentId(@PathVariable String studentId) {
        List<Attendance> list = attendanceService.getAttendanceByStudentId(studentId);
        return Result.success(list);
    }

    /**
     * 根据课程ID查询考勤记录
     * GET /api/attendance/course/{courseId}
     */
    @GetMapping("/course/{courseId}")
    public Result<List<Attendance>> getAttendanceByCourseId(@PathVariable Long courseId) {
        List<Attendance> list = attendanceService.getAttendanceByCourseId(courseId);
        return Result.success(list);
    }

    /**
     * 根据日期查询考勤记录
     * GET /api/attendance/date?date=2026-04-15
     */
    @GetMapping("/date")
    public Result<List<Attendance>> getAttendanceByDate(@RequestParam(required = false) String date) {
        LocalDate queryDate = (date != null) ? LocalDate.parse(date) : LocalDate.now();
        List<Attendance> list = attendanceService.getAttendanceByDate(queryDate);
        return Result.success(list);
    }

    /**
     * 查询学生某课程的所有考勤
     * GET /api/attendance/student/{studentId}/course/{courseId}
     */
    @GetMapping("/student/{studentId}/course/{courseId}")
    public Result<List<Attendance>> getStudentCourseAttendance(@PathVariable String studentId,
                                                               @PathVariable Long courseId) {
        List<Attendance> list = attendanceService.getStudentCourseAttendance(studentId, courseId);
        return Result.success(list);
    }

    /**
     * 统计某课程某日期的考勤人数
     * GET /api/attendance/count/course/{courseId}/date/{date}
     */
    @GetMapping("/count/course/{courseId}/date/{date}")
    public Result<Integer> countAttendanceByCourseAndDate(@PathVariable Long courseId,
                                                          @PathVariable String date) {
        LocalDate queryDate = LocalDate.parse(date);
        int count = attendanceService.countAttendanceByCourseAndDate(courseId, queryDate);
        return Result.success(count);
    }

    /**
     * 统计学生迟到次数
     * GET /api/attendance/late/{studentId}/course/{courseId}
     */
    @GetMapping("/late/{studentId}/course/{courseId}")
    public Result<Integer> countStudentLate(@PathVariable String studentId,
                                            @PathVariable Long courseId) {
        int count = attendanceService.countStudentLate(studentId, courseId);
        return Result.success(count);
    }

    /**
     * 更新考勤记录
     * PUT /api/attendance/update
     */
    @PutMapping("/update")
    public Result<String> updateAttendance(@RequestBody Attendance attendance) {
        boolean success = attendanceService.updateAttendance(attendance);
        if (success) {
            return Result.success("考勤记录更新成功");
        } else {
            return Result.error("考勤记录更新失败");
        }
    }

    /**
     * 删除考勤记录
     * DELETE /api/attendance/delete/{id}
     */
    @DeleteMapping("/delete/{id}")
    public Result<String> deleteAttendance(@PathVariable Long id) {
        boolean success = attendanceService.deleteAttendance(id);
        if (success) {
            return Result.success("考勤记录删除成功");
        } else {
            return Result.error("考勤记录删除失败");
        }
    }
}