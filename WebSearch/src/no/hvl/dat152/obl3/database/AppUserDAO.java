package no.hvl.dat152.obl3.database;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

import no.hvl.dat152.obl3.util.Crypto;


public class AppUserDAO {

  public AppUser getAuthenticatedUser(String username, String password) {

    String hashedPassword = Crypto.generateMD5Hash(password);

  //  String sql = "SELECT * FROM SecOblig.AppUser" + " WHERE username = '" + username + "'"
  //  											+ " AND passhash = '" + hashedPassword + "'";
    
    String PSquery = "SELECT * FROM SecOblig.Appuser WHERE username = ? AND passhash = ?";
    
    
    AppUser user = null;

    Connection c = null;
   // Statement s = null;
    PreparedStatement ps = null;
    ResultSet r = null;

    try {        
    	c = DatabaseHelper.getConnection();
//		s = c.createStatement();
		ps = c.prepareStatement(PSquery);
		ps.setString(1, username);
		ps.setString(2, hashedPassword);
		r = ps.executeQuery();
//		r = s.executeQuery(sql);

      if (r.next()) {
        user = new AppUser(
            r.getString("username"),
            r.getString("passhash"),
            r.getString("firstname"),
            r.getString("lastname"),
            r.getString("mobilephone"),
            r.getString("role"),
            r.getString("clientId")
            );
      }

    } catch (Exception e) {
      System.out.println(e);
    } finally {
      DatabaseHelper.closeConnection(r, ps, c);
    }

    return user;
  }
  
  public String getUserClientID(String mobilephone) {

    //String sql = "SELECT clientId FROM SecOblig.AppUser" 
      //  + " WHERE mobilephone = '" + mobilephone + "'";
    
    String PSquery = "SELECT clientId FROM SecOblig.Appuser WHERE mobilephone = ?";
    
    String clientID = null;

    Connection c = null;
    PreparedStatement ps = null;
    ResultSet r = null;

    try {        
      c = DatabaseHelper.getConnection();
      ps = c.prepareStatement(PSquery);
      ps.setString(1, mobilephone);
      //s = c.createStatement();       
      r = ps.executeQuery();

      if (r.next()) {
        clientID = r.getString("clientId");
      }

    } catch (Exception e) {
      System.out.println(e);
    } finally {
      DatabaseHelper.closeConnection(r, ps, c);
    }

    //System.out.println("ClientId: "+clientID);
    
    return clientID;
  }
  
  public boolean clientIDExist(String clientid) {

	   // String sql = "SELECT clientId FROM SecOblig.AppUser" 
	   //     + " WHERE clientId = '" + clientid + "'";
	  
	  String PSquery = "SELECT clientId FROM SecOblig.Appuser" + " WHERE clientId = ?";
	    
	    
	    String clientID = null;

	    Connection c = null;
	    PreparedStatement ps = null;
	    ResultSet r = null;

	    try {        
	      c = DatabaseHelper.getConnection();
	      ps = c.prepareStatement(PSquery);
	      ps.setString(1, clientid);
	      r = ps.executeQuery();

	      if (r.next()) {
	        clientID = r.getString("clientId");
	      }

	    } catch (Exception e) {
	      System.out.println(e);
	    } finally {
	      DatabaseHelper.closeConnection(r, ps, c);
	    }

	    //System.out.println("ClientId: "+clientID);
	    
	    return clientID != null;
	  }

  public boolean saveUser(AppUser user) {

	  String PSquery = "INSERT INTO SecOblig.AppUser VALUES (?, ?, ?, ?, ?, ?, ?)";

		Connection c = null;
		PreparedStatement ps = null;
		ResultSet r = null;

		try {
			c = DatabaseHelper.getConnection();
			ps = c.prepareStatement(PSquery);
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getPasshash());
			ps.setString(3, user.getFirstname());
			ps.setString(4, user.getLastname());
			ps.setString(5, user.getMobilephone());
			ps.setString(6, user.getRole());
			ps.setString(7, user.getClientID());
			int row = ps.executeUpdate();
			if (row >= 0) {
				return true;
			}
		} catch (Exception e) {
			System.out.println(e);
			return false;
		} finally {
			DatabaseHelper.closeConnection(r, ps, c);
		}

		return false;
  }
  
  public List<String> getUsernames() {
	  
	  List<String> usernames = new ArrayList<String>();
	  
	  String sql = "SELECT username FROM SecOblig.AppUser";

		    Connection c = null;
		    Statement s = null;
		    ResultSet r = null;

		    try {        
		      c = DatabaseHelper.getConnection();
		      s = c.createStatement();       
		      r = s.executeQuery(sql);

		      while (r.next()) {
		    	  usernames.add(r.getString("username"));
		      }

		    } catch (Exception e) {
		      System.out.println(e);
		    } finally {
		      DatabaseHelper.closeConnection(r, s, c);
		    }
	  
	  return usernames;
  }
  
  public boolean updateUserPassword(String username, String passwordnew) {
	  
	  String hashedPassword = Crypto.generateMD5Hash(passwordnew);
	  
	    //String sql = "UPDATE SecOblig.AppUser "
	    //		+ "SET passhash = '" + hashedPassword + "' "
	    //				+ "WHERE username = '" + username + "'";
	  
	  String PSquery = "UPDATE SecOblig.Appuser SET passhash= ? WHERE username = ?";
	  
	
	    Connection c = null;
	    PreparedStatement ps = null;
	    ResultSet r = null;
	
	    try {        
	      c = DatabaseHelper.getConnection();
	      ps = c.prepareStatement(PSquery); 
	      ps.setString(1,hashedPassword);
	      ps.setString(2,username);
	      int row = ps.executeUpdate();
	      System.out.println("Password update successful for "+username);
	      if(row >= 0)
	    	  return true;
	      
	    } catch (Exception e) {
	      System.out.println(e);
	      return false;
	    } finally {
	      DatabaseHelper.closeConnection(r, ps, c);
	    }
	    
	    return false;
  }
  
  public boolean updateUserRole(String username, String role) {

//	    String sql = "UPDATE SecOblig.AppUser "
//	    		+ "SET role = '" + role + "' "
//	    				+ "WHERE username = '" + username + "'";
//	
	  String PSquery = "UPDATE SecOblig.AppUser SET role = ? WHERE username = ?";
	  
	    Connection c = null;
	    PreparedStatement ps = null;
	    ResultSet r = null;
	
	    try {        
	      c = DatabaseHelper.getConnection();
	      ps = c.prepareStatement(PSquery);
	      ps.setString(1,role);
	      ps.setString(2,username);
	      
	      int row = ps.executeUpdate(PSquery);
	      System.out.println("Role update successful for "+username+" New role = "+role);
	      if(row >= 0)
	    	  return true;
	      
	    } catch (Exception e) {
	      System.out.println(e);
	      return false;
	    } finally {
	      DatabaseHelper.closeConnection(r, ps, c);
	    }
	    return false;
  }



  //CSRF
  public String generateAntiCsrf() {
      SecureRandom randomToken = new SecureRandom();
      byte bytetab[] = new byte[30];
      randomToken.nextBytes(bytetab);
      return DigestUtils.md5Hex(bytetab);
  }

}

