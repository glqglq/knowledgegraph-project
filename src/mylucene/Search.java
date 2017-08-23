package mylucene;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;

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

public class Search {
	static String indexDir = GetProperties.getproperties("indexDir");
	static Path indexDirPath = FileSystems.getDefault().getPath(indexDir);
	static Analyzer analyzer = new IKAnalyzer(false);
	public static JSONArray search(String searchStr,int first,int last) throws IOException, ParseException {
		Directory directory = FSDirectory.open(indexDirPath);
		DirectoryReader ireader = DirectoryReader.open(directory);
		IndexSearcher isearcher = new IndexSearcher(ireader);
		
		ArrayList<SearchResult> list=new ArrayList<SearchResult>();
		MultiFieldQueryParser parser = new MultiFieldQueryParser(new String[]{"content","filename"}, analyzer);
	    Query query = parser.parse(searchStr);
	    ScoreDoc[] hits = isearcher.search(query, 100).scoreDocs;
//	    System.out.println(hits.length);
	    
	    for (int i = first; i < Math.min(last,hits.length); i++) {
	        SearchResult t = new SearchResult();
	        t.setScore(hits[i].score);
	        t.setFileName(isearcher.doc(hits[i].doc).get("filename"));
	        t.setContent(isearcher.doc(hits[i].doc).get("content"));
	        list.add(t);
//	        System.out.println(isearcher.doc(hits[i].doc).get("filename"));
	    }
	    ireader.close();
	    directory.close();
	    return JSONArray.fromObject(list);
	}
	public static void main(String[] args) throws IOException, ParseException {
		Search.search("Íõ´ºÓî",0,100);
	}
}
