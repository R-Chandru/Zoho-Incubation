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
    <h1 style="color:tomato;">
        <%= "Welcome " + request.getAttribute("firstName") + " " + request.getAttribute("lastName") %>
    </h1>
    <form action="CheckUser" method="post">
        Enter Mobile: <input type="number" name="userMobile"><br><br>
        Enter Email:&nbsp; <input type="email" name="userEmail"><br><br>
        <input type="submit" value="Check">
    </form>
</body>
</html>
