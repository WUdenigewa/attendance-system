package com.example.attendance.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 批量导入结果类
 * 用于记录Excel导入的成功数、失败数和错误详情
 */
public class ImportResult {

    private int successCount = 0;      // 成功导入数量
    private int failCount = 0;          // 失败数量
    private List<String> errors = new ArrayList<>();  // 错误详情列表

    /**
     * 成功数+1
     */
    public void incrementSuccess() {
        successCount++;
    }

    /**
     * 失败数+1
     */
    public void incrementFail() {
        failCount++;
    }

    /**
     * 添加错误信息
     * @param error 错误描述
     */
    public void addError(String error) {
        errors.add(error);
    }

    // ========== Getter 和 Setter ==========

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public int getFailCount() {
        return failCount;
    }

    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    /**
     * 获取总计处理数量
     */
    public int getTotalCount() {
        return successCount + failCount;
    }
}