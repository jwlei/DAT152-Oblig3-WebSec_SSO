package no.hvl.dat152.obl3.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SearchItemDAO {

  public List<SearchItem> getSearchHistoryLastFive() {
    String sql = "SELECT * FROM SecOblig.History ORDER BY datetime DESC";
    // LIMIT 5
    // Derby lacks LIMIT
    return getSearchItemList(sql,5);
  }

  public List<SearchItem> getSearchHistoryForUser(String username) {
//	    String sql = "SELECT * FROM SecOblig.History " 
//	        + "WHERE username = '" + username 
//	        + "' ORDER BY datetime DESC";
	  
	  String PSquery = "SELECT * FROM SecOblig.History WHERE username = ? ORDER BY datetime DESC";
	    //  LIMIT 50
	    // Derby lacks LIMIT
	  
	   // return getSearchItemList(sql,50);
	  List<SearchItem> result = new ArrayList<SearchItem>();

		Connection c = null;
		Statement s = null;
		ResultSet r = null;
		PreparedStatement ps = null;

		try {
			c = DatabaseHelper.getConnection();
			ps = c.prepareStatement(PSquery);
			ps.setString(1, username);
			ps.setMaxRows(50);
			r = ps.executeQuery();

			while (r.next()) {
				SearchItem item = new SearchItem(r.getTimestamp("datetime"), r.getString("username"),
						r.getString("searchkey"));
				result.add(item);
			}

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			DatabaseHelper.closeConnection(r, s, c);
		}

		return result;
	  }
  
  public List<SearchItem> getSearchHistoryForUser(String username, String sortkey) {
    String sql = "SELECT * FROM SecOblig.History " 
        + "WHERE username = '" + username 
        + "' ORDER BY "+sortkey+" ASC";
    //  LIMIT 50
    // Derby lacks LIMIT
    //return getSearchItemList(sql,50);
	  
	  String PSquery = "SELECT * FROM SecOblig.History WHERE username = ? ORDER BY  ? ASC"; 
    List<SearchItem> result = new ArrayList<SearchItem>();

	Connection c = null;
	Statement s = null;
	ResultSet r = null;
	PreparedStatement ps = null;

	try {
		c = DatabaseHelper.getConnection();
		ps = c.prepareStatement(PSquery);
		ps.setString(1, username);
		ps.setString(2,sortkey);
		ps.setMaxRows(50);
		r = ps.executeQuery();

		while (r.next()) {
			SearchItem item = new SearchItem(r.getTimestamp("datetime"), r.getString("username"),
					r.getString("searchkey"));
			result.add(item);
		}

	} catch (Exception e) {
		System.out.println(e);
	} finally {
		DatabaseHelper.closeConnection(r, ps, c);
	}

	return result;
  }

  private List<SearchItem> getSearchItemList(String sql,Integer limit) {

    List<SearchItem> result = new ArrayList<SearchItem>();

    Connection c = null;
    Statement s = null;
    ResultSet r = null;
    //PreparedStatement ps = null;

    try {        
      c = DatabaseHelper.getConnection();
      s = c.createStatement();
      if (limit > 0) s.setMaxRows(limit);
      r = s.executeQuery(sql);

      while (r.next()) {
        SearchItem item = new SearchItem(
            r.getTimestamp("datetime"),
            r.getString("username"),
            r.getString("searchkey")
            );
        result.add(item);
      }

    } catch (Exception e) {
    	e.printStackTrace();
      //System.out.println(e);
    } finally {
      DatabaseHelper.closeConnection(r, s, c);
    }

    return result;
  }

  public void saveSearch(SearchItem search) {

//    String sql = "INSERT INTO SecOblig.History VALUES (" 
//        + "'" + search.getDatetime()  + "', "
//        + "'" + search.getUsername()  + "', "
//        + "'" + search.getSearchkey() + "')";
		String PSquery = "INSERT INTO SecOblig.History VALUES (?, ?, ?)";

		Connection c = null;
		ResultSet r = null;
		PreparedStatement ps = null;

		try {

			c = DatabaseHelper.getConnection();
			ps = c.prepareStatement(PSquery);
			ps.setString(1, search.getDatetime().toString());
			ps.setString(2, search.getUsername());
			ps.setString(3, search.getSearchkey());

			ps.execute();

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			DatabaseHelper.closeConnection(r, ps, c);
		}
	}


}
