<%--
  Created by IntelliJ IDEA.
  User: sergey
  Date: 25.11.14
  Time: 20:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Top 500 Rolling Stone Albums in your Last.fm library</title>
</head>
<body>
<form action="/getdata" method="get">
  <div><input type="text" name="user" placeholder="username"/></div>
  <div><input type="submit" value="Show"/></div>
</form>
</body>
</html>
