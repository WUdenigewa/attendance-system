package com.example.attendance.service;

import com.example.attendance.entity.Attendance;
import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {

    /**
     * 添加考勤记录
     */
    boolean addAttendance(Attendance attendance);

    /**
     * 根据ID查询考勤记录
     */
    Attendance getAttendanceById(Long id);

    /**
     * 根据学生学号查询考勤记录
     */
    List<Attendance> getAttendanceByStudentId(String studentId);

    /**
     * 根据课程ID查询考勤记录
     */
    List<Attendance> getAttendanceByCourseId(Long courseId);

    /**
     * 根据日期查询考勤记录
     */
    List<Attendance> getAttendanceByDate(LocalDate date);

    /**
     * 查询学生某课程的所有考勤
     */
    List<Attendance> getStudentCourseAttendance(String studentId, Long courseId);

    /**
     * 统计某课程某日期的考勤人数
     */
    int countAttendanceByCourseAndDate(Long courseId, LocalDate date);

    /**
     * 统计学生迟到次数
     */
    int countStudentLate(String studentId, Long courseId);

    /**
     * 更新考勤记录
     */
    boolean updateAttendance(Attendance attendance);

    /**
     * 删除考勤记录
     */
    boolean deleteAttendance(Long id);

    /**
     * 签到（自动记录时间）
     */
    boolean checkIn(String studentId, Long courseId, String status);
}