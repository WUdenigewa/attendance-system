package com.example.attendance.entity;

/**
 * 用户注册请求参数类
 * 用于接收前端注册时提交的数据
 */
public class RegisterRequest {

    // 账号信息
    private String username;    // 用户名
    private String password;    // 密码

    // 角色信息
    private String role;        // 角色：admin/teacher/student

    // 基本信息
    private String realName;    // 真实姓名
    private String email;       // 邮箱
    private String phone;       // 电话
    private String gender;      // 性别
    private String department;  // 院系

    // 学生专用字段
    private String studentId;   // 学号

    // 教师专用字段
    private String teacherNo;   // 教师编号

    // ========== 无参构造函数 ==========
    public RegisterRequest() {
    }

    // ========== 有参构造函数（可选） ==========
    public RegisterRequest(String username, String password, String role, String realName) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.realName = realName;
    }

    // ========== Getter 和 Setter ==========
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getTeacherNo() {
        return teacherNo;
    }

    public void setTeacherNo(String teacherNo) {
        this.teacherNo = teacherNo;
    }

    // ========== toString 方法 ==========
    @Override
    public String toString() {
        return "RegisterRequest{" +
                "username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", realName='" + realName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}