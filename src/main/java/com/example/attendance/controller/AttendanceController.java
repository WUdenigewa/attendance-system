package com.example.attendance.controller;

import com.example.attendance.common.Result;
import com.example.attendance.entity.Attendance;
import com.example.attendance.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    // ========== 基础 CRUD 接口 ==========

    @PostMapping("/add")
    public Result<String> addAttendance(@RequestBody Attendance attendance) {
        boolean success = attendanceService.addAttendance(attendance);
        if (success) {
            return Result.success("考勤记录添加成功");
        } else {
            return Result.error("考勤记录添加失败");
        }
    }

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

    @GetMapping("/{id}")
    public Result<Attendance> getAttendanceById(@PathVariable Long id) {
        Attendance attendance = attendanceService.getAttendanceById(id);
        if (attendance != null) {
            return Result.success(attendance);
        } else {
            return Result.error("考勤记录不存在");
        }
    }

    @GetMapping("/student/{studentId}")
    public Result<List<Attendance>> getAttendanceByStudentId(@PathVariable String studentId) {
        List<Attendance> list = attendanceService.getAttendanceByStudentId(studentId);
        return Result.success(list);
    }

    @GetMapping("/course/{courseId}")
    public Result<List<Attendance>> getAttendanceByCourseId(@PathVariable Long courseId) {
        List<Attendance> list = attendanceService.getAttendanceByCourseId(courseId);
        return Result.success(list);
    }

    @GetMapping("/date")
    public Result<List<Attendance>> getAttendanceByDate(@RequestParam(required = false) String date) {
        LocalDate queryDate = (date != null) ? LocalDate.parse(date) : LocalDate.now();
        List<Attendance> list = attendanceService.getAttendanceByDate(queryDate);
        return Result.success(list);
    }

    @GetMapping("/student/{studentId}/course/{courseId}")
    public Result<List<Attendance>> getStudentCourseAttendance(@PathVariable String studentId,
                                                               @PathVariable Long courseId) {
        List<Attendance> list = attendanceService.getStudentCourseAttendance(studentId, courseId);
        return Result.success(list);
    }

    @GetMapping("/count/course/{courseId}/date/{date}")
    public Result<Integer> countAttendanceByCourseAndDate(@PathVariable Long courseId,
                                                          @PathVariable String date) {
        LocalDate queryDate = LocalDate.parse(date);
        int count = attendanceService.countAttendanceByCourseAndDate(courseId, queryDate);
        return Result.success(count);
    }

    @GetMapping("/late/{studentId}/course/{courseId}")
    public Result<Integer> countStudentLate(@PathVariable String studentId,
                                            @PathVariable Long courseId) {
        int count = attendanceService.countStudentLate(studentId, courseId);
        return Result.success(count);
    }

    @PutMapping("/update")
    public Result<String> updateAttendance(@RequestBody Attendance attendance) {
        boolean success = attendanceService.updateAttendance(attendance);
        if (success) {
            return Result.success("考勤记录更新成功");
        } else {
            return Result.error("考勤记录更新失败");
        }
    }

    @DeleteMapping("/delete/{id}")
    public Result<String> deleteAttendance(@PathVariable Long id) {
        boolean success = attendanceService.deleteAttendance(id);
        if (success) {
            return Result.success("考勤记录删除成功");
        } else {
            return Result.error("考勤记录删除失败");
        }
    }

    // ========== 分页接口 ==========

    @GetMapping("/page")
    public Result<Page<Attendance>> getAttendancePage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) List<String> sort) {

        Pageable pageable;
        if (sort != null && !sort.isEmpty()) {
            Sort sortOrder = Sort.unsorted();
            for (String sortParam : sort) {
                String[] parts = sortParam.split(",");
                String field = parts[0];
                String direction = parts.length > 1 ? parts[1] : "asc";
                Sort.Direction dir = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
                sortOrder = sortOrder.and(Sort.by(dir, field));
            }
            pageable = PageRequest.of(page, size, sortOrder);
        } else {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "attendanceDate"));
        }

        Page<Attendance> attendancePage = attendanceService.getAttendancePage(pageable);
        return Result.success(attendancePage);
    }

    @GetMapping("/page/student/{studentId}")
    public Result<Page<Attendance>> getAttendancePageByStudentId(
            @PathVariable String studentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortField,
            @RequestParam(defaultValue = "desc") String direction) {

        Pageable pageable;
        if (sortField != null && !sortField.isEmpty()) {
            Sort.Direction dir = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            pageable = PageRequest.of(page, size, Sort.by(dir, sortField));
        } else {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "attendanceDate"));
        }

        Page<Attendance> attendancePage = attendanceService.getAttendancePageByStudentId(studentId, pageable);
        return Result.success(attendancePage);
    }

    @GetMapping("/page/course/{courseId}")
    public Result<Page<Attendance>> getAttendancePageByCourseId(
            @PathVariable Long courseId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortField,
            @RequestParam(defaultValue = "desc") String direction) {

        Pageable pageable;
        if (sortField != null && !sortField.isEmpty()) {
            Sort.Direction dir = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            pageable = PageRequest.of(page, size, Sort.by(dir, sortField));
        } else {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "attendanceDate"));
        }

        Page<Attendance> attendancePage = attendanceService.getAttendancePageByCourseId(courseId, pageable);
        return Result.success(attendancePage);
    }

    @GetMapping("/page/date")
    public Result<Page<Attendance>> getAttendancePageByDate(
            @RequestParam String date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortField,
            @RequestParam(defaultValue = "asc") String direction) {

        LocalDate queryDate = LocalDate.parse(date);

        Pageable pageable;
        if (sortField != null && !sortField.isEmpty()) {
            Sort.Direction dir = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            pageable = PageRequest.of(page, size, Sort.by(dir, sortField));
        } else {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "checkInTime"));
        }

        Page<Attendance> attendancePage = attendanceService.getAttendancePageByDate(queryDate, pageable);
        return Result.success(attendancePage);
    }

    @GetMapping("/page/range")
    public Result<Page<Attendance>> getAttendancePageByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortField,
            @RequestParam(defaultValue = "desc") String direction) {

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        Pageable pageable;
        if (sortField != null && !sortField.isEmpty()) {
            Sort.Direction dir = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            pageable = PageRequest.of(page, size, Sort.by(dir, sortField));
        } else {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "attendanceDate"));
        }

        Page<Attendance> attendancePage = attendanceService.getAttendancePageByDateRange(start, end, pageable);
        return Result.success(attendancePage);
    }

    // ========== 排序接口 ==========

    @GetMapping("/sorted")
    public Result<List<Attendance>> getAllAttendanceSorted(
            @RequestParam(defaultValue = "attendanceDate") String sortField,
            @RequestParam(defaultValue = "desc") String direction) {

        Sort.Direction dir = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(dir, sortField);
        List<Attendance> list = attendanceService.getAllAttendanceSorted(sort);
        return Result.success(list);
    }

    @GetMapping("/sorted/student/{studentId}")
    public Result<List<Attendance>> getStudentAttendanceSorted(
            @PathVariable String studentId,
            @RequestParam(defaultValue = "attendanceDate") String sortField,
            @RequestParam(defaultValue = "desc") String direction) {

        Sort.Direction dir = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(dir, sortField);
        List<Attendance> list = attendanceService.getStudentAttendanceSorted(studentId, sort);
        return Result.success(list);
    }

    @GetMapping("/sorted/course/{courseId}")
    public Result<List<Attendance>> getCourseAttendanceSorted(
            @PathVariable Long courseId,
            @RequestParam(defaultValue = "attendanceDate") String sortField,
            @RequestParam(defaultValue = "desc") String direction) {

        Sort.Direction dir = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(dir, sortField);
        List<Attendance> list = attendanceService.getCourseAttendanceSorted(courseId, sort);
        return Result.success(list);
    }

    @GetMapping("/sort/multi")
    public Result<List<Attendance>> getAttendanceWithMultiSort(
            @RequestParam List<String> sort) {
        List<Attendance> list = attendanceService.getAttendanceWithMultiSort(sort);
        return Result.success(list);
    }

    // ========== 多条件动态查询接口 ==========

    @GetMapping("/search")
    public Result<List<Attendance>> searchAttendance(
            @RequestParam(required = false) String studentId,
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {

        LocalDate start = (startDate != null) ? LocalDate.parse(startDate) : null;
        LocalDate end = (endDate != null) ? LocalDate.parse(endDate) : null;

        List<Attendance> list = attendanceService.searchAttendance(studentId, courseId, status, start, end);
        return Result.success(list);
    }

    @GetMapping("/search/page")
    public Result<Page<Attendance>> searchAttendancePage(
            @RequestParam(required = false) String studentId,
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortField,
            @RequestParam(defaultValue = "desc") String direction) {

        LocalDate start = (startDate != null) ? LocalDate.parse(startDate) : null;
        LocalDate end = (endDate != null) ? LocalDate.parse(endDate) : null;

        Pageable pageable;
        if (sortField != null && !sortField.isEmpty()) {
            Sort.Direction dir = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            pageable = PageRequest.of(page, size, Sort.by(dir, sortField));
        } else {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "attendanceDate"));
        }

        Page<Attendance> attendancePage = attendanceService.searchAttendancePage(
                studentId, courseId, status, start, end, pageable);
        return Result.success(attendancePage);
    }

    @GetMapping("/page/advanced")
    public Result<Page<Attendance>> advancedPageQuery(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) List<String> sort,
            @RequestParam(required = false) String studentId,
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {

        Pageable pageable;
        if (sort != null && !sort.isEmpty()) {
            Sort sortOrder = Sort.unsorted();
            for (String sortParam : sort) {
                String[] parts = sortParam.split(",");
                String field = parts[0];
                String direction = parts.length > 1 ? parts[1] : "asc";
                Sort.Direction dir = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
                sortOrder = sortOrder.and(Sort.by(dir, field));
            }
            pageable = PageRequest.of(page, size, sortOrder);
        } else {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "attendanceDate"));
        }

        LocalDate start = (startDate != null) ? LocalDate.parse(startDate) : null;
        LocalDate end = (endDate != null) ? LocalDate.parse(endDate) : null;

        Page<Attendance> attendancePage = attendanceService.searchAttendancePage(
                studentId, courseId, status, start, end, pageable);
        return Result.success(attendancePage);
    }
}