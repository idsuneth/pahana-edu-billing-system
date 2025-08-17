package com.system.dao;

import com.system.model.CashierLogin;
import com.system.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CashierLoginDAO {
    public boolean checkLogin(CashierLogin cashier) {
        boolean isValid = false;

        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT * FROM cashier WHERE username=? AND password=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, cashier.getUsername());
            stmt.setString(2, cashier.getPassword());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                isValid = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return isValid;
    }
}
