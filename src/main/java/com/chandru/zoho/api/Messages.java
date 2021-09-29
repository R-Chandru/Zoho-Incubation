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
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "Messages", value = "/chats/*")
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
        List<PrivateChat> messageList = new ArrayList<>();
        ResultSet set = null;

        String path = request.getPathInfo();
        String[] resource = path.split("/");

        if (resource.length == 4 && resource[2].equals("messages")) {
            String messageId = resource[3];
            String contactNumber = resource[1];
            getSingleMessage(messageId, contactNumber, response);
        }
        else if (resource.length == 3 && resource[2].equals("messages")) {
            getMessages(resource[1], response);
        }
        else if (resource.length > 0) {
            try {
                String updateStatus = "update private_chat set status='read' where userNumber = " + resource[1];
                statement.executeUpdate(updateStatus);
                set = statement.executeQuery("select * from private_chat where userNumber = " + resource[1] + ";");
                while (set.next()) {
                    messageList.add(
                            new PrivateChat(set.getInt("Id"), set.getString("userName"), set.getString("userNumber"),
                                    set.getString("userProfilePic"), set.getString("userEmail"),
                                    set.getString("userDescription"), set.getString("userType"),
                                    set.getString("lastSeen"), set.getString("lastMessage"), set.getString("status"))
                    );
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                String updateStatus = "update private_chat set status='read'";
                statement.executeUpdate(updateStatus);
                set = statement.executeQuery("select * from private_chat");
                while (set.next()) {
                    messageList.add(
                            new PrivateChat(set.getInt("Id"), set.getString("userName"), set.getString("userNumber"),
                                    set.getString("userProfilePic"), set.getString("userEmail"),
                                    set.getString("userDescription"), set.getString("userType"),
                                    set.getString("lastSeen"), set.getString("lastMessage"), set.getString("status"))
                    );
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
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
        String path = request.getPathInfo();
        String[] resource = path.split("/");

        if (resource.length > 2 && resource[2].equals("messages")) {
            try {
                postMessage(resource[1], requestBody, response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            insertContact(requestBody);
            PrintWriter writer = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            writer.write(requestBody);
        }
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
                updateContact(requestBody, resource[1]);
                writer.write(requestBody);
            }
            catch (Exception e) {
                e.printStackTrace();
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

        if (resource.length == 4 && resource[2].equals("messages")) {
            try {
                deleteMessage(response, resource[3]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (resource.length < 1) {
            JSONObject object = new JSONObject();
            object.put("message", "Cannot Delete, Invalid Id or Id not found");
            writer.write(object.toString());
        }
        else {
            JSONObject object = new JSONObject();
            try {
                String deleteContact = "delete from private_chat where userNumber = " + resource[1] +";";
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

    protected void updateContact(String requestBody, String contactId) {
        JSONObject jsonObject = new JSONObject(requestBody);
        String userName = jsonObject.getString("userName");
        String userNumber = jsonObject.getString("userNumber");
        String userProfilePic = jsonObject.getString("userProfilePic");
        String userEmail = jsonObject.getString("userEmail");
        String userDescription = jsonObject.getString("userDescription");
        String userType = jsonObject.getString("userType");
        String lastSeen = jsonObject.getString("messageTime");
        String lastMessage = jsonObject.getString("lastMessage");

        try {
            PreparedStatement query = connection.prepareStatement("update private_chat set userName=?, userNumber=?," +
                    "userProfilePic=?, userEmail=?, userDescription=?, userType=?, lastSeen=?, lastMessage=? where userNumber=?");
            query.setString(1, userName);
            query.setString(2, userNumber);
            query.setString(3, userProfilePic);
            query.setString(4, userEmail);
            query.setString(5, userDescription);
            query.setString(6, userType);
            query.setString(7, lastSeen);
            query.setString(8, lastMessage);
            query.setString(9, contactId);
            query.executeUpdate();
        }
        catch (Exception e) {

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
        String lastSeen = jsonObject.getString("messageTime");
        String lastMessage = jsonObject.getString("lastMessage");

        try {
            String insertContact = "insert into private_chat values (" + 0 + ", '" + userName + "', '" + userNumber
                    + "', '" + userProfilePic + "', '" + userEmail + "', '" +userDescription + "', '"+ userType + "', '"
                    + lastSeen + "', '" + lastMessage +"', 'sent');";
            statement.executeUpdate(insertContact);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void getMessages(String userNumber, HttpServletResponse response) {
        List<Message> list = new ArrayList<>();
        try {
            PreparedStatement query = connection.prepareStatement("select * from messages where senderNumber=? or receiverNumber=?;");
            query.setString(1, userNumber);
            query.setString(2, userNumber);
            ResultSet set = query.executeQuery();

            while (set.next()) {
                list.add(
                        new Message(set.getString("senderNumber"), set.getString("receiverNumber"),
                                set.getString("message"), set.getString("messageTime"),
                                set.getString("messageStatus"), set.getString("messageReadTime")
                        )
                );
            }
            Gson gson = new Gson();
            String JSON = gson.toJson(list);

            PrintWriter writer = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            writer.write(JSON);
            writer.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void getSingleMessage(String messageId, String contactNumber, HttpServletResponse response) {
        List<Message> list = new ArrayList<>();
        try {
            PreparedStatement query = connection.prepareStatement("select * from messages where Id=? and (senderNumber=? or receiverNumber=?)");
            query.setString(1, messageId);
            query.setInt(2, Integer.parseInt(contactNumber));
            query.setInt(3, Integer.parseInt(contactNumber));
            ResultSet set = query.executeQuery();

            while (set.next()) {
                list.add(new Message(set.getString("senderNumber"), set.getString("receiverNumber"),
                        set.getString("message"), set.getString("messageTime"),
                        set.getString("messageStatus"), set.getString("messageReadTime"))
                );
            }

            Gson gson = new Gson();
            String JSON = gson.toJson(list);

            PrintWriter writer = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            writer.write(JSON);
            writer.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void postMessage(String userNumber, String requestBody, HttpServletResponse response) throws Exception{
        PrintWriter writer = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JSONObject jsonObject = new JSONObject(requestBody);
        String senderNumber = jsonObject.getString("senderNumber");
        String receiverNumber = jsonObject.getString("receiverNumber");
        String message = jsonObject.getString("message");
        String messageTime = jsonObject.getString("messageTime");
        String messageReadTime = jsonObject.getString("messageReadTime");

        if (userNumber.equals(senderNumber) || userNumber.equals(receiverNumber)) {
            try {
                String insertQuery = "insert into messages (Id, senderNumber, receiverNumber, message, messageTime, messageStatus, messageReadTime) " +
                        "values(0, ?, ?, ?, ?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(insertQuery);
                statement.setString(1, senderNumber);
                statement.setString(2, receiverNumber);
                statement.setString(3, message);
                statement.setString(4, messageTime);
                statement.setString(5, "sent");
                statement.setString(6, messageReadTime);
                statement.executeUpdate();
                writer.write(requestBody);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            writer.write("Invalid Sender or receiver Id");
        }
    }

    protected void deleteMessage(HttpServletResponse response, String messageId) throws Exception {
        PrintWriter writer = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JSONObject object = new JSONObject();
        try {
            String deleteContact = "delete from messages where Id = " + messageId +";";
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
