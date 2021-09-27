package com.chandru.zoho;

import com.chandru.zoho.model.PrivateMessage;
import com.chandru.zoho.model.User;
import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "Messages", value = "/messages/*")
public class Messages extends HttpServlet {
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
        List<PrivateMessage> messageList = new ArrayList<>();
        ResultSet set = null;

        String path = request.getPathInfo();
        String[] resource = path.split("/");

        if (resource.length > 1) {
            try {
                set = statement.executeQuery("select * from messages where userNumber = " + resource[1] + ";");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                set = statement.executeQuery("select * from messages");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            while (set.next()) {
                messageList.add(
                        new PrivateMessage(set.getInt("Id"), set.getString("userName"), set.getString("userNumber"),
                                set.getString("userProfilePic"), set.getString("userEmail"),
                                set.getString("userDescription"), set.getString("userType"),
                                set.getString("lastSeen"), set.getString("lastMessage"))
                );
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        String JSON = gson.toJson(messageList);

        PrintWriter writer = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        writer.write(JSON);
        writer.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        insertContact(requestBody);

        PrintWriter writer = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        writer.write(requestBody);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        String path = request.getPathInfo();
        String[] resource = path.split("/");

        if (resource.length < 1) {
            JSONObject object = new JSONObject();
            object.put("message", "Cannot perform put, Invalid Id or Id not found");
            writer.write(object.toString());
        }
        else {
            JSONObject object = new JSONObject();
            try {
                String deleteContact = "delete from messages where userNumber = " + resource[1] +";";
                statement.executeUpdate(deleteContact);
                insertContact(requestBody);
                writer.write(requestBody);
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

        if (resource.length < 1) {
            JSONObject object = new JSONObject();
            object.put("message", "Cannot Delete, Invalid Id or Id not found");
            writer.write(object.toString());
        }
        else {
            JSONObject object = new JSONObject();
            try {
                String deleteContact = "delete from messages where userNumber = " + resource[1] +";";
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
        String userName = jsonObject.getString("userName");
        String userNumber = jsonObject.getString("userNumber");
        String userProfilePic = jsonObject.getString("userProfilePic");
        String userEmail = jsonObject.getString("userEmail");
        String userDescription = jsonObject.getString("userDescription");
        String userType = jsonObject.getString("userType");
        String lastSeen = jsonObject.getString("lastSeen");
        String lastMessage = jsonObject.getString("lastMessage");

        try {
            String insertContact = "insert into messages values (" + 0 + ", '" + userName + "', '" + userNumber
                    + "', '" + userProfilePic + "', '" + userEmail + "', '" +userDescription + "', '"+ userType + "', '"
                    + lastSeen + "', '" + lastMessage + "');";
            statement.executeUpdate(insertContact);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
