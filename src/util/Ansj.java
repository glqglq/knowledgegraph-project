package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.DicAnalysis;
import org.ansj.splitWord.analysis.NlpAnalysis;

public class Ansj {
	public static String analyzeParagraph2str(String str) {
		/**
		 * 分词
		 * 
		 */
		Result res = NlpAnalysis .parse(str);
		List<Term> tlist = res.getTerms();
		StringBuilder sb = new StringBuilder();
		for (Term t : tlist) {
			if (t.getName().trim().isEmpty() || t.getNatureStr().equals("null")||t.getName().matches("[a-zA-Z0-9]+")||t.getName().equals("\n")) {
				continue;
			}
			sb.append(t.getName());
			sb.append("/");
//			sb.append(t.getNatureStr());
			sb.append(" ");
		}
		if (sb.length() == 0) {
			return "";
		}
		return sb.substring(0, sb.length() - 1);
	}
	
	public static String getKeyword(String title, String con) {
		/**
		 * 关键词提取
		 * 
		 */
		KeyWordComputer kwc = new KeyWordComputer(10);
		Collection<Keyword> result = kwc.computeArticleTfidf(title, con);
		System.out.println(result);
		return "";
	}
	
	public static void analyzeParagraph2Paragraph(String path) {
		File file=new File(path);
		File[] tempList = file.listFiles();

		for (int i = 0; i < tempList.length; i++) {
			   if (tempList[i].isFile()) {
				   try {
					   File out_file = new File("G:/33知识图谱/数据/33知识图谱所有数据_txt/py-NLP分词" + tempList[i].getName());
					   FileWriter fw = new FileWriter(file);;
					   BufferedWriter writer = new BufferedWriter(fw);
					   
					   BufferedReader reader = new BufferedReader(new FileReader(file));
					   StringBuilder sb = new StringBuilder();
					   String tempString = null;
					   while ((tempString = reader.readLine()) != null) {
						   sb.append(tempString);
					   }
					   writer.write(analyzeParagraph2str(sb.toString()));
					   writer.flush();
					   
					   reader.close();
					   writer.close();
					   fw.close();					   
				   }catch(Exception e) {
					   e.printStackTrace();
				   }
			   }
		}
	}
	public static void main(String[] args) {
		analyzeParagraph2Paragraph("G:\\33知识图谱\\数据\\33知识图谱所有数据_txt\\py");
//		Scanner sc = new Scanner(System.in);
//		while (sc.hasNext()) {
//			String line = sc.nextLine();
//			if (line.startsWith("END")) {
//				break;
//			}
//			String res = analyzeParagraph2str(line);
//			System.out.println(res);
//		}
//		sc.close();
//		
//		// for test
//		List<Term> slist = NlpAnalysis.parse("我是中国人 , ，习近平学习").getTerms();
//		for (Term t : slist) {
//			if (t.getName().trim().isEmpty() || t.getNatureStr().equals("null")) {
//				continue;
//			}
//			System.out.print(t.getName());
//			System.out.print("/");
//			System.out.print(t.getNatureStr());
//			System.out.print(" ");
//		}
//		System.out.println();
//		System.out.println(slist);
	}

}
