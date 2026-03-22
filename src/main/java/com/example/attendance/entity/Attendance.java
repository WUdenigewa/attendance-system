package com.example.attendance.entity;

import java.time.LocalDateTime;

public class Attendance {
    private String studentId;      // 学号
    private String studentName;    // 学生姓名
    private String status;         // 考勤状态：正常/迟到/早退/旷课/请假
    private String courseName;     // 课程名称
    private LocalDateTime checkInTime;  // 签到时间
    private String remark;         // 备注

    // 无参构造函数
    public Attendance() {}

    // 有参构造函数
    public Attendance(String studentId, String studentName, String status,
                      String courseName, LocalDateTime checkInTime, String remark) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.status = status;
        this.courseName = courseName;
        this.checkInTime = checkInTime;
        this.remark = remark;
    }

    // Getter 和 Setter 方法
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public LocalDateTime getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(LocalDateTime checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}