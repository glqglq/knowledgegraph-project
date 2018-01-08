package util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

<<<<<<< HEAD
import doc.structure.Doc;
=======
import extract.Doc;
>>>>>>> 6e00568378d0718f1093656e8ab62d0e836215dd

public class Terminology {
	public static void getTerminologyFromTxt(Doc doc) {
		for(String line:doc.getContent()) {
//			System.out.println(line.split("\\s+").length);
			if(line.split("\\s+").length >= 2)
				System.out.println(line);
		}
	}
	public static void extractWord() {
		
	}
	public static void main(String[] args) {
		List<String> words = new ArrayList<String>();
		Scanner sc = new Scanner(System.in);
		while(sc.hasNext()) {
			String t = sc.next();
			boolean flag = true;
			for(String word:words) {
				if(word.equals(t)) {flag = false;break;}
			}
			if(flag) words.add(t);
		}
		for(String word:words)
			System.out.println(word);
	}
}
