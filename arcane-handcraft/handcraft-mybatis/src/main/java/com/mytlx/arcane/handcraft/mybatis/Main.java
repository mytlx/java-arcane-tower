package com.mytlx.arcane.handcraft.mybatis;

import com.mytlx.arcane.utils.json.gson.GsonUtils;

import java.sql.*;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-08-20 13:42:56
 */
public class Main {

    public static void main(String[] args) {
        // System.out.println(jdbcSelectById(1958039467822551040L));

        HCSqlSessionFactory factory = new HCSqlSessionFactory();
        UserMapper mapper = factory.getMapper(UserMapper.class);
        User user = mapper.selectById(1958039467822551040L);
        System.out.println("user = " + GsonUtils.toJson(user));


    }

    // 1958039467822551040
    private static User jdbcSelectById(long id) {

        String jdbcUrl = "jdbc:mysql://localhost:3306/java-arcane-tower?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
        String username = "root";
        String password = "root";

        String sql = "SELECT * FROM t_handcraft_mybatis_user WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement ps = conn.prepareStatement(sql);) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setName(rs.getString("name"));
                user.setAge(rs.getInt("age"));
                return user;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
