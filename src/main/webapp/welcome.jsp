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
    <title>Congrats</title>
    <link rel="stylesheet" type="text/css" href="Styles/styles.css">
</head>
<body>
    <h1 style="color:tomato;">
    <%= "Welcome " + request.getAttribute("firstname") + " " + request.getAttribute("lastname") %>
    </h1>
</body>
</html>
