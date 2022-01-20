/**
 * 
 */
package no.hvl.dat152.obl3.blog;

/**
 * @author tdoy
 *
 */
public class Constants {

	public static String CLIENT_ID = "";			// this is assigned to the client by the Identity Provider
	//public static String CLIENT_SECRET = "";		// this is issued to the client by the identity Provider during registration
	
	public static final String STATE = "abcdef";		// this should be a secure random number (not used in this example)
	
	public static final String IDP_AUTH_ENDPOINT = "http://localhost:9092/DAT152WebSearch/login";
	public static final String IDP_LOGOUT_ENDPOINT = "http://localhost:9092/DAT152WebSearch/logout";
	public static final String IDP_TOKEN_ENDPOINT = "http://localhost:9092/DAT152WebSearch/token";
	public static final String IDP_USERCLAIMS_ENDPOINT = "http://localhost:9092/DAT152WebSearch/userinfo";
	public static final String IDP_REGISTER_ENDPOINT = "http://localhost:9092/DAT152WebSearch/register";
	
	public static final String IDP_PATH = "/DAT152WebSearch";
	
	public static final String CALLBACK_ADDRESS = "http://localhost:9090/blogapp/callback";
	
}
