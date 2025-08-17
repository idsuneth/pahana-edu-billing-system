package com.system.controller;

import com.system.model.CustomerModel;
import com.system.service.CustomerService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/addCustomer")
public class AddCustomerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CustomerService customerService = new CustomerService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Show Add Customer form
        request.getRequestDispatcher("/AddCustomer.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String accountNumber = request.getParameter("accountNumber");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String nic = request.getParameter("nic");
        String address = request.getParameter("address");
        String phoneNumber = request.getParameter("phoneNumber");

        CustomerModel customer = new CustomerModel(accountNumber, username, password, name, nic, address, phoneNumber);

        try {
            boolean success = customerService.addCustomer(customer);
            if (success) {
                // Redirect to customer list page after successful addition
                response.sendRedirect("ViewCustomers.jsp");
                return;
            } else {
                request.setAttribute("errorMessage", "Failed to add customer.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error: " + e.getMessage());
        }

        // Forward back to AddCustomer.jsp with error message if failed
        request.getRequestDispatcher("/AddCustomer.jsp").forward(request, response);
    }
}

