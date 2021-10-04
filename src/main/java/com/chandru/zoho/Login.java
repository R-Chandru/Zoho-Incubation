package com.chandru.zoho;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

@WebServlet(name = "Login", value = "/Login")
public class Login extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String userName = request.getParameter("name");
        String userEmail = request.getParameter("email");
        String userAccount = request.getParameter("account");
        String userPass = request.getParameter("pass");

        String encryptedPass = encryptPass(userPass);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/zoho", "root", "chandru");
            PreparedStatement statement = connection.prepareStatement("insert into users values(0, ?, ?, ?, ?)");
            statement.setString(1, userName);
            statement.setString(2, userEmail);
            statement.setString(3, userAccount);
            statement.setString(4, encryptedPass);
            statement.executeUpdate();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("Signin.jsp");
        dispatcher.forward(request, response);
    }

    protected String encryptPass(String userPass) {
        String lowerAlphabets = "0abcdefghijklmnopqrstuvwxyz";
        String upperAlphabets = "0ABCDEFGHIJKLNMOPQRSTUVWXYZ";
        String key = "abc";

        int keyPointer = 0;

        String result = "";

        for (int itr=0 ; itr<userPass.length() ; itr++) {
            if (userPass.charAt(itr) == ' ') {
                result += " ";
                keyPointer = 0;
                continue;
            }

            char tempChar = userPass.charAt(itr);

            //To start the key from first
            if (keyPointer >= key.length())
                keyPointer = 0;
            char tempKey = key.toLowerCase().charAt(keyPointer);
            keyPointer++;

            //For lower case aplhabet
            if ( (int)tempChar>=97 && (int)tempChar<123) {
                int charIndex = lowerAlphabets.indexOf(tempChar);
                int keyIndex = lowerAlphabets.indexOf(tempKey);
                int encryptIndex = (charIndex + keyIndex) % 26;

                result += lowerAlphabets.charAt(encryptIndex);
            }
            //For upper case alphabet
            else {
                int charIndex = upperAlphabets.indexOf(tempChar);
                int keyIndex = lowerAlphabets.indexOf(tempKey);
                int encryptIndex = (charIndex + keyIndex) % 26;

                result += upperAlphabets.charAt(encryptIndex);
            }
        }

        return result;
    }
}
