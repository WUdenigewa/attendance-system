package com.example.attendance.dao;

import com.example.attendance.entity.User;
import java.util.List;

public interface UserDao {

    // ========== 增删改查方法 ==========

    /**
     * 插入用户
     */
    int insert(User user);

    /**
     * 根据ID查询用户
     */
    User findById(Long id);

    /**
     * 根据用户名查询用户
     */
    User findByUsername(String username);

    /**
     * 根据学号查询用户
     */
    User findByStudentId(String studentId);

    /**
     * 查询所有用户
     */
    List<User> findAll();

    /**
     * 查询所有教师
     */
    List<User> findAllTeachers();

    /**
     * 更新用户信息
     */
    int update(User user);

    /**
     * 根据ID删除用户
     */
    int deleteById(Long id);

    /**
     * 检查用户名是否存在
     */
    boolean existsByUsername(String username);

    /**
     * 更新最后登录时间
     */
    int updateLastLoginTime(Long id);
}