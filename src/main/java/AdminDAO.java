package com.system.dao;

import com.system.model.Admin;
import com.system.util.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminDAO {

    public boolean validate(Admin admin) {
        boolean status = false;
        String sql = "SELECT * FROM admin WHERE username = ? AND password = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, admin.getUsername());
            stmt.setString(2, admin.getPassword());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                status = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }
}