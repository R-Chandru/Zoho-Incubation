<%--
  Created by IntelliJ IDEA.
  User: Chandru
  Date: 19-09-2021
  Time: 13:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login Success</title>
</head>
<body>
    <h2>Congrats</h2> <br>
    <%= "You have Successfully created acoount with "+
            request.getAttribute("userEmail") %> <br><br>
    <a href="index.jsp">Home</a>
</body>
</html>