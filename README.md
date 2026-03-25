# 考勤管理系统 (Attendance System)

## 基本信息

| 项目 | 内容          |
|------|-------------|
| 学号 | 42411042    |
| 姓名 | 吾德尼格瓦       |
| 专业 | 计算机科学与技术    |
| 班级 | 计科2班        |
| 课程 | Java EE开发实践 |

## 项目简介

基于 Spring Boot 开发的考勤管理系统，实现了学生信息管理和考勤记录功能。

## 技术栈

- **后端框架**: Spring Boot 3.1.5
- **开发语言**: Java 21
- **构建工具**: Maven
- **数据库**: MySQL (模拟数据)
- **版本控制**: Git

## 项目结构
```
attendance-system/
├── src/main/java/com/example/attendance/
│   ├── AttendanceSystemApplication.java
│   ├── controller/
│   │   └── StudentController.java
│   ├── service/
│   │   ├── StudentService.java
│   │   └── impl/
│   │       └── StudentServiceImpl.java
│   ├── dao/
│   │   └── StudentDao.java
│   ├── entity/
│   │   ├── Student.java
│   │   └── Attendance.java
│   └── common/
│       └── Result.java
├── src/main/resources/
│   └── application.properties
└── pom.xml
```

## 功能列表

| 功能 | 接口 | 方法 |
|------|------|------|
| 查询所有学生 | /student/list/all | GET |
| 按学号查询 | /student/info/{studentId} | GET |
| 按班级查询 | /student/list/byClass | GET |
| 分页查询 | /student/list | GET |
| 新增学生 | /student/add | POST |
| 更新学生 | /student/update | PUT |
| 删除学生 | /student/delete/{studentId} | DELETE |
| 个人信息 | /about | GET |

## 分层架构

- **Controller层**：接收请求，返回结果
- **Service层**：业务逻辑处理
- **Dao层**：数据访问操作