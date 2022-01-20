package no.hvl.dat152.obl3.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import no.hvl.dat152.obl3.database.AppUser;
import no.hvl.dat152.obl3.idp.oauth.utility.Constants;
import no.hvl.dat152.obl3.idp.oauth.utility.JWTHandler;
import no.hvl.dat152.obl3.util.TokenSingleton;

public class RequestHelper {

	public static String getCookieValue(HttpServletRequest request,
			String cookieName) {

		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie c : cookies) {
				if (c.getName().equals(cookieName)) {
					return c.getValue();
				}
			}
		}
		return null;
	}
	
	public static boolean isLoggedIn(HttpServletRequest request) {
		return request.getSession().getAttribute("user") != null;
	}
	//Broken authentication/Weak session management
	//Get the logged in username so that we can check it up against the session in use
	public static String getLoggedInUsername(HttpServletRequest request) {
        return ((AppUser) request.getSession().getAttribute("user")).getUsername();
    }
	
	
	public static boolean isLoggedInOIDC(HttpServletRequest request, String clientid) {
		// get the user's phone number
		String user_phone = TokenSingleton.Instance().getUserClientIDs().get(clientid);

		// does this user still have a valid session with the IdP?
		String session_phone = "";
		try {
			session_phone = ((AppUser) request.getSession().getAttribute(Constants.USER)).getMobilephone();
		}catch(Exception e) {
			//
		}
		
		// we check whether the id_token is valid
		boolean auth_token_valid = false;
		String keypath = request.getServletContext().getRealPath("WEB-INF/");
		String id_token = getCookieValue(request, "id_token");
		
		if(id_token != null) {
			auth_token_valid = JWTHandler.verifyJWTAsymmetric(id_token, keypath);
		}

		return session_phone.equals(user_phone) && auth_token_valid;

	}

}
