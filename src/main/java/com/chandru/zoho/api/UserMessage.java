package com.chandru.zoho.api;

import com.chandru.zoho.model.Message;
import com.chandru.zoho.model.PrivateChat;
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

@WebServlet(name = "UserMessage", value = "/chats/messages/*")
public class UserMessage extends HttpServlet {
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

    //Denying the access for not logged in users - BROKEN ACCESS CONTROL
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        String userNumber = req.getParameter("number") != null ? req.getParameter("number") : "";
        String userPass = req.getParameter("pass") != null ? req.getParameter("pass") : "";
        List<Message> messageList = new ArrayList<>();
        ResultSet set = null;

        if (userPass.equals("chandru")) {
            try {
                String updateStatus = "update messages set messageStatus='read' where senderNumber = " + userNumber;
                statement.executeUpdate(updateStatus);
                set = statement.executeQuery("select * from messages where senderNumber = " + userNumber + " or receiverNumber = " + userNumber);
                while (set.next()) {
                    messageList.add(
                            new Message(set.getString("senderNumber"), set.getString("receiverNumber"),
                                    set.getString("message"), set.getString("messageTime"),
                                    set.getString("messageStatus"), set.getString("messageReadTime")
                            )
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
        else {
            PrintWriter writer = response.getWriter();
            response.setContentType("text/html");
            writer.write("You don't have the access");
        }

        System.out.println(userNumber);
    }

    /*@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Message> messageList = new ArrayList<>();
        ResultSet set = null;

        String path = request.getPathInfo();
        String[] resource = path.split("/");

        if (resource.length > 1) {
            try {
                String updateStatus = "update messages set messageStatus='read' where senderNumber = " + resource[1];
                statement.executeUpdate(updateStatus);
                set = statement.executeQuery("select * from messages where senderNumber = " + resource[1] + ";");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                String updateStatus = "update messages set messageStatus='read'";
                statement.executeUpdate(updateStatus);
                set = statement.executeQuery("select * from messages");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            while (set.next()) {
                messageList.add(
                        new Message(set.getString("senderNumber"), set.getString("receiverNumber"),
                                set.getString("message"), set.getString("messageTime"),
                                set.getString("messageStatus"), set.getString("messageReadTime")
                        )
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
    }*/

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        createMessage(requestBody);

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
                String deleteContact = "delete from messages where senderNumber = " + resource[1] +";";
                statement.executeUpdate(deleteContact);
                createMessage(requestBody);
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
                String deleteContact = "delete from messages where senderNumber = " + resource[1] +";";
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

    protected void createMessage(String requestBody) {
        JSONObject jsonObject = new JSONObject(requestBody);
        String senderNumber = jsonObject.getString("senderNumber");
        String receiverNumber = jsonObject.getString("receiverNumber");
        String message = jsonObject.getString("message");
        String messageTime = jsonObject.getString("messageTime");
        String messageReadTime = jsonObject.getString("messageReadTime");

        try {
            String insertContact = "insert into messages values (" + 0 + ", '" + senderNumber + "', '" + receiverNumber
                    + "', '" + message + "', '" + messageTime + "', 'sent', '" + "', '" + messageReadTime + "');";
            statement.executeUpdate(insertContact);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
