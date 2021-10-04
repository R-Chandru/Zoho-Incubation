package com.chandru.zoho;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name = "CheckUser", value = "/CheckUser")
public class CheckUser extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();
        response.setContentType("text/html");

        String userMobile = request.getParameter("userMobile");
        String userEmail = request.getParameter("userEmail");

        //SQL injection
        /*try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/zoho", "root", "chandru");
            Statement statement = connection.createStatement();

            ResultSet set = statement.executeQuery("select * from contacts where userEmail = " +"'"+ userEmail + "'");

            while (set.next()) {
                writer.println(set.getString("userFirstName") +"\t"+ set.getString("userNumber") +"\t"+ set.getString("userEmail") + "\n");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }*/

        //Preventing from SQL injection using prepared statement
        ResultSet set = null;
        String name = "";
        String number = "";
        String userType = "";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/zoho", "root", "chandru");
            String getContact = "select * from contacts where userEmail = ?";
            PreparedStatement statement = connection.prepareStatement(getContact);

            statement.setString(1, userEmail);

            set = statement.executeQuery();
            while (set.next()) {
                name = set.getString("userFirstName");
                number = set.getString("userNumber");
                userType = set.getString("userType");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        /*request.setAttribute("userEmail", userEmail);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("Success.jsp");
        requestDispatcher.forward(request, response);*/

        if (!name.equals("")) {
            writer.println("<h2> Account exists! </h2>");
            writer.println("<br />Account name = " + name);
            writer.println("<br />Account number = " + number);
            writer.println("<br />Account Type = " + userType);
        }
        else {
            writer.println("<h2> Account not found </h2>");
        }
    }
}
