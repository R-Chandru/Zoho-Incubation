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
            width: 100%;
            height: 200px;
            border: 1px solid silver;
            overflow-y: scroll;
        }
        #msg {width: 99%;}
        h1 {text-align: center;}
    </style>
</head>
<script type="text/javascript">

    var wsUrl;
    if (window.location.protocol == 'http:') {
        wsUrl = 'ws://';
    } else {
        wsUrl = 'wss://';
    }
    var ws = new WebSocket(wsUrl + window.location.host + "/zoho/chat");
    console.log(ws)
    ws.connect()

    ws.onmessage = function(event) {
        var mySpan = document.getElementById("chat");
        mySpan.innerHTML+=event.data+"<br/>";
    };

    ws.onerror = function(event){
        console.log("Error ", event)
    }
    function sendMsg() {
        var msg = document.getElementById("msg").value;
        console.log(msg)
        if(msg)
        {
            ws.send(msg);
        }
        document.getElementById("msg").value="";
    }
</script>
<body>
<h1>Live Chat updates</h1>

<div>
    <div id="chat" class="chat"></div>
    <div>
        <input type="text" name="msg" id="msg" placeholder="Enter message here"/>
        <button onclick="return sendMsg();">Enter</button>
    </div>
</div>
</body>
</html>