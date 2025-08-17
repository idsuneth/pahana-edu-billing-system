package com.system.dao;

import com.system.model.Item;
import com.system.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ItemDAO {

    public boolean addItem(Item item) throws SQLException {
        String sql = "INSERT INTO item (item_name, price, quantity, category) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, item.getItemName());
            ps.setDouble(2, item.getPrice());
            ps.setInt(3, item.getQuantity());
            ps.setString(4, item.getCategory());

            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }
}
