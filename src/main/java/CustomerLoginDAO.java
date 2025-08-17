package com.system.dao;

import com.system.model.CustomerLoginModel;
import com.system.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CustomerLoginDAO {

    public boolean validate(CustomerLoginModel customer) {
        boolean isValid = false;

        String sql = "SELECT * FROM customer WHERE account_number = ? AND password = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customer.getAccountNumber());
            stmt.setString(2, customer.getPassword());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    isValid = true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return isValid;
    }
}
