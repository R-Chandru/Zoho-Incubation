package com.chandru.zoho;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name = "SignIn", value = "/SignIn")
public class SignIn extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userEmail = request.getParameter("UserEmail");
        String userPass = request.getParameter("UserPassword");
        ResultSet resultSet = null;
        String userName = "";
        String decryptPassword = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/zoho", "root", "chandru");
            PreparedStatement statement = connection.prepareStatement("select * from users where userEmail=?");
            statement.setString(1, userEmail);

            resultSet = statement.executeQuery();

            while (resultSet.next() && resultSet != null) {
                decryptPassword = decryptPass(resultSet.getString("userPass"));
                userName = resultSet.getString("userName");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        PrintWriter writer = response.getWriter();
        response.setContentType("text/html");

        if (userPass.equals(decryptPassword) && !userPass.equals(""))
            writer.write("Successfully Logged In as " + userName);
        else
            writer.write("Incorrect password or Account not exists");
    }

    protected String decryptPass(String encryptedPass) {
        String lowerAlphabets = "0abcdefghijklmnopqrstuvwxyz";
        String upperAlphabets = "0ABCDEFGHIJKLNMOPQRSTUVWXYZ";
        String key = "abc";

        int keyPointer = 0;

        String result = "";

        for (int itr=0 ; itr<encryptedPass.length() ; itr++) {
            if (encryptedPass.charAt(itr) == ' ') {
                result += " ";
                keyPointer = 0;
                continue;
            }

            char tempChar = encryptedPass.charAt(itr);

            //To start the key from first
            if (keyPointer >= key.length())
                keyPointer = 0;
            char tempKey = key.toLowerCase().charAt(keyPointer);
            keyPointer++;

            //For lower case aplhabet
            if ( (int)tempChar>=97 && (int)tempChar<123) {
                int charIndex = lowerAlphabets.indexOf(tempChar);
                int keyIndex = lowerAlphabets.indexOf(tempKey);
                int encryptIndex = (charIndex - keyIndex) % 26;

                result += lowerAlphabets.charAt(encryptIndex);
            }
            //For upper case alphabet
            else {
                int charIndex = upperAlphabets.indexOf(tempChar);
                int keyIndex = lowerAlphabets.indexOf(tempKey);
                int encryptIndex = (charIndex - keyIndex) % 26;

                result += upperAlphabets.charAt(encryptIndex);
            }
        }
        return result;
    }
}
