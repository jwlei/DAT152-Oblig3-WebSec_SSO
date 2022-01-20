package no.hvl.dat152.obl3.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.hvl.dat152.obl3.database.AppUser;
import no.hvl.dat152.obl3.database.AppUserDAO;
import no.hvl.dat152.obl3.dictionary.DictionaryDAO;
import no.hvl.dat152.obl3.idp.oauth.utility.Constants;
import no.hvl.dat152.obl3.idp.oauth.utility.OpenIDUser;
import no.hvl.dat152.obl3.util.Role;
import no.hvl.dat152.obl3.util.TokenSingleton;
import no.hvl.dat152.obl3.util.Validator;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		String client_id = request.getParameter(Constants.CLIENT_ID);	// check if we have a request for SSO
		if(client_id != null) {
			if (RequestHelper.isLoggedInOIDC(request, client_id)) {
				// forward request to authorization endpoint
				request.getRequestDispatcher("authorizehelper").forward(request, response);
				return;
			} else {
				request.getRequestDispatcher("login.jsp").forward(request, response);
				return;
			}
		} else if (RequestHelper.isLoggedIn(request)) {
			request.getRequestDispatcher("searchpage").forward(request, response);
			return;
		} else {
			request.getRequestDispatcher("login.jsp").forward(request, response);
			return;
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String client_id = request.getParameter(Constants.CLIENT_ID);	// check if we have a request for SSO
		
		if(client_id != null) {
			if(client_id.isEmpty()) {
				String redirect_uri = request.getParameter(Constants.REDIRECT_URI);
				response.sendRedirect(redirect_uri+"?ssologgedin=failed");
			} else
				openIDAuthentication(request, response, client_id);
			
		} else {
			
			boolean successfulLogin = false;

			String username = Validator.validString(request.getParameter("username"));
			String password = Validator.validString(request.getParameter("password"));

			if (username != null && password != null) {
	
				AppUserDAO userDAO = new AppUserDAO();
				AppUser authUser = userDAO.getAuthenticatedUser(username, password);
				
				//CSRF - Creating token on login of user
				if (authUser != null) {
					successfulLogin = true;
					request.getSession().setAttribute("user", authUser);
					request.getSession().setAttribute("anticsrf", userDAO.generateAntiCsrf());
					
					System.out.println("Generated CSRF Token for USER " + authUser);
					request.getSession().setAttribute("updaterole", "");
	
					//Admin
					if (authUser.getRole().equals(Role.ADMIN.toString())) {
						List<String> usernames = userDAO.getUsernames();
						request.getSession().setAttribute("usernames", usernames);
						request.getSession().setAttribute("anticsrf", userDAO.generateAntiCsrf());
						
							if (authUser.getRole().equals(Role.ADMIN.toString())) {
									System.out.println("Generated CSRF Token for ADMIN " + authUser);
							}
							request.getSession().setAttribute("updaterole", "<a href=\"updaterole.jsp\">Update Role</a>");
							}
					} else {
						response.sendRedirect("login");
						return;
				}
			}
	
			if (successfulLogin) {
				response.sendRedirect("searchpage");
	
			} else {
				request.setAttribute("message", "Username " + username + ": Login failed!");
				request.getRequestDispatcher("login.jsp").forward(request, response);
			}
		}
	}
	

	private void openIDAuthentication(HttpServletRequest request, HttpServletResponse response, 
			String clientId) throws IOException, ServletException {
		/**
		 * Authentication: OpenID specification (parameters)
		 * response_type
		 * scope
		 * client_id
		 * state (OPTIONAL)
		 * redirect_uri
		 */
			
		OpenIDUser oidc_user = createOpenIDUser(request);
		System.out.println("OIDC ENTERED");
		boolean successfulLogin = login(request, response);
		
		if(successfulLogin) {
			
			AppUser appUser = (AppUser) request.getSession().getAttribute(Constants.USER);
			request.getSession().setAttribute(Constants.OIDC_USER, oidc_user);
			TokenSingleton.Instance().addOpenIDUser(clientId, oidc_user);
			TokenSingleton.Instance().addRole(clientId, appUser.getRole());
		
			
			// redirect to authorization endpoint
			request.getRequestDispatcher("authorize").forward(request, response);
		} else {
			response.sendRedirect(oidc_user.getRedirectURI()+"?ssologgedin=failed");
		}		
		
	}
	
	private OpenIDUser createOpenIDUser(HttpServletRequest request) {
		
		String response_type = request.getParameter(Constants.RESPONSE_TYPE);
		String scope = request.getParameter(Constants.SCOPE);
		String client_id = request.getParameter(Constants.CLIENT_ID);
		String redirect_uri = request.getParameter(Constants.REDIRECT_URI);
		String state = request.getParameter(Constants.STATE);
		
		// handle client's scope
		String[] scopes = scope.split(" ");
		List<String> consents = new ArrayList<>();
		for(String _scope : scopes) {
			consents.add(_scope);
		}
		OpenIDUser oidc_user = new OpenIDUser(client_id);
		oidc_user.setConsents(consents);
		oidc_user.setResponseType(response_type);
		oidc_user.setRedirectURI(redirect_uri);
		
		if(state != null)
			oidc_user.setState(state);
		
		return oidc_user;
	}
	
	private boolean login(HttpServletRequest request,
			HttpServletResponse response) {
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		boolean successfulLogin = false;

		if (username != null && password != null) {

			AppUserDAO userDAO = new AppUserDAO();
			AppUser authUser = userDAO.getAuthenticatedUser(username, password);

			if (authUser != null) {
				successfulLogin = true;
				request.getSession().setAttribute(Constants.USER, authUser);
				request.getSession().setAttribute("updaterole", "");
			
				// set dictionary url in a cookie
				Cookie dicturl = new Cookie("dicturl",DictionaryDAO.DEFAULT_DICT_URL);
				dicturl.setMaxAge(30*60);
				response.addCookie(dicturl);
				
				// admin issues
				if(authUser.getRole().equals(Role.ADMIN.name())) {
					List<String> usernames = userDAO.getUsernames();
					request.getSession().setAttribute("usernames", usernames);
					request.getSession().setAttribute("updaterole", "<a href=\"updaterole.jsp\">Update Role</a>");
				}
			}
		}
		
		return successfulLogin;
	}
}