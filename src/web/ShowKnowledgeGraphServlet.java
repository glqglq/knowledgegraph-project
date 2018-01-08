package web;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import doc.structure.Doc;


import mylucene.Search;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class SearchServlet
 */
public class ShowKnowledgeGraphServlet extends HttpServlet {
	private static final long serialVersionUID = 3060852870298693421L;
	private static String getRandomColor() {
		String r,g,b;
		Random random = new Random();
		r = Integer.toHexString(random.nextInt(256));
		g = Integer.toHexString(random.nextInt(256));
		b = Integer.toHexString(random.nextInt(256));
		r = r.length() == 1?"0" + r:r;
		g = g.length() == 1?"0" + g:g;
		b = b.length() == 1?"0" + b:b;
		return "#" + r+g+b;
	}
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<Map.Entry<HashMap<String,String>,Double>> jaccardList = Doc.getjaccard();
		JSONObject graph = new JSONObject();
		JSONArray ja = new JSONArray();
		for(Map.Entry<HashMap<String,String>,Double> entry:jaccardList) {
			HashMap<String,String> hm = entry.getKey();
			Double value = entry.getValue();
			if(!hm.entrySet().iterator().hasNext()) continue;
			Map.Entry<String, String> words = hm.entrySet().iterator().next();
			String word1 = words.getKey();
			String word2 = words.getValue();
			JSONObject jo = new JSONObject();
			jo.put("sourceID", word1);
			jo.put("targetID", word2);
			jo.put("size", value);
			jo.put("attributes", new JSONObject());
			ja.add(jo);
			//if(ja.size() >= 100) break;//取前100个关系
		}
		graph.put("edges", ja);
		
		
		ja = new JSONArray();
		ArrayList<String> zbs = new ArrayList<String>();//装备词典
		File file = new File("C:\\\\Users\\\\dragon\\\\Desktop\\\\dict（去重后）.txt");
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while((tempString = reader.readLine()) != null) {
				zbs.add(tempString);
			}
		}catch(IOException e) {
			e.printStackTrace();
		}finally {
			if(reader != null)
				try {
					reader.close();
				}catch(IOException e) {}
		}
		
		for(String zb:zbs) {
			double size = 0;
			for(Map.Entry<HashMap<String,String>,Double> entry:jaccardList) {
				HashMap<String,String> hm = entry.getKey();
				Double value = entry.getValue();
				if(!hm.entrySet().iterator().hasNext()) continue;
				Map.Entry<String, String> words = hm.entrySet().iterator().next();
				String word1 = words.getKey();
				String word2 = words.getValue();
				if(word1.equals(zb) || word2.equals(zb)) 
					size += value.doubleValue();
			}
			if(size != 0) {
				JSONObject jo = new JSONObject();
				jo.put("attributes", new JSONObject());
				jo.put("id",zb);
				jo.put("label", zb);
				jo.put("size", size); //参数可以调，改变node大小
				jo.put("color", getRandomColor());
				ja.add(jo);				
			}
		}
		graph.put("nodes", ja);
		
		System.out.println(graph);
		
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.write(graph.toString());
		out.close();
	}
	
}
