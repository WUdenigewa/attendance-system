package com.example.attendance.dao;

import com.example.attendance.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 1. 新增用户（教师）
     */
    public int insert(User user) {
        String sql = "INSERT INTO user (username, password, role, real_name, teacher_no, email, phone, gender, department, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                user.getUsername(),
                user.getPassword(),
                user.getRole(),
                user.getRealName(),
                user.getTeacherNo(),
                user.getEmail(),
                user.getPhone(),
                user.getGender(),
                user.getDepartment(),
                user.getStatus() != null ? user.getStatus() : 1
        );
    }

    /**
     * 2. 根据 ID 查询用户
     */
    public User findById(Long id) {
        String sql = "SELECT id, username, password, role, real_name, student_id, teacher_no, " +
                "email, phone, gender, birthday, department, status, last_login_time, avatar " +
                "FROM user WHERE id = ?";
        List<User> list = jdbcTemplate.query(sql, new UserRowMapper(), id);
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * 3. 根据用户名查询用户（用于登录验证）
     */
    public User findByUsername(String username) {
        String sql = "SELECT id, username, password, role, real_name, student_id, teacher_no, " +
                "email, phone, gender, birthday, department, status, last_login_time, avatar " +
                "FROM user WHERE username = ?";
        List<User> list = jdbcTemplate.query(sql, new UserRowMapper(), username);
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * 4. 查询所有教师
     */
    public List<User> findAllTeachers() {
        String sql = "SELECT id, username, password, role, real_name, student_id, teacher_no, " +
                "email, phone, gender, birthday, department, status, last_login_time, avatar " +
                "FROM user WHERE role = 'teacher'";
        return jdbcTemplate.query(sql, new UserRowMapper());
    }

    /**
     * 5. 更新用户信息
     */
    public int update(User user) {
        String sql = "UPDATE user SET username = ?, real_name = ?, email = ?, phone = ?, " +
                "gender = ?, department = ?, status = ? WHERE id = ?";
        return jdbcTemplate.update(sql,
                user.getUsername(),
                user.getRealName(),
                user.getEmail(),
                user.getPhone(),
                user.getGender(),
                user.getDepartment(),
                user.getStatus(),
                user.getId()
        );
    }

    /**
     * 6. 根据 ID 删除用户
     */
    public int deleteById(Long id) {
        String sql = "DELETE FROM user WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    /**
     * 7. 更新最后登录时间
     */
    public int updateLastLoginTime(Long id) {
        String sql = "UPDATE user SET last_login_time = NOW() WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    /**
     * 8. 查询所有用户
     */
    public List<User> findAll() {
        String sql = "SELECT id, username, password, role, real_name, student_id, teacher_no, " +
                "email, phone, gender, birthday, department, status, last_login_time, avatar " +
                "FROM user";
        return jdbcTemplate.query(sql, new UserRowMapper());
    }

    /**
     * 9. 检查用户名是否存在
     */
    public boolean existsByUsername(String username) {
        String sql = "SELECT COUNT(*) FROM user WHERE username = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return count != null && count > 0;
    }

    /**
     * 用户结果集映射
     */
    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setRole(rs.getString("role"));
            user.setRealName(rs.getString("real_name"));
            user.setStudentId(rs.getString("student_id"));
            user.setTeacherNo(rs.getString("teacher_no"));
            user.setEmail(rs.getString("email"));
            user.setPhone(rs.getString("phone"));
            user.setGender(rs.getString("gender"));
            if (rs.getDate("birthday") != null) {
                user.setBirthday(rs.getTimestamp("birthday").toLocalDateTime());
            }
            user.setDepartment(rs.getString("department"));
            user.setStatus(rs.getInt("status"));
            if (rs.getTimestamp("last_login_time") != null) {
                user.setLastLoginTime(rs.getTimestamp("last_login_time").toLocalDateTime());
            }
            user.setAvatar(rs.getString("avatar"));
            return user;
        }
    }
}