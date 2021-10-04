package com.chandru.zoho;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "details", value = "/details")
public class details extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");

        //Prevents from running script functions.
        /*if (firstName.contains("script"))
            firstName=" ";
        if (lastName.contains("script"))
            lastName = " ";*/

        request.setAttribute("firstName", firstName);
        request.setAttribute("lastName", lastName);

        try {
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("welcome.jsp");
            requestDispatcher.forward(request, response);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
