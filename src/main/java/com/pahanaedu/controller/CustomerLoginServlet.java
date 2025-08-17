package com.system.controller;

import com.system.model.CustomerLoginModel;
import com.system.service.CustomerLoginService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/CustomerLoginServlet")
public class CustomerLoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CustomerLoginService service = new CustomerLoginService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accountNumber = request.getParameter("account_number");
        String password = request.getParameter("password");

        CustomerLoginModel customer = new CustomerLoginModel();
        customer.setAccountNumber(accountNumber);
        customer.setPassword(password);

        boolean isValid = service.authenticate(customer);

        if (isValid) {
            HttpSession session = request.getSession();
            session.setAttribute("accountNumber", accountNumber);
            response.sendRedirect("CustomerDashboard.jsp");
        } else {
            response.sendRedirect("CustomerLogin.jsp?error=Invalid+Account+Number+or+Password");
        }
    }
}
