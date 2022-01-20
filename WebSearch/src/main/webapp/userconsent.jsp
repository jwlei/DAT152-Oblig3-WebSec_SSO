<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="no.hvl.dat152.obl3.idp.oauth.utility.OpenIDUser,no.hvl.dat152.obl3.idp.oauth.utility.Constants,no.hvl.dat152.obl3.idp.oauth.utility.Scope,no.hvl.dat152.obl3.database.AppUser,
    java.util.List" 
    %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>User consent</title>
</head>
<body>
<!-- Present user info data based on the scope defined in the client original openid request -->
<!-- scope: 
openid -> OpenID authentication and ID token
email {email, email_verified}, 
phone {phone_number, phone_number_verified}, 
profile {name, family_name, given_name, middle_name, nickname, profile, picture, website, gender, birthdate, etc},
address {address} 
-->
<%
	OpenIDUser oidcu = (OpenIDUser) request.getSession().getAttribute(Constants.OIDC_USER);
	List<String> consents = oidcu.getConsents();
%>
<h3>Consent</h3>
<h4><b>Allow DAT152BlogWebApp access to your:</b></h4>
<form method="post" action="authorizehelper">
<input type="hidden" name="anticsrf" value="${anticsrf}">

<table>
	<%	for(String scope : consents) { %>
		<%	if(scope.equals(Scope.profile.name())){	%>
				<tr><td>profile</td><td><input type= "checkbox" id="profile" name="profile" value="profile" ></td></tr>
		<%	} else if(scope.equals(Scope.email.name())){	%>
				<tr><td>email</td><td><input type= "checkbox" id="email" name="email" value="email" ></td></tr>
		<% 	} else if(scope.equals(Scope.phone.name())){	%>
				<tr><td>phone</td><td><input type= "checkbox" id="phone" name="phone" value="phone" ></td></tr>
		<% 	} else if(scope.equals(Scope.address.name())){	%>
				<tr><td>address</td><td><input type= "checkbox" id="address" name="address" value="address" ></td></tr>
		<% 	} %>
	<% } %>
	<tr><td><input type="submit" name="submit" value="Allow"></td><td><input type="submit" name="submit" value="Deny"></td></tr>
</table>
</form>


</body>
</html>