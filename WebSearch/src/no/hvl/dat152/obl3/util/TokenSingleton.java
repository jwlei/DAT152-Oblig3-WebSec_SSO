/**
 * 
 */
package no.hvl.dat152.obl3.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.hvl.dat152.obl3.idp.oauth.utility.OpenIDUser;
import no.hvl.dat152.obl3.idp.oauth.utility.TokenID;

/**
 * @author tdoy
 *
 */
public class TokenSingleton {

	
	private static TokenSingleton INSTANCE = null;
	
	private static Map<String, String> authcode;
	private static Map<String, String> consents;
	//private static Map<String, String> refreshTokens;
	private static Map<String, OpenIDUser> openIDUserMap;
	private static List<String> refreshtokens;
	private static Map<String, String> roles;
	private static Map<String, String> userClientIDs;
	
	private TokenSingleton() {
		
	}
	
	public static TokenSingleton Instance() {
		
		if(INSTANCE == null) {
			authcode = new HashMap<>();
			consents = new HashMap<>();
			roles = new HashMap<>();
			userClientIDs = new HashMap<>();
			//refreshTokens = new HashMap<>();
			openIDUserMap = new HashMap<>();
			refreshtokens = new ArrayList<>();
			
			INSTANCE = new TokenSingleton();
		}
		
		return INSTANCE;
	}

	
	public Map<String, OpenIDUser> getOpenIDUserMap(){
		return openIDUserMap;
	}
	
	public void addOpenIDUser(String client, OpenIDUser openIDUser) {
		openIDUserMap.put(client, openIDUser);
	}
	
	public OpenIDUser getOpenIDUser(String client) {
		return openIDUserMap.get(client);
	}
	
	public void deleteOpenIDUser(String client) {
		openIDUserMap.remove(client);
	}

	
	public Map<String, String> getAuthCode(){
		return authcode;
	}
	
	public void addAuthCode(String clientid, String code) {
		authcode.put(clientid, code);
	}
	
	public void deleteAuthCode(String client) {
		authcode.remove(client);
	}
	
	public Map<String, String> getConsents(){
		return consents;
	}
	
	public void addConsents(String clientid, String consent) {
		consents.put(clientid, consent);
	}
	
	public void deleteConsents(String client) {
		consents.remove(client);
	}
	
	public Map<String, String> getRoles(){
		return roles;
	}
	
	public void addRole(String clientid, String role) {
		roles.put(clientid, role);
	}
	
	public void deleteRole(String client) {
		roles.remove(client);
	}
	
	public Map<String, String> getUserClientIDs(){
		return userClientIDs;
	}
	
	public void addUserClientIDs(String clientid, String phone) {
		userClientIDs.put(clientid, phone);
	}
	
	public void deleteUserClientIDs(String clientid) {
		userClientIDs.remove(clientid);
	}
	
	public boolean refreshTokenExist(String refreshtoken) {
		return refreshtokens.indexOf(refreshtoken) > 0;
	}
	
	public boolean removeRefreshToken(String refreshtoken) {
		return refreshtokens.remove(refreshtoken);
	}
	
	public void addRefreshToken(String token) {
		refreshtokens.add(token);
	}
}
