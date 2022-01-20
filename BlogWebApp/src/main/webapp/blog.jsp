<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="no.hvl.dat152.obl3.blog.util.Util,no.hvl.dat152.obl3.blog.database.User" %>
    <%@ page import="no.hvl.dat152.obl3.blog.Constants,java.util.List" %>
    <%@ page import="no.hvl.dat152.obl3.blog.servlets.RequestHelper" %>
    <%@ page import="no.hvl.dat152.obl3.blog.util.Role" %>
    <%@ page import="no.hvl.dat152.obl3.blog.tokens.JWT" %>
    <%@ page import="no.hvl.dat152.obl3.blog.tokens.JWTHandler" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Comment Page</title>
</head>
<body>

<%	

	try{	
		String id_token = RequestHelper.getCookieValue(request, "id_token");
		String user = "";
		String role = "";
		String exp = "";
		String iat = "";
		
		String pubkeypath = application.getRealPath("/WEB-INF/");
		boolean validSession = RequestHelper.isLoggedInSSO(id_token);
		
		if(!validSession){
			request.setAttribute("message", "Session timed out or invalid SSO auth token");
			request.getRequestDispatcher("index.jsp").forward(request, response);
			
		} else {
			
			if(id_token != null){
				JWT jwt = JWTHandler.getJWT(id_token);
				role = jwt.getRole();
				user = jwt.getUsername();
				exp = jwt.getExp();
				iat = jwt.getIat();
			} 

			if (RequestHelper.isLoggedInSSO(id_token, pubkeypath)) { 
			
			String fpath = application.getRealPath("/WEB-INF/blogdb.txt");
			
			String button = request.getParameter("submit");
			
			if(null != button){
				if(button.equals("Delete Comments")){
					Util.deleteComments(fpath);
				}
			}

			String comment = request.getParameter("comment");
			if(comment != null){
				
				Util.saveComments(fpath, comment, user);
				
			}
			List<String> comments = Util.getComments(fpath);
			for(String comt: comments){
				out.println("<br>"+comt+"</br>");
			} %>
			<p>
			<form action="" method="post">
				<table>
				<tr><td>Comment</td><td><textarea name=comment rows=4 cols=40></textarea></td></tr>
				<tr><td><input type=submit name=submit value="Post Comment"></td></tr>
				</table>
			</form>
			
			<%
			//String role = request.getSession().getAttribute("role").toString();
			if(role.equals(Role.ADMIN.name())){ 
				
			%>
			<form action="" method="post">
				<input type=submit name=submit value="Delete Comments">
			</form>
			<% 
			}
			%>
			
			<p><b>You are logged in as <%=user %>. <a href="logout">Log out</a></b></p>
			<form method="post" action=<%=Constants.IDP_LOGOUT_ENDPOINT %> >
			<input type="hidden" name="client_id" value=<%=Constants.CLIENT_ID %>>
			<input type="hidden" name="redirect_uri" value=<%=Constants.CALLBACK_ADDRESS %>>
			<input type=submit value="SSO:Logout">
			</form>
<%			} else {
	request.setAttribute("message", "Exception! Error logging into the app.");
	request.getRequestDispatcher("index.jsp").forward(request, response);
}
		}

	} catch(Exception e){
		request.setAttribute("message", "Exception! Error logging into the app."+e.getMessage());
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}


	
%>

</body>
</html>