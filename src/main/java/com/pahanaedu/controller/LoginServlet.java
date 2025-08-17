package com.system.controller;

import com.system.service.AdminService;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private AdminService adminService = new AdminService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String role = request.getParameter("role");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if ("admin".equalsIgnoreCase(role)) {
            boolean isValid = adminService.login(username, password);
            if (isValid) {
                // Store session
                HttpSession session = request.getSession();
                session.setAttribute("username", username);
                session.setAttribute("role", "admin");
                response.sendRedirect("adminDashboard.jsp");
            } else {
                response.sendRedirect("adminLogin.jsp?error=invalid");
            }
        } else {
            response.getWriter().println("Unsupported role!");
        }
    }
}
