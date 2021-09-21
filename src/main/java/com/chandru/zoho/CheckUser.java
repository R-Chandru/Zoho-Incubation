package com.chandru.zoho;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "CheckUser", value = "/CheckUser")
public class CheckUser extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();
        response.setContentType("text/html");

        String userMobile = request.getParameter("userMobile");
        String userEmail = request.getParameter("userEmail");

        request.setAttribute("userEmail", userEmail);

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("Success.jsp");
        requestDispatcher.forward(request, response);
    }
}
