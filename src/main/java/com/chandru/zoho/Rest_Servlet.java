package com.chandru.zoho;

import com.chandru.zoho.model.User;
import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.io.IOException;
import java.util.stream.Collectors;

@WebServlet(name = "Rest_Servlet", value = "/Rest_Servlet/*")
public class Rest_Servlet extends HttpServlet {
    private static Connection connection;
    private static Statement statement;

    @Override
    public void init() throws ServletException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/zoho", "root", "chandru");
            statement = connection.createStatement();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<User> userList = new ArrayList<>();
        ResultSet set = null;

        String path = request.getPathInfo();
        String[] resource = path.split("/");

        if (resource.length > 1) {
            try {
                set = statement.executeQuery("select * from contacts where userNumber = " + resource[1] + ";");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                set = statement.executeQuery("select * from contacts");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            while (set.next()) {
                userList.add(
                        new User(set.getInt("Id"), set.getString("userFirstName"),
                                set.getString("userLastName"), set.getString("userNumber"),
                                set.getString("userEmail"), set.getString("userType"),
                                set.getString("lastCall"))
                );
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        String JSON = gson.toJson(userList);

        PrintWriter writer = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        writer.write(JSON);
        writer.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        writer.write(requestBody);

        insertContact(requestBody);

        //System.out.println(userFirstName);
        //contackJSON.add( new User(Id, userFirstName, userLastName, userNumber, userEmail, userType, lastCall) ) ;
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        writer.write(requestBody);

        String path = request.getPathInfo();
        String[] resource = path.split("/");

        if (resource[1] == null) {
            JSONObject object = new JSONObject();
            object.put("message", "Cannot perform put, Invalid Id");
            writer.write(object.toString());
        }
        else {
            JSONObject object = new JSONObject();
            try {
                String deleteContact = "delete from contacts where userNumber = " + resource[1] +";";
                statement.executeUpdate(deleteContact);
                insertContact(requestBody);
            }
            catch (Exception e) {
                object.put("message", "Updation Failed");
                writer.write(object.toString());
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String path = request.getPathInfo();
        String[] resource = path.split("/");

        if (resource[1] == null) {
            JSONObject object = new JSONObject();
            object.put("message", "Cannot Delete, Invalid Id");
            writer.write(object.toString());
        }
        else {
            JSONObject object = new JSONObject();
            try {
                String deleteContact = "delete from contacts where userNumber = " + resource[1] +";";
                statement.executeUpdate(deleteContact);
                object.put("message", "Deleted Successfully");
                writer.write(object.toString());
            }
            catch (Exception e) {
                object.put("message", "Deletion Failed");
                writer.write(object.toString());
            }
        }
    }

    protected void insertContact(String requestBody) {
        JSONObject jsonObject = new JSONObject(requestBody);
        int Id = (int)(Math.random()*(200-999+1)+200);
        String userFirstName = jsonObject.getString("userFirstName");
        String userLastName = jsonObject.getString("userLastName");
        String userNumber = jsonObject.getString("userNumber");
        String userEmail = jsonObject.getString("userEmail");
        String userType = jsonObject.getString("userType");
        String lastCall = jsonObject.getString("lastCall");

        try {
            String insertContact = "insert into contacts values (" + 0 + ", '" + userFirstName + "', '" + userLastName
                    + "', '" + userNumber + "', '" + userEmail + "', '" + userType + "', '" + lastCall + "');";
            statement.executeUpdate(insertContact);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*protected List<User> getUsers() {
        return Arrays.asList(
                new User(101, "Dhoni", "MS", "2123323", "dhoni@gmail.com", "Friend", "2021-09-23 12:44 IST"),
                new User(102, "Virat", "kohli", "5894394", "virat@gmail.com", "Close Friend", "2021-09-24 18:25 IST"),
                new User(103, "Rohit", "Sharma", "9434233", "rohit@gmail.com", "Friend", "2021-09-22 07:18 IST"),
                new User(104, "Sachin", "Tendulkar", "2324334", "sachin@gmail.com", "Family", "2021-09-24 01:06 IST")
        );
    }*/
}
