package com.example.attendance.service;

import com.example.attendance.entity.StatisticsDTO;
import com.example.attendance.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class AttendanceStatisticsService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    /**
     * 统计学生总考勤次数
     * @param studentId 学生学号
     * @return 总次数
     */
    public long countByStudentId(String studentId) {
        if (studentId == null || studentId.isEmpty()) {
            return 0;
        }
        return attendanceRepository.countByStudentId(studentId);
    }

    /**
     * 统计学生某状态的考勤次数
     * @param studentId 学生学号
     * @param status 状态（NORMAL正常/LATE迟到/ABSENT缺勤）
     * @return 次数
     */
    public long countByStudentIdAndStatus(String studentId, String status) {
        if (studentId == null || studentId.isEmpty()) {
            return 0;
        }
        return attendanceRepository.countByStudentIdAndStatus(studentId, status);
    }

    /**
     * 按日期范围统计考勤次数
     * @param studentId 学生学号
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 次数
     */
    public long countByStudentIdAndDateRange(String studentId, LocalDate startDate, LocalDate endDate) {
        if (studentId == null || studentId.isEmpty() || startDate == null || endDate == null) {
            return 0;
        }
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atStartOfDay().plusDays(1);
        return attendanceRepository.countByStudentIdAndCheckInTimeBetween(studentId, start, end);
    }

    /**
     * 获取学生完整统计信息
     * @param studentId 学生学号
     * @return 统计结果
     */
    public StatisticsDTO getStudentStatistics(String studentId) {
        if (studentId == null || studentId.isEmpty()) {
            return new StatisticsDTO(0, 0, 0, 0);
        }

        long totalCount = attendanceRepository.countByStudentId(studentId);
        long normalCount = attendanceRepository.countByStudentIdAndStatus(studentId, "NORMAL");
        long lateCount = attendanceRepository.countByStudentIdAndStatus(studentId, "LATE");
        long absentCount = attendanceRepository.countByStudentIdAndStatus(studentId, "ABSENT");

        return new StatisticsDTO(totalCount, normalCount, lateCount, absentCount);
    }

    /**
     * 按周统计出勤率
     * @param studentId 学生学号
     * @param year 年份
     * @param week 周数（1-52）
     * @return 统计结果
     */
    public StatisticsDTO getWeeklyStatistics(String studentId, int year, int week) {
        if (studentId == null || studentId.isEmpty()) {
            return new StatisticsDTO(0, 0, 0, 0);
        }

        // 获取该周的开始日期（周一）和结束日期（周日）
        LocalDate startDate = LocalDate.of(year, 1, 1)
                .with(DayOfWeek.MONDAY)
                .plusWeeks(week - 1);
        LocalDate endDate = startDate.plusDays(6);

        long totalCount = countByStudentIdAndDateRange(studentId, startDate, endDate);
        long normalCount = attendanceRepository.countByStudentIdAndStatusAndCheckInTimeBetween(
                studentId, "NORMAL", startDate.atStartOfDay(), endDate.atStartOfDay().plusDays(1));

        double rate = totalCount > 0 ? (double) normalCount / totalCount * 100 : 0;
        StatisticsDTO dto = new StatisticsDTO();
        dto.setTotalCount(totalCount);
        dto.setNormalCount(normalCount);
        dto.setAttendanceRate(rate);
        return dto;
    }

    /**
     * 按月统计出勤率
     * @param studentId 学生学号
     * @param year 年份
     * @param month 月份（1-12）
     * @return 统计结果
     */
    public StatisticsDTO getMonthlyStatistics(String studentId, int year, int month) {
        if (studentId == null || studentId.isEmpty()) {
            return new StatisticsDTO(0, 0, 0, 0);
        }

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        long totalCount = countByStudentIdAndDateRange(studentId, startDate, endDate);
        long normalCount = attendanceRepository.countByStudentIdAndStatusAndCheckInTimeBetween(
                studentId, "NORMAL", startDate.atStartOfDay(), endDate.atStartOfDay().plusDays(1));

        double rate = totalCount > 0 ? (double) normalCount / totalCount * 100 : 0;
        StatisticsDTO dto = new StatisticsDTO();
        dto.setTotalCount(totalCount);
        dto.setNormalCount(normalCount);
        dto.setAttendanceRate(rate);
        return dto;
    }

    /**
     * 获取本周统计
     * @param studentId 学生学号
     * @return 统计结果
     */
    public StatisticsDTO getCurrentWeekStatistics(String studentId) {
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.with(DayOfWeek.MONDAY);
        LocalDate endDate = now.with(DayOfWeek.SUNDAY);

        long totalCount = countByStudentIdAndDateRange(studentId, startDate, endDate);
        long normalCount = attendanceRepository.countByStudentIdAndStatusAndCheckInTimeBetween(
                studentId, "NORMAL", startDate.atStartOfDay(), endDate.atStartOfDay().plusDays(1));

        double rate = totalCount > 0 ? (double) normalCount / totalCount * 100 : 0;
        StatisticsDTO dto = new StatisticsDTO();
        dto.setTotalCount(totalCount);
        dto.setNormalCount(normalCount);
        dto.setAttendanceRate(rate);
        return dto;
    }

    /**
     * 获取本月统计
     * @param studentId 学生学号
     * @return 统计结果
     */
    public StatisticsDTO getCurrentMonthStatistics(String studentId) {
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.withDayOfMonth(1);
        LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());

        long totalCount = countByStudentIdAndDateRange(studentId, startDate, endDate);
        long normalCount = attendanceRepository.countByStudentIdAndStatusAndCheckInTimeBetween(
                studentId, "NORMAL", startDate.atStartOfDay(), endDate.atStartOfDay().plusDays(1));

        double rate = totalCount > 0 ? (double) normalCount / totalCount * 100 : 0;
        StatisticsDTO dto = new StatisticsDTO();
        dto.setTotalCount(totalCount);
        dto.setNormalCount(normalCount);
        dto.setAttendanceRate(rate);
        return dto;
    }
}