package com.example.attendance.entity;

public class StatisticsDTO {

    private long totalCount;      // 总考勤次数
    private long normalCount;     // 正常次数
    private long lateCount;       // 迟到次数
    private long leaveCount;      // 请假次数
    private long absentCount;     // 缺勤次数
    private double attendanceRate; // 出勤率

    // 无参构造函数
    public StatisticsDTO() {
    }

    // 全参构造函数（5个参数）
    public StatisticsDTO(long totalCount, long normalCount, long lateCount, long leaveCount, long absentCount) {
        this.totalCount = totalCount;
        this.normalCount = normalCount;
        this.lateCount = lateCount;
        this.leaveCount = leaveCount;
        this.absentCount = absentCount;
        this.attendanceRate = totalCount > 0 ? (double) normalCount / totalCount * 100 : 0;
    }

    // 简化构造函数（4个参数，兼容旧代码）
    public StatisticsDTO(long totalCount, long normalCount, long lateCount, long absentCount) {
        this(totalCount, normalCount, lateCount, 0, absentCount);
    }

    // Getters and Setters
    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public long getNormalCount() {
        return normalCount;
    }

    public void setNormalCount(long normalCount) {
        this.normalCount = normalCount;
    }

    public long getLateCount() {
        return lateCount;
    }

    public void setLateCount(long lateCount) {
        this.lateCount = lateCount;
    }

    public long getLeaveCount() {
        return leaveCount;
    }

    public void setLeaveCount(long leaveCount) {
        this.leaveCount = leaveCount;
    }

    public long getAbsentCount() {
        return absentCount;
    }

    public void setAbsentCount(long absentCount) {
        this.absentCount = absentCount;
    }

    public double getAttendanceRate() {
        return attendanceRate;
    }

    public void setAttendanceRate(double attendanceRate) {
        this.attendanceRate = attendanceRate;
    }
}