package lucene;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class Search {
	static String indexDir = GetProperties.getproperties("indexDir");
	static Path indexDirPath = FileSystems.getDefault().getPath(indexDir);
	static Analyzer analyzer = new IKAnalyzer(false);
	public static void search(String searchStr) throws IOException, ParseException {
		// TODO Auto-generated method stub
		Directory directory = FSDirectory.open(indexDirPath);
		DirectoryReader ireader = DirectoryReader.open(directory);
		IndexSearcher isearcher = new IndexSearcher(ireader);
		QueryParser parser = new QueryParser("content", analyzer);
	    Query query = parser.parse(searchStr);
	    ScoreDoc[] hits = isearcher.search(query, 100).scoreDocs;
//	    System.out.println(hits.length);
	    for (int i = 0; i < hits.length; i++) {
	        Document hitDoc = isearcher.doc(hits[i].doc);
//	        System.out.println(hits[i].score);
	      }
	      ireader.close();
	      directory.close();
	}
	public static void main(String[] args) throws IOException, ParseException {
		Search.search("Íõ´ºÓî");
	}
}
