package com.system.dao;

import com.system.model.CustomerModel;
import com.system.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CustomerDAO {

    public boolean addCustomer(CustomerModel customer) throws SQLException {
        String sql = "INSERT INTO customer (account_number, username, password, name, nic, address, phone_number) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setString(1, customer.getAccountNumber());
            ps.setString(2, customer.getUsername());
            ps.setString(3, customer.getPassword());
            ps.setString(4, customer.getName());
            ps.setString(5, customer.getNic());
            ps.setString(6, customer.getAddress());
            ps.setString(7, customer.getPhoneNumber());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    
}
