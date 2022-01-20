package no.hvl.dat152.obl3.controller;


import no.hvl.dat152.obl3.database.SearchItem;
import no.hvl.dat152.obl3.database.SearchItemDAO;
import no.hvl.dat152.obl3.dictionary.DictionaryDAO;
import no.hvl.dat152.obl3.util.Validator;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet("/dosearch")
public class SearchResultServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final String DEFAULT_DICT_URL = DictionaryDAO.DEFAULT_DICT_URL;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		if (no.hvl.dat152.obl3.controller.RequestHelper.isLoggedIn(request)) {

			String dicturl = RequestHelper.getCookieValue(request, "dicturl");
			if (dicturl == null) {
				dicturl = DEFAULT_DICT_URL;
			}

			String user = Validator.validString(request.getParameter("user"));
			String searchkey = Validator.validString(request.getParameter("searchkey"));
			
			//XSS
			searchkey = Jsoup.clean(searchkey, Safelist.basic());
			
			//Broken authentication/Weak session management
			//We check t hat the logged in username is the same user that runs the session
			if (!RequestHelper.getLoggedInUsername(request).equals(user)) {
                response.sendRedirect("searchpage");
                return;
			}

			Timestamp datetime = new Timestamp(new Date().getTime());
			SearchItem search = new SearchItem(datetime, user, searchkey);
			
			SearchItemDAO searchItemDAO = new SearchItemDAO();
			searchItemDAO.saveSearch(search);
			DictionaryDAO dict = new DictionaryDAO(dicturl);

			List<String> foundEntries = new ArrayList<String>();
			try {
				foundEntries = dict.findEntries(searchkey);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			request.setAttribute("searchkey", searchkey);
			request.setAttribute("result", foundEntries);
			request.getRequestDispatcher("searchresult.jsp").forward(request,
					response);
		} else {
			request.getSession().invalidate();
			request.getRequestDispatcher("index.html").forward(request,
					response);
		}
	}
}
