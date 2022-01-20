package no.hvl.dat152.obl3.util;

import org.apache.commons.lang3.StringEscapeUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class Validator {
	
	//CSRF
	public static boolean csrfValidity(HttpServletRequest r) {
        String a = "";
        try {
            a = (String) r.getSession().getAttribute("anticsrf");
        } catch (NullPointerException e) {
            return true;
        	}
        String b = r.getParameter("anticsrf");
        return !a.equals(b);
    }

	public static String validString(String parameter) {

		//return parameter != null ? parameter : "null"; OLD
		
		//Uses apache commons stringescapeutils to escape special characters when validating a string
		return parameter != null ? StringEscapeUtils.escapeHtml4(parameter) : "null";
	}

	
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
}

