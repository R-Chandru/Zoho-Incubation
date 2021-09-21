package com.chandru.zoho;

import com.chandru.zoho.model.User;
import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.PrintWriter;
import java.util.*;
import java.io.IOException;

@WebServlet(name = "Rest_Servlet", value = "/Rest_Servlet")
public class Rest_Servlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<User> userJSON = getUsers();

        Gson gson = new Gson();
        String JSON = gson.toJson(userJSON);

        PrintWriter writer = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        writer.write(JSON);
        writer.close();

        /*request.setAttribute("Json", userJSON);

        try {
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("RestApi.jsp");
            requestDispatcher.forward(request, response);
        }
        catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    protected List<User> getUsers() {
        return Arrays.asList(
                new User(101, "Dhoni", "MS", "dhoni@gmail.com"),
                new User(102, "Virat", "kohli", "virat@gmail.com"),
                new User(103, "Rohit", "Sharma", "rohit@gmail.com")
        );
    }
}
