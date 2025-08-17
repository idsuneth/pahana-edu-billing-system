package com.system.controller;

import com.system.model.Item;
import com.system.service.ItemService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/addItem")
public class AddItemServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ItemService itemService = new ItemService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/AddItem.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String itemName = request.getParameter("itemName");
        String category = request.getParameter("category");
        double price = 0;
        int quantity = 0;

        try {
            price = Double.parseDouble(request.getParameter("price"));
            quantity = Integer.parseInt(request.getParameter("quantity"));
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Price and Quantity must be valid numbers.");
            request.getRequestDispatcher("/AddItem.jsp").forward(request, response);
            return;
        }

        Item item = new Item(itemName, price, quantity, category);

        try {
            boolean success = itemService.addItem(item);
            if (success) {
                // Redirect to ViewItem.jsp on success
                response.sendRedirect("ViewItems.jsp");
                return;
            } else {
                request.setAttribute("errorMessage", "Failed to add item.");
                request.getRequestDispatcher("/AddItem.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error: " + e.getMessage());
            request.getRequestDispatcher("/AddItem.jsp").forward(request, response);
        }
    }
}
