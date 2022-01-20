package no.hvl.dat152.obl3.blog.servlets;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.hvl.dat152.obl3.blog.tokens.JWTHandler;

public class RequestHelper {

	public static String getCookieValue(HttpServletRequest request,
			String cookieName) {

		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie c : cookies) {
				if (c.getName().equals(cookieName.trim())) {
					return c.getValue();
				}
			}
		}
		return null;
	}
	
	public static Cookie getCookie(HttpServletRequest request,
			String cookieName) {

		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie c : cookies) {
				if (c.getName().equals(cookieName.trim())) {
					return c;
				}
			}
		}
		return null;
	}
	
	public static boolean isLoggedIn(HttpServletRequest request) {
		return request.getSession().getAttribute("user") != null;
	}
	
	public static boolean isLoggedInSSO(String id_token) {
		return id_token != null;
	}
	
	public static boolean isLoggedInSSO(String id_token, String wpath) {
		
		return JWTHandler.verifyJWTAsymmetric(id_token, wpath);
		
	}
}
