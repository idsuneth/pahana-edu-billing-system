package com.system.controller;

import com.system.util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/BillingServlet")
public class BillingServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accountNumber = request.getParameter("accountNumber");

        String[] itemNames = request.getParameterValues("itemName");
        String[] quantities = request.getParameterValues("quantity");
        String[] prices = request.getParameterValues("price");

        if (accountNumber == null || itemNames == null || quantities == null || prices == null) {
            response.sendRedirect("adminBillGenerator.jsp?msg=error");
            return;
        }

        try (Connection con = DBUtil.getConnection()) {

            // Check if customer exists
            String customerCheckSql = "SELECT COUNT(*) FROM customer WHERE account_number = ?";
            try (PreparedStatement psCheck = con.prepareStatement(customerCheckSql)) {
                psCheck.setString(1, accountNumber);
                ResultSet rs = psCheck.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) {
                    response.sendRedirect("adminBillGenerator.jsp?msg=notfound");
                    return;
                }
            }

            // Calculate total amount
            double totalAmount = 0;
            for (int i = 0; i < itemNames.length; i++) {
                int qty = Integer.parseInt(quantities[i]);
                double price = Double.parseDouble(prices[i]);
                totalAmount += qty * price;
            }

            // Insert into bill table
            String insertBillSql = "INSERT INTO bill (account_number, total_amount) VALUES (?, ?)";
            try (PreparedStatement psBill = con.prepareStatement(insertBillSql, Statement.RETURN_GENERATED_KEYS)) {
                psBill.setString(1, accountNumber);
                psBill.setDouble(2, totalAmount);
                int affectedRows = psBill.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Creating bill failed, no rows affected.");
                }

                ResultSet generatedKeys = psBill.getGeneratedKeys();
                int billId = 0;
                if (generatedKeys.next()) {
                    billId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating bill failed, no ID obtained.");
                }

                // Insert bill items
                String insertItemSql = "INSERT INTO bill_item (bill_id, item_name, quantity, price_per_unit) VALUES (?, ?, ?, ?)";
                try (PreparedStatement psItem = con.prepareStatement(insertItemSql)) {
                    for (int i = 0; i < itemNames.length; i++) {
                        psItem.setInt(1, billId);
                        psItem.setString(2, itemNames[i]);
                        psItem.setInt(3, Integer.parseInt(quantities[i]));
                        psItem.setDouble(4, Double.parseDouble(prices[i]));
                        psItem.addBatch();
                    }
                    psItem.executeBatch();
                }

                // Prepare bill text for display
                StringBuilder billText = new StringBuilder();
                billText.append("Bill ID: ").append(billId).append("\n");
                billText.append("Account Number: ").append(accountNumber).append("\n");
                billText.append("--------------------------------------------------\n");
                billText.append(String.format("%-20s %-10s %-15s %-10s\n", "Item Name", "Quantity", "Price per Unit", "Total"));
                billText.append("--------------------------------------------------\n");

                for (int i = 0; i < itemNames.length; i++) {
                    int qty = Integer.parseInt(quantities[i]);
                    double price = Double.parseDouble(prices[i]);
                    double lineTotal = qty * price;
                    billText.append(String.format("%-20s %-10d %-15.2f %-10.2f\n", itemNames[i], qty, price, lineTotal));
                }
                billText.append("--------------------------------------------------\n");
                billText.append(String.format("Total Amount: Rs %.2f\n", totalAmount));

                // Set the bill string and forward to JSP
                request.setAttribute("bill", billText.toString());
                request.getRequestDispatcher("adminBillGenerator.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("adminBillGenerator.jsp?msg=error");
        }
    }
}
