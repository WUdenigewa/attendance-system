-- ============================================
-- 考勤系统数据库脚本
-- 学号：42411042
-- 姓名：吾德尼格瓦
-- 日期：2026-04-01
-- ============================================

-- 1. 创建数据库
CREATE DATABASE IF NOT EXISTS attendance_system
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE attendance_system;

-- 2. 创建 user 表
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
                        username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
                        password VARCHAR(255) NOT NULL COMMENT '密码',
                        role VARCHAR(20) NOT NULL COMMENT '角色',
                        real_name VARCHAR(50) NOT NULL COMMENT '真实姓名',
                        student_id VARCHAR(20) UNIQUE COMMENT '学号',
                        teacher_no VARCHAR(20) UNIQUE COMMENT '教师编号',
                        email VARCHAR(100) COMMENT '邮箱',
                        phone VARCHAR(20) COMMENT '电话',
                        gender VARCHAR(10) COMMENT '性别',
                        birthday DATE COMMENT '生日',
                        department VARCHAR(100) COMMENT '院系',
                        status TINYINT DEFAULT 1 COMMENT '状态',
                        create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
                        update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 3. 创建 course 表
CREATE TABLE course (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        course_code VARCHAR(20) NOT NULL UNIQUE,
                        course_name VARCHAR(100) NOT NULL,
                        teacher_id BIGINT,
                        teacher_name VARCHAR(50),
                        credit INT DEFAULT 2,
                        total_hours INT DEFAULT 32,
                        classroom VARCHAR(50),
                        capacity INT DEFAULT 60,
                        semester VARCHAR(20)
);

-- 4. 创建 course_selection 表
CREATE TABLE course_selection (
                                  id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                  student_id VARCHAR(20) NOT NULL,
                                  student_name VARCHAR(50) NOT NULL,
                                  course_id BIGINT NOT NULL,
                                  course_name VARCHAR(100) NOT NULL,
                                  seat_number VARCHAR(20),
                                  seat_row INT,
                                  seat_col INT,
                                  status VARCHAR(20) DEFAULT 'selected',
                                  UNIQUE KEY uk_student_course (student_id, course_id)
);

-- 5. 创建 attendance 表
CREATE TABLE attendance (
                            id BIGINT PRIMARY KEY AUTO_INCREMENT,
                            student_id VARCHAR(20) NOT NULL,
                            student_name VARCHAR(50) NOT NULL,
                            course_id BIGINT NOT NULL,
                            course_name VARCHAR(100) NOT NULL,
                            attendance_date DATE NOT NULL,
                            status VARCHAR(20) NOT NULL,
                            check_in_time DATETIME,
                            seat_number VARCHAR(20),
                            seat_row INT,
                            seat_col INT,
                            remark VARCHAR(500)
);

-- 6. 插入用户数据
INSERT INTO `user` (username, password, role, real_name, email, phone, gender, department) VALUES
    ('admin', '123456', 'admin', '系统管理员', 'admin@attendance.com', '13800000000', '男', '信息学院');

INSERT INTO `user` (username, password, role, real_name, teacher_no, email, phone, gender, department) VALUES
                                                                                                           ('wanglaoshi', '123456', 'teacher', '王明', 'T2024001', 'wang@attendance.com', '13800000001', '男', '软件工程系'),
                                                                                                           ('lilaoshi', '123456', 'teacher', '李芳', 'T2024002', 'li@attendance.com', '13800000002', '女', '计算机科学系');

INSERT INTO `user` (username, password, role, real_name, student_id, email, phone, gender, department, birthday) VALUES
                                                                                                                     ('zhangsan', '123456', 'student', '张三', '20240001', 'zhangsan@example.com', '13800138001', '男', '软件工程系', '2004-05-15'),
                                                                                                                     ('lisi', '123456', 'student', '李四', '20240002', 'lisi@example.com', '13800138002', '女', '软件工程系', '2004-08-22'),
                                                                                                                     ('wangwu', '123456', 'student', '王五', '20240003', 'wangwu@example.com', '13800138003', '男', '计算机科学系', '2004-03-10');

-- 7. 插入课程数据
INSERT INTO course (course_code, course_name, teacher_id, teacher_name, credit, total_hours, classroom, capacity, semester) VALUES
                                                                                                                                ('CS101', '数据结构', 2, '王明', 3, 48, 'A101', 64, '2025春季'),
                                                                                                                                ('CS102', '数据库原理', 3, '李芳', 3, 48, 'A102', 64, '2025春季'),
                                                                                                                                ('CS103', 'Java编程', 2, '王明', 4, 64, '机房B', 60, '2025春季');

-- 8. 插入选课数据
INSERT INTO course_selection (student_id, student_name, course_id, course_name, seat_number, seat_row, seat_col) VALUES
                                                                                                                     ('20240001', '张三', 1, '数据结构', 'A-03', 1, 3),
                                                                                                                     ('20240001', '张三', 2, '数据库原理', 'B-05', 2, 5),
                                                                                                                     ('20240002', '李四', 1, '数据结构', 'A-05', 1, 5),
                                                                                                                     ('20240002', '李四', 3, 'Java编程', 'C-02', 3, 2),
                                                                                                                     ('20240003', '王五', 1, '数据结构', 'A-08', 2, 8),
                                                                                                                     ('20240003', '王五', 2, '数据库原理', 'B-02', 2, 2);

-- 9. 插入考勤数据
INSERT INTO attendance (student_id, student_name, course_id, course_name, attendance_date, status, check_in_time, seat_number, seat_row, seat_col, remark) VALUES
                                                                                                                                                               ('20240001', '张三', 1, '数据结构', CURDATE(), '正常', NOW(), 'A-03', 1, 3, '按时签到'),
                                                                                                                                                               ('20240002', '李四', 1, '数据结构', CURDATE(), '迟到', DATE_ADD(NOW(), INTERVAL 15 MINUTE), 'A-05', 1, 5, '迟到15分钟'),
                                                                                                                                                               ('20240003', '王五', 1, '数据结构', CURDATE(), '正常', NOW(), 'A-08', 2, 8, '按时签到'),
                                                                                                                                                               ('20240001', '张三', 2, '数据库原理', CURDATE(), '请假', NULL, 'B-05', 2, 5, '病假');

-- 10. 验证数据
SELECT '=== 用户表 ===' AS '';
SELECT id, username, real_name, role, student_id, teacher_no, email FROM `user`;

SELECT '=== 课程表 ===' AS '';
SELECT id, course_code, course_name, teacher_name FROM course;

SELECT '=== 选课表 ===' AS '';
SELECT student_id, student_name, course_name, seat_number FROM course_selection;

SELECT '=== 考勤表 ===' AS '';
SELECT student_name, course_name, attendance_date, status FROM attendance;