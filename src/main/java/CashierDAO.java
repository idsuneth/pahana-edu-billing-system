package com.system.dao;

import com.system.model.Cashier;
import com.system.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CashierDAO {

    public boolean addCashier(Cashier cashier) throws SQLException {
        String sql = "INSERT INTO cashier (name, address, nic, phone_number, username, password) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cashier.getName());
            ps.setString(2, cashier.getAddress());
            ps.setString(3, cashier.getNic());
            ps.setString(4, cashier.getPhoneNumber());
            ps.setString(5, cashier.getUsername());
            ps.setString(6, cashier.getPassword());

            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }
}

