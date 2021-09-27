package com.chandru.zoho.chat_application;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "ChatLogin", value = "/ChatLogin")
public class ChatLogin extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName = request.getParameter("userName");
        request.setAttribute("name", userName);

        try {
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("ChatWindow.jsp");
            requestDispatcher.forward(request, response);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
