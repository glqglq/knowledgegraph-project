package mylucene;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class CreateIndex {
	static String indexDir = GetProperties.getproperties("indexDir");
	static String dateDir = GetProperties.getproperties("dateDir");
	static String databaseIp = GetProperties.getproperties("databaseIp");
	static int databasePort = Integer.parseInt(GetProperties.getproperties("databasePort"));
	static String databaseName = GetProperties.getproperties("databaseName");
	static String collectionName = GetProperties.getproperties("collectionName");

	static Analyzer analyzer = new IKAnalyzer(false);
	static IndexWriterConfig config = new IndexWriterConfig(analyzer);
	static File[] files = new File(dateDir).listFiles();
	static MongoClient mongoClient = new MongoClient(databaseIp, databasePort);
	static MongoDatabase mongoDatabase = mongoClient.getDatabase(databaseName);
	static MongoCollection<org.bson.Document> collection = mongoDatabase.getCollection(collectionName);

	public static void getAndSavePageContent(File[] files) throws IOException {
		List<org.bson.Document> documents = new ArrayList<org.bson.Document>();

		collection.drop();

		for (int i = 0; i < files.length; i++) {
			InputStream in = new FileInputStream(files[i]);
			String fileName = files[i].getName();
			if (fileName.endsWith("docx")) {
				XWPFDocument wdoc = new XWPFDocument(in);
				XWPFWordExtractor w = new XWPFWordExtractor(wdoc);
				documents.add(new org.bson.Document("filename", fileName).append("content", w.getText()));
				w.close();
			} else if (fileName.endsWith("doc")) {
				WordExtractor w = new WordExtractor(in);
				documents.add(new org.bson.Document("filename", fileName).append("content", w.getText()));
				w.close();
			}
		}
		collection.insertMany(documents);
	}

	public static void createIndex() throws IOException {
		try {
			// ����Ŀ¼����
			Directory directory = FSDirectory.open(Paths.get(indexDir));
			IndexWriter iwriter = new IndexWriter(directory, config);
			MongoCursor<org.bson.Document> cursor = collection.find().iterator();
			while (cursor.hasNext()) {
				org.bson.Document bsonDoc = org.bson.Document.parse(cursor.next().toJson());
				Document doc = new Document();
//				System.out.println(bsonDoc.getString("filename"));
				doc.add(new Field("filename", bsonDoc.getString("filename"), TextField.TYPE_STORED));
				doc.add(new Field("content", bsonDoc.getString("content"), TextField.TYPE_STORED));
				// д��IndexWriter
				iwriter.addDocument(doc);
			}
			iwriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		CreateIndex.getAndSavePageContent(files);
		CreateIndex.createIndex();
	}
}
