package com.example.attendance.entity;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_code", length = 20)
    private String courseCode;

    @Column(name = "course_name", length = 100)
    private String courseName;

    @Column(name = "class_name", length = 50)
    private String className;

    @Column(name = "teacher_id")
    private Long teacherId;

    @Column(name = "teacher_name", length = 50)
    private String teacherName;

    @Column(name = "class_start_time")
    private LocalTime classStartTime;

    @Column(name = "class_end_time")
    private LocalTime classEndTime;

    @Column(name = "credit")
    private Integer credit;

    @Column(name = "total_hours")
    private Integer totalHours;

    @Column(name = "classroom", length = 50)
    private String classroom;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "semester", length = 20)
    private String semester;

    @Column(name = "status")
    private Integer status;

    // 构造函数
    public Course() {}

    // Getter 和 Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public Long getTeacherId() { return teacherId; }
    public void setTeacherId(Long teacherId) { this.teacherId = teacherId; }

    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }

    public LocalTime getClassStartTime() { return classStartTime; }
    public void setClassStartTime(LocalTime classStartTime) { this.classStartTime = classStartTime; }

    public LocalTime getClassEndTime() { return classEndTime; }
    public void setClassEndTime(LocalTime classEndTime) { this.classEndTime = classEndTime; }

    public Integer getCredit() { return credit; }
    public void setCredit(Integer credit) { this.credit = credit; }

    public Integer getTotalHours() { return totalHours; }
    public void setTotalHours(Integer totalHours) { this.totalHours = totalHours; }

    public String getClassroom() { return classroom; }
    public void setClassroom(String classroom) { this.classroom = classroom; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
}