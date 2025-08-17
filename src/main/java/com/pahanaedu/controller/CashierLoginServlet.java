package com.system.controller;

import com.system.model.CashierLogin;
import com.system.service.CashierLoginService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/CashierLoginServlet")
public class CashierLoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CashierLoginService loginService = new CashierLoginService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        CashierLogin cashier = new CashierLogin();
        cashier.setUsername(username);
        cashier.setPassword(password);

        boolean isValid = loginService.validateCashier(cashier);

        if (isValid) {
            HttpSession session = request.getSession();
            session.setAttribute("cashierUsername", username);
            response.sendRedirect("cashierDashboard.jsp");
        } else {
            response.sendRedirect("Cashier_Login.jsp?error=Invalid+username+or+password");
        }
    }
}
