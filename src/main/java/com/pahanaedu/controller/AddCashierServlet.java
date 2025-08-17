package com.system.controller;

import com.system.model.Cashier;
import com.system.service.CashierService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/addCashier")
public class AddCashierServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CashierService cashierService = new CashierService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/AddCashier.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String name = request.getParameter("name");
        String address = request.getParameter("address");
        String nic = request.getParameter("nic");
        String phoneNumber = request.getParameter("phoneNumber");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        Cashier cashier = new Cashier(name, address, nic, phoneNumber, username, password);

        try {
            boolean success = cashierService.addCashier(cashier);
            if (success) {
                // Redirect to the cashier listing page on success
                response.sendRedirect("ViewCashiers.jsp");
                return;
            } else {
                request.setAttribute("errorMessage", "Failed to add cashier.");
                request.getRequestDispatcher("/AddCashier.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error: " + e.getMessage());
            request.getRequestDispatcher("/AddCashier.jsp").forward(request, response);
        }
    }
}
