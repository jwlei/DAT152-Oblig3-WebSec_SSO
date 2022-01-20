package no.hvl.dat152.obl3.blog.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.hvl.dat152.obl3.blog.Constants;

/**
 * Servlet implementation class SSOForwarder
 */
@WebServlet("/sso")
public class SSOForwarder extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SSOForwarder() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String clientId = request.getParameter("client_id");
		Constants.CLIENT_ID = clientId;
		
		String scope = request.getParameter("scope");
		String response_type = request.getParameter("response_type");
		String state = request.getParameter("state");
		String redirect_uri = request.getParameter("redirect_uri");
		
		String idp_endpoint = Constants.IDP_AUTH_ENDPOINT;
		String ssourl = idp_endpoint+"?client_id="+clientId+"&scope="+scope+"&response_type="+response_type+"&state="+state+
				"&redirect_uri="+redirect_uri;
		
		// add the auth token to be forwarded to the IdP for verification
		Cookie id_token = RequestHelper.getCookie(request, "id_token");
		if(id_token != null) {
            id_token.setPath(Constants.IDP_PATH);
            response.addCookie(id_token);
            response.sendRedirect(ssourl);
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
