<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="no.hvl.dat152.obl3.blog.Constants, java.util.List" %>
    <%@ page import="no.hvl.dat152.obl3.blog.database.UserXMLDbLogic" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Welcome to the DAT152BlogApp</title>
<link rel="stylesheet" type="text/css" href="css/styles.css">
</head>
<body>

<form action="login" method="post" autocomplete="off">
<div id="rcorners">
<h3>DAT152BlogWebApp Log in</h3>
<p><font color="red">${message}</font></p>
<table>
<tr>
<td><h5>Username</h5></td>
<td><input type="text" name="username" value="" size=20></td>
</tr>
<tr>
<td><h5>Password</h5></td><td><input type="password" name="password" value="" size=20></td>
</tr>
<tr>
<td><input type=submit name="submit" value="Login"></td><td><a href="register.jsp">Register</a></td>
</tr>
</table>
</div>
</form>
<p>
<form id="ssoform" method="get" action="sso" onsubmit="getClientId(); return false;">
<div id="rcorners">
<input type="hidden" name="response_type" value="code">
<input type="hidden" name="scope" value="openid phone profile">
<input type="hidden" name="client_id" id="clientID">
<input type="hidden" name="state" value=<%=Constants.STATE %>>
<input type="hidden" name="redirect_uri" value=<%=Constants.CALLBACK_ADDRESS %>>
<input type=submit value="SSO:Login with DAT152WebSearch">
</div>
</form>
<script>
function getClientId(){
	var clientid = prompt("Please enter your clientId");
	
	if (clientid == null || clientid == "") {
	  return;
	} else {
		document.getElementById("clientID").value = clientid;
		document.getElementById("ssoform").submit();
	}
}
</script>
</body>
</html>