<%--
  Created by IntelliJ IDEA.
  User: Chandru
  Date: 23-09-2021
  Time: 12:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>JSP Page</title>
    <style media="screen" type="text/css">
        .chat {
            width: 50%;
            height: 200px;
            border: 1px solid silver;
        }
        #msg {width: 49%;}
        #btn {margin-left: 25%;}
    </style>
</head>
</script>
<script type="text/javascript">
    var wsUrl;
    var userName = '<%=request.getAttribute("name")%>';

    if (window.location.protocol == 'http:') {
        wsUrl = 'ws://';
    } else {
        wsUrl = 'wss://';
    }
    var ws = new WebSocket(wsUrl + window.location.host + "/Zoho_war_exploded/chat");

    ws.onmessage = function(event) {
        var mySpan = document.getElementById("chat");
        mySpan.innerHTML+=event.data+"<br/>";
    };

    ws.onerror = function(event){
        console.log("Error ", event)
    }

    function sendMsg() {
        var msg = document.getElementById("msg").value;
        if(msg)
        {
            ws.send(userName + " : "+ msg);
        }
        document.getElementById("msg").value="";
    }
</script>
<body>
<h1>Chat</h1>

<div>
    <div id="chat" class="chat"></div><br>
    <div>
        <input type="text" name="msg" id="msg" placeholder="Enter message"/><br><br>
        <button onclick="return sendMsg();" id="btn">Enter</button>
    </div>
</div>
</body>
</html>
