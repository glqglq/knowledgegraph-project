package web;

import java.io.IOException;
import java.io.PrintWriter;

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
		String searchStr = request.getParameter("searchstr");
//		 System.out.println(searchStr+Integer.valueOf(request.getParameter("first")).intValue()
//		 + Integer.valueOf(request.getParameter("last")).intValue());
		System.out.println(searchStr);
		// ±à½âÂë
		JSONArray json;
		json = Search.search(searchStr, (Integer.valueOf(request.getParameter("page_id")).intValue() - 1) * 10,
				Integer.valueOf(request.getParameter("page_id")).intValue() * 10);
		System.out.println(json);
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.write(json.toString());
		out.close();
//		out.println("<script>window.location.href = \"url\"</script>");
	}

}
