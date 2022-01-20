<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="no.hvl.dat152.obl3.idp.oauth.utility.Constants,no.hvl.dat152.obl3.controller.RequestHelper" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<% 
	if (RequestHelper.isLoggedIn(request))
		request.getRequestDispatcher("searchpage").forward(request, response);
%>
		
	<h3>A Searchable English Dictionary</h3>
	<p>You must be logged in to use this service</p>
	<p><font color="red">${message}</font></p>
	<p><a href="login">Log in</a></p>
	<p><a href="newuser">New User</a></p>
	<p><a href="http://localhost:8080/BlogWebApp">BlogWebApp</a></p>
</body>
</html>