package web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.queryparser.classic.ParseException;

import mylucene.Search;
import net.sf.json.JSONArray;

/**
 * Servlet implementation class SearchServlet
 */
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 3060852870298693421L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String searchStr = new String(request.getParameter("searchstr").getBytes("iso8859-1"), "gbk");
//		 System.out.println(searchStr+
//		 Integer.valueOf(request.getParameter("first")).intValue()
//		 + Integer.valueOf(request.getParameter("last")).intValue());
		// ±à½âÂë
//		System.out.println(searchStr);
		JSONArray json;
		json = Search.search(searchStr, Integer.valueOf(request.getParameter("first")).intValue(),
				Integer.valueOf(request.getParameter("last")).intValue());
		System.out.println(json);
		response.setCharacterEncoding("GB2312");
		response.getWriter().write(json.toString());
		System.out.println(json.toString());
	}

}
