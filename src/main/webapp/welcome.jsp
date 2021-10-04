<%--
  Created by IntelliJ IDEA.
  User: Chandru
  Date: 18-09-2021
  Time: 12:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Details</title>
</head>
<body>
    <h2 style="color:tomato;">
        <%= "Welcome " + request.getAttribute("firstName") + " " + request.getAttribute("lastName") %>
    </h2>
    <form action="CheckUser" method="post">
        <h4>Check details to search your Team mates</h4>
        Enter Mobile: <input type="number" name="userMobile"><br><br>
        Enter Email:&nbsp; <input type="text" name="userEmail"><br><br>
        <input type="submit" value="Search">
    </form>
</body>
</html>
