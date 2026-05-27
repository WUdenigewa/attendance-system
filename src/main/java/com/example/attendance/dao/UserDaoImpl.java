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
public class UserDaoImpl implements UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int insert(User user) {
        String sql = "INSERT INTO user (username, password, role, real_name, student_id, teacher_no, " +
                "email, phone, gender, department, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                user.getUsername(),
                user.getPassword(),
                user.getRole(),
                user.getRealName(),
                user.getStudentId(),
                user.getTeacherNo(),
                user.getEmail(),
                user.getPhone(),
                user.getGender(),
                user.getDepartment(),
                user.getStatus() != null ? user.getStatus() : 1
        );
    }

    @Override
    public User findById(Long id) {
        String sql = "SELECT id, username, password, role, real_name, student_id, teacher_no, " +
                "email, phone, gender, birthday, department, status, last_login_time, avatar " +
                "FROM user WHERE id = ?";
        List<User> list = jdbcTemplate.query(sql, new UserRowMapper(), id);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public User findByUsername(String username) {
        String sql = "SELECT id, username, password, role, real_name, student_id, teacher_no, " +
                "email, phone, gender, birthday, department, status, last_login_time, avatar " +
                "FROM user WHERE username = ?";
        List<User> list = jdbcTemplate.query(sql, new UserRowMapper(), username);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public User findByStudentId(String studentId) {
        String sql = "SELECT id, username, password, role, real_name, student_id, teacher_no, " +
                "email, phone, gender, birthday, department, status, last_login_time, avatar " +
                "FROM user WHERE student_id = ?";
        List<User> list = jdbcTemplate.query(sql, new UserRowMapper(), studentId);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT id, username, password, role, real_name, student_id, teacher_no, " +
                "email, phone, gender, birthday, department, status, last_login_time, avatar " +
                "FROM user";
        return jdbcTemplate.query(sql, new UserRowMapper());
    }

    @Override
    public List<User> findAllTeachers() {
        String sql = "SELECT id, username, password, role, real_name, student_id, teacher_no, " +
                "email, phone, gender, birthday, department, status, last_login_time, avatar " +
                "FROM user WHERE role = 'teacher'";
        return jdbcTemplate.query(sql, new UserRowMapper());
    }

    @Override
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

    @Override
    public int deleteById(Long id) {
        String sql = "DELETE FROM user WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existsByUsername(String username) {
        String sql = "SELECT COUNT(*) FROM user WHERE username = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return count != null && count > 0;
    }

    @Override
    public int updateLastLoginTime(Long id) {
        String sql = "UPDATE user SET last_login_time = NOW() WHERE id = ?";
        return jdbcTemplate.update(sql, id);
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