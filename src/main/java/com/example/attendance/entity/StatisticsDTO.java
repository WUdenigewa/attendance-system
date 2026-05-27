package com.example.attendance.entity;

/**
 * 考勤统计结果类
 * 用于展示学生的考勤统计数据
 */
public class StatisticsDTO {

    private long totalCount;      // 总考勤次数
    private long normalCount;     // 正常次数
    private long lateCount;       // 迟到次数
    private long absentCount;     // 缺勤次数
    private double attendanceRate; // 出勤率（百分比）

    /**
     * 无参构造函数
     */
    public StatisticsDTO() {
    }

    /**
     * 全参构造函数
     * @param totalCount 总次数
     * @param normalCount 正常次数
     * @param lateCount 迟到次数
     * @param absentCount 缺勤次数
     */
    public StatisticsDTO(long totalCount, long normalCount, long lateCount, long absentCount) {
        this.totalCount = totalCount;
        this.normalCount = normalCount;
        this.lateCount = lateCount;
        this.absentCount = absentCount;
        // 计算出勤率 = 正常次数 / 总次数 × 100
        this.attendanceRate = totalCount > 0 ? (double) normalCount / totalCount * 100 : 0;
    }

    // ========== Getter 和 Setter ==========

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