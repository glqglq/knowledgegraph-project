package mylucene;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import mybean.SearchResult;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Search {
//	static String indexDir = "C:\\Users\\LuckyGong\\Documents\\GitHub\\knowledgegraph-project\\index";
	static String indexDir = GetProperties.getproperties("indexDir");
	static Path indexDirPath = FileSystems.getDefault().getPath(indexDir);

	public static JSONArray search(String searchStr, int first, int last) throws IOException {
//		System.out.println(indexDir);
//		System.out.println(searchStr);
		long startMili=System.currentTimeMillis();
		
		Analyzer analyzer = new IKAnalyzer(false);
		Directory directory = FSDirectory.open(indexDirPath);
		DirectoryReader ireader = DirectoryReader.open(directory);
		IndexSearcher isearcher = new IndexSearcher(ireader);
		
		ArrayList<SearchResult> list = new ArrayList<SearchResult>();
		MultiFieldQueryParser parser = new MultiFieldQueryParser(new String[] { "content", "filename" }, analyzer);
		Query query;
		try {
			query = parser.parse(searchStr);
			ScoreDoc[] hits = isearcher.search(query, 100).scoreDocs;
//			 System.out.println(hits.length);

			for (int i = first; i < Math.min(last, hits.length); i++) {
				SearchResult t = new SearchResult();
				t.setScore(hits[i].score);
				t.setFileName(isearcher.doc(hits[i].doc).get("filename"));
				t.setContent(isearcher.doc(hits[i].doc).get("content"));
				list.add(t);
				// System.out.println(isearcher.doc(hits[i].doc).get("filename"));
			}
			JSONObject jb = new JSONObject();
			Map rowData = new HashMap();
			rowData.put("item_count", hits.length);
			
		    rowData.put("page_count", Math.ceil(hits.length * 1.0/ 10));
		    long endMili=System.currentTimeMillis();
		    rowData.put("duration", (endMili-startMili)*1.0/1000);
		    jb.accumulateAll(rowData);
			JSONArray json = JSONArray.fromObject(list);
			json.add(jb);
//			System.out.println(JSONArray.fromObject(list).toString());
			return json ;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			ireader.close();
			analyzer.close();
			directory.close();
		}
		return JSONArray.fromObject(list);
	}

	public static void main(String[] args) throws IOException, ParseException {
		Search.search("Íõ´ºÓî", 0, 100);
	}
}
