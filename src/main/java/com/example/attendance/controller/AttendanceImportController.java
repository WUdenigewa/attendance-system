package com.example.attendance.controller;

import com.example.attendance.entity.Attendance;
import com.example.attendance.entity.ImportResult;
import com.example.attendance.entity.User;
import com.example.attendance.repository.AttendanceRepository;
import com.example.attendance.repository.CourseRepository;
import com.example.attendance.service.UserService;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Controller
@RequestMapping("/attendance")
public class AttendanceImportController {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserService userService;

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userService.getUserByUsername(username);
    }

    @GetMapping("/import")
    public String importPage() {
        return "attendance-import";
    }

    @PostMapping("/import")
    public String importFile(@RequestParam("file") MultipartFile file,
                             RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "请选择文件");
            return "redirect:/attendance/import";
        }

        String filename = file.getOriginalFilename();
        if (filename == null || (!filename.endsWith(".xlsx") && !filename.endsWith(".xls"))) {
            redirectAttributes.addFlashAttribute("error", "文件格式不正确，请上传 .xlsx 或 .xls 文件");
            return "redirect:/attendance/import";
        }

        if (file.getSize() > 10 * 1024 * 1024) {
            redirectAttributes.addFlashAttribute("error", "文件大小不能超过10MB");
            return "redirect:/attendance/import";
        }

        try {
            ImportResult result = importFromExcel(file);
            redirectAttributes.addFlashAttribute("result", result);
            redirectAttributes.addFlashAttribute("success",
                    "导入完成！成功：" + result.getSuccessCount() + "条，失败：" + result.getFailCount() + "条");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "导入失败：" + e.getMessage());
        }

        return "redirect:/attendance/import";
    }

    private ImportResult importFromExcel(MultipartFile file) throws IOException {
        ImportResult result = new ImportResult();

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }

                try {
                    String studentId = getCellValue(row.getCell(0));
                    String courseName = getCellValue(row.getCell(1));
                    String checkInTimeStr = getCellValue(row.getCell(2));
                    String status = getCellValue(row.getCell(3));
                    String remark = getCellValue(row.getCell(4));

                    if (studentId.isEmpty()) {
                        result.incrementFail();
                        result.addError("第" + (i + 1) + "行：学号不能为空");
                        continue;
                    }

                    if (courseName.isEmpty()) {
                        result.incrementFail();
                        result.addError("第" + (i + 1) + "行：课程名称不能为空");
                        continue;
                    }

                    User student = userService.getUserByStudentId(studentId);
                    if (student == null) {
                        result.incrementFail();
                        result.addError("第" + (i + 1) + "行：学号 " + studentId + " 不存在");
                        continue;
                    }

                    Long courseId = courseRepository.findIdByCourseName(courseName);
                    if (courseId == null) {
                        result.incrementFail();
                        result.addError("第" + (i + 1) + "行：课程 " + courseName + " 不存在");
                        continue;
                    }

                    LocalDateTime checkInTime;
                    try {
                        checkInTime = LocalDateTime.parse(checkInTimeStr,
                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    } catch (DateTimeParseException e) {
                        checkInTime = LocalDateTime.now();
                        result.addError("第" + (i + 1) + "行：时间格式错误，已使用当前时间");
                    }

                    if (!status.isEmpty() && !status.equals("NORMAL") &&
                            !status.equals("LATE") && !status.equals("LEAVE") && !status.equals("ABSENT")) {
                        status = "NORMAL";
                        result.addError("第" + (i + 1) + "行：状态格式错误，已设置为正常");
                    }
                    if (status.isEmpty()) {
                        status = "NORMAL";
                    }

                    Attendance attendance = new Attendance();
                    attendance.setStudentId(studentId);
                    attendance.setStudentName(student.getRealName());
                    attendance.setCourseId(courseId);
                    attendance.setCourseName(courseName);
                    attendance.setCheckInTime(checkInTime);
                    attendance.setAttendanceDate(checkInTime.toLocalDate());
                    attendance.setStatus(status);
                    attendance.setRemark(remark);
                    attendance.setCreateTime(LocalDateTime.now());

                    attendanceRepository.save(attendance);
                    result.incrementSuccess();

                } catch (Exception e) {
                    result.incrementFail();
                    result.addError("第" + (i + 1) + "行：" + e.getMessage());
                }
            }
        }

        return result;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
}