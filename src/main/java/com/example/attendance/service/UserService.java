package com.example.attendance.service;

import com.example.attendance.entity.User;
import java.util.List;

public interface UserService {

    // ========== 增删改查方法 ==========

    /**
     * 新增用户
     * @param user 用户对象
     * @return 是否成功
     */
    boolean addUser(User user);

    /**
     * 根据ID查询用户
     * @param id 用户ID
     * @return 用户对象
     */
    User getUserById(Long id);

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户对象
     */
    User getUserByUsername(String username);

    /**
     * 查询所有教师
     * @return 教师列表
     */
    List<User> getAllTeachers();

    /**
     * 查询所有用户
     * @return 用户列表
     */
    List<User> getAllUsers();

    /**
     * 更新用户信息
     * @param user 用户对象
     * @return 是否成功
     */
    boolean updateUser(User user);

    /**
     * 删除用户
     * @param id 用户ID
     * @return 是否成功
     */
    boolean deleteUser(Long id);

    // ========== 认证相关方法 ==========

    /**
     * 用户登录验证
     * @param username 用户名
     * @param password 密码
     * @return 登录成功返回用户信息，失败返回null
     */
    User login(String username, String password);

    /**
     * 用户注册（带密码加密）
     * @param user 用户对象
     * @return 是否成功
     */
    boolean register(User user);

    /**
     * 检查用户名是否存在
     * @param username 用户名
     * @return 是否存在
     */
    boolean existsByUsername(String username);
}