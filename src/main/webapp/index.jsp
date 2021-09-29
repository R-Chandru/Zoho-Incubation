<%@ page contentType="text/html; charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Welcome</title>
    <link rel="stylesheet" type="text/css" href="Styles/styles.css">
</head>
<body>
    <h1 style="color:tomato;">Zoho Incubation Tasks</h1>

    <form action="Details.jsp" style="margin-top: 20px;">
        <input type="submit" value="Simple Application with Servlet and JSP">
    </form>

    <form action="Rest_Servlet/" style="margin-top: 10px;">
        <input type="submit" value="Contact API">
    </form>

    <form action="chats/" style="margin-top: 10px;">
        <input type="submit" value="Chats API">
    </form>

    <form action="chats/messages/" style="margin-top: 10px;">
        <input type="submit" value="Message API">
    </form>

    <form action="Get_RestApi" style="margin-top: 10px;">
        <input type="submit" value="Display REST API">
    </form>

    <form action="ChatLogin.jsp" style="margin-top: 10px;">
        <input type="submit" value="Chat Application with Web sockets">
    </form>

    <br/>
</body>
</html>