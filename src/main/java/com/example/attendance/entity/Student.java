package com.example.attendance.entity;

public class Student {
    private String studentId;
    private String name;
    private String major;
    private String className;
    private String phone;
    private String email;

    // 无参构造函数
    public Student() {}

    // 简化构造函数（只传学号和姓名）
    public Student(String studentId, String name) {
        this.studentId = studentId;
        this.name = name;
        this.major = "待补充";
        this.className = "待补充";
        this.phone = "待补充";
        this.email = "待补充";
    }

    // 完整构造函数
    public Student(String studentId, String name, String major, String className, String phone, String email) {
        this.studentId = studentId;
        this.name = name;
        this.major = major;
        this.className = className;
        this.phone = phone;
        this.email = email;
    }

    // Getter 和 Setter 方法
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}