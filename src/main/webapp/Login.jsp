<%--
  Created by IntelliJ IDEA.
  User: Chandru
  Date: 30-09-2021
  Time: 11:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h2>Sign Up</h2><br>
<form action="Login" class="adjust" method="post">
    Name:&emsp;&emsp; <input type="text" name="name"><br><br>
    Email:&emsp;&emsp; <input type="text" name="email"><br><br>
    AccountNo: <input type="text" name="account"><br><br>
    Pass:&emsp;&emsp;&ensp; <input type="password" name="pass"><br><br>
    <input type="submit" value="Create Account"><br>
</form>
<form action="Signin.jsp">
    <input type="submit" value="Already have a account">
</form>
</body>
</html>
