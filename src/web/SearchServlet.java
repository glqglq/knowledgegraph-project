package web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


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
		String searchStr = new String(request.getParameter("searchstr").getBytes("GB2312"),"UTF-8");
		System.out.println(searchStr+ Integer.valueOf(request.getParameter("first")).intValue()
				+ Integer.valueOf(request.getParameter("last")).intValue());
		//±à½âÂë
//		try {
//			JSONArray json = Search.search(request.getParameter("searchstr"),
//					Integer.getInteger(request.getParameter("first")),
//					Integer.getInteger(request.getParameter("last")));
//			response.setCharacterEncoding("GB2113");
//			response.getWriter().write(json.toString());
//			System.out.println(json.toString());
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
	}

}
