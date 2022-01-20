package no.hvl.dat152.obl3.blog.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.hvl.dat152.obl3.blog.database.User;
import no.hvl.dat152.obl3.blog.database.UserXMLDbLogic;

/**
 * Servlet implementation class Login
 */
@WebServlet("/login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
		
		String username = request.getParameter("username");
		String pwd = request.getParameter("password");
		
		if(!username.equals("") && !pwd.equals("")) {
			/**
			 * use xml file located on the server to store users - weak
			 */
			String dbpath = getServletContext().getRealPath("WEB-INF/usersdb.xml");
			UserXMLDbLogic xmldb = new UserXMLDbLogic(dbpath);
			User user = xmldb.authenticateWithSaltUser(username, pwd);
			
			if(user != null) {
				request.getSession().setAttribute("user", user);
				request.getSession().setAttribute("loggeduser", user.getUserId());
				
				response.sendRedirect("blog2.jsp");
			} else {
				request.setAttribute("message", "Error logging into the app. Register as a new user...");
				request.getRequestDispatcher("index.jsp").forward(request, response);
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
