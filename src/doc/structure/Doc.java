package doc.structure;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Doc {
	public static final String CODE_PREFIX = "GJB";

	private static final Object[] String[] = null;
	
	private String title;
	private List<String> content;
	
	private String code;
	private String year;
	private String proposer;
	private String[] draftOrg; // 起草单位
	private String[] draftPeo; // 起草人
	private JSONObject docLevelObj;
	private List<String> reference = new ArrayList<String>();//引用文件
	
	private ArrayList<String> zbs = new ArrayList<String>();//装备词典
	
	private HashMap<String,ArrayList<String>> ss = new HashMap<String,ArrayList<String>>();//
	
	public Doc(String title, List<String> content) {
		this.title = title;
		this.content = content;
	}
	
	public List<String> getContent() {
		return content;
	}

	public void setContent(List<String> content) {
		this.content = content;
	}

	public void structure() {
		try {
			extractCode();//代码、年份
		}catch(Exception e) {
			System.out.println("抽取代码、年份错误，原因是" + e.getMessage());
			throw e;
		}
		try {
			extractProposer();//提案人
		}catch(Exception e) {
			System.out.println("抽取提案人错误，原因是" + e.getMessage());
			throw e;
		}
		try {
			extractDraftMember();//起草成员
		}catch(Exception e) {
			System.out.println("抽取起草成员错误，原因是" + e.getMessage());
			throw e;
		}
		try {
			extractEquipment();////设备
		}catch(Exception e) {
			System.out.println("抽取设备错误，原因是" + e.getMessage());
			throw e;
		}
		try {
			extractObj();//对象
		}catch(Exception e) {
			System.out.println("抽取对象json错误，原因是" + e.getMessage());
			throw e;
		}
		try {
			extractReference();//抽取引用文件
		}catch(Exception e) {
			System.out.println("抽取引用文件错误，原因是" + e.getMessage());
			throw e;
		}
	}
	
	private void extractCode() {
		/**
		 * 1.抽取军标编码，格式：GJB****
		 * 2.抽取年份，格式：2007
		 */
		String codeNum = title.split("\\s+")[0];
		code = CODE_PREFIX + " " + codeNum;
		
		for (String line : content) {
			if (line.isEmpty()) continue;
			if (line.startsWith(code)) {
				try {
					year = line.trim().split("—|－")[1];
				} catch (Exception e) {
					year = "";
				}
				break;
			}
		}
	}
	
	private void extractProposer() {
		/**
		 * 抽取军标提出单位
		 */
		Pattern pn = Pattern.compile("本标准由(.*?)提出");
		for (String line : content) {
			if (line.isEmpty()) continue;
			Matcher mt = pn.matcher(line);
			if (mt.find()) {
				proposer = mt.group(1);
				break;
			}
		}
	}
	
	private void extractDraftMember() {
		/**
		 * 抽取军标起草单位、起草人
		 */
		Pattern pnOrg = Pattern.compile("本标准起草单位：(.*?)。");
		Pattern pnPeo = Pattern.compile("本标准主要起草人：(.*?)。");
		int matched = 2;
		for (String line : content) {
			if (line.isEmpty()) continue;
			Matcher mtOrg = pnOrg.matcher(line);
			Matcher mtPeo = pnPeo.matcher(line);
			if (mtOrg.find()) {
				draftOrg = mtOrg.group(1).split("、");
				matched--;
			}
			if (mtPeo.find()) {
				draftPeo = mtPeo.group(1).split("、");
				matched--;
			}
			if (matched == 0) {
				break;
			}
		}
		
		// 清空名字中空格
		int dpLen = draftPeo == null ? 0 : draftPeo.length;
		for (int i = 0; i < dpLen; i++) {
			if (draftPeo[i].contains(" ")) {
				draftPeo[i] = draftPeo[i].replaceAll("\\s+", "");
			}
		}

		//去掉“等”
		if (dpLen > 1) {
			if (draftPeo[dpLen - 1].endsWith("等")) {
				draftPeo[dpLen - 1] = draftPeo[dpLen - 1].substring(0, draftPeo[dpLen - 1].length() - 1);
			}
		}

	}
	
	private void extractReference() {
		/**
		 * 抽取引用文件
		 */
		int flag = -1;
		for(int i = 0;i < content.size();i++) {
			if(content.get(i).contains("2　引用文件")) {
				flag = i;
				break;
			}
		}
		boolean hasNext = true;
		if(flag >= 0) {
			for(int i = flag + 1;i < content.size();i++) {
				if(content.get(i).contains("GJB ")) {
					reference.add(content.get(i));
					hasNext = false;
				}else if(hasNext == false)
					break;
			}
		}
	}
	
	private void extractEquipment() {
		
	}
	
	private static boolean myIsLetter(char s) {
		int si = (int) s;
		if((si >= 97 && si <= 122))
			return true;
		return false;
	}
	private void extractObj() {
		List<String> levelKeyList = new LinkedList<String>();
		docLevelObj = new JSONObject();
		JSONObject obj = null;
		for (int i = 0; i < content.size(); i++) {
			if (content.get(i).isEmpty()) continue;
			if (Character.isDigit(content.get(i).charAt(0)) || (Doc.myIsLetter(content.get(i).charAt(0)) && content.get(i).charAt(1) == ')')) {
				String[] levelPair = content.get(i).split("\\s+|　+");
				//System.out.println(content.get(i) + levelPair.length);
				if (levelPair.length == 2) {
					JSONObject curObj = new JSONObject();
					curObj.element("level", levelPair[0]);
					curObj.element("title", levelPair[1]);
					int judge = -1;
					if (levelKeyList.size() > 0) {
						judge = prefixOn(levelKeyList.get(levelKeyList.size() - 1), levelPair[0]);
					}
					if (judge == 0 || judge == 1) {
						if (levelKeyList.size() > 0) {
							levelKeyList.remove(levelKeyList.size() - 1);
						} else {
							break;
						}
						if (judge == 0) {
							i--;
							continue;
						}
					}
					
					obj = getJSONObjByKeys(docLevelObj, levelKeyList);
					obj.element(levelPair[0], curObj);
					levelKeyList.add(levelPair[0]);
				}
			}
		}
	}
	
	public JSONObject getJSONObjByKeys(JSONObject obj, List<String> keys) {
		for (String key : keys) {
			obj = obj.getJSONObject(key);
		}
		return obj;
	}
	
	/**
	 * 判断前缀关系
	 * @param p1 默认p1小于p2
	 * @param p2
	 * @return 1:兄弟 2:父子 0:其他
	 */
	public static int prefixOn(String p1, String p2) {
		if(p2.length() < 2 || p1.length() < 2)
			return 0;
		if(p2.charAt(1) == ')' && !(p1.charAt(1) == ')'))
			return 2;
		else if(p2.charAt(1) == ')' && p1.charAt(1) == ')') {
			if(Character.isDigit(p2.charAt(0)) && Doc.myIsLetter(p1.charAt(0)))
				return 2;
			else if(Doc.myIsLetter(p2.charAt(0)) && Doc.myIsLetter(p1.charAt(0)))
				return 1;
			else if(Character.isDigit(p2.charAt(0)) && Character.isDigit(p1.charAt(0)))
				return 1;
		}
		int len1 = p1.length();
		int len2 = p2.length();
		int idx = 0;
		while (idx < len1 && idx < len2) {
			if (p1.charAt(idx) == p2.charAt(idx)) {
				idx++;
			} else {
				break;
			}
		}
		String s1 = p1.substring(idx);
		String s2 = p2.substring(idx);
		int int1 = -1, int2 = -1;
		try {
			int1 = Integer.parseInt(s1);
		} catch (Exception e) {}
		try {
			if (s2.startsWith(".")) {
				s2 = s2.substring(1);
			}
			int2 = Integer.parseInt(s2);
		} catch (Exception e) {}
		
		if (int1 > 0 && int2 > 0 && int1 + 1 == int2) {
			return 1;
		}
		if (s1.isEmpty() && int2 > 0) {
			return 2;
		}
		
		return 0;
	}
	
	@Override
	public String toString() {
		return "Doc [code=" + code + ", year=" + year + ", proposer=" + proposer + ", draftOrg="
				+ Arrays.toString(draftOrg) + ", draftPeo=" + Arrays.toString(draftPeo) + ", docLevelObj=" + docLevelObj
				+ ", reference=" + reference + "]";
	}
	
	/**
	 * 找出各个zb对应的层级，用到全局变量ss（zb-层级对应关系，HashMap<String,String>类型）、zbs（zb字典，ArrayList<String>类型）
	 * @param json
	 * @return 空
	 */
	public void getLevel(Object objJson) {
		JSONObject jsonObj = (JSONObject)objJson;
		Iterator iterator = jsonObj.keys();
		while(iterator.hasNext()) {
			String key = iterator.next().toString();
			Object value = jsonObj.get(key);
			if(value instanceof JSONObject) {
				if(((JSONObject) value).has("level")){
					String nowVal = ((String) ((JSONObject) value).get("title"));
					for(String zb:zbs) {
						if(nowVal.indexOf(zb) != -1) {
							if(!ss.containsKey(zb)) 
								ss.put(zb, new ArrayList<String>());
							ss.get(zb).add((String) ((JSONObject) value).get("level"));
						}
					}
				}
				if(((JSONObject) value).size() != 2) {
					getLevel((JSONObject)value);
				}
			}
		}
		
	}
	
	/**
	 * 找出每个zb与其他zb同时出现在本文件中的次数
	 * @return HashMap<HashMap<String,String>,Integer>类型，
	 *   - HashMap<String,String>：两个有关系的zb
	 *   - Integer：两个zb出现的“总共”次数
	 */
	public HashMap<HashMap<String,String>,Integer> getAppearTimeInSameFile() {
		HashMap<HashMap<String,String>,Integer> hm = new HashMap<HashMap<String,String>,Integer>();
		for(int i = 0;i < zbs.size();i++)
			for(int j = i + 1;j < zbs.size();j++) {
				if(ss.containsKey(zbs.get(i)) && ss.containsKey(zbs.get(j))) {
					HashMap<String,String> t = new HashMap<String,String>();
					if(zbs.get(i).compareTo(zbs.get(j)) < 0) {
						t.put(zbs.get(i), zbs.get(j));
					}else if(zbs.get(i).compareTo(zbs.get(j)) > 0){
						t.put(zbs.get(j), zbs.get(i));
					}
					if(hm.containsKey(t))
						hm.put(t,new Integer(hm.get(t).intValue() + ss.get(zbs.get(i)).size() + ss.get(zbs.get(j)).size()));
					else
						hm.put(t, new Integer(ss.get(zbs.get(i)).size() + ss.get(zbs.get(j)).size()));
				}
			}
		return hm;
	}
	
	/**
	 * 统计一下每个zb与其他zb同时出现在各个层级的次数
	 * @param json
	 * @return 空
	 * 	 - HashMap<String,String>：两个有关系的zb
	 *   - Integer：两个zb出现的“总共”次数
	 */
	public HashMap<HashMap<String,String>,Integer> getAppearTimeInSameLevel(Object objJson) {
		HashMap<HashMap<String,String>,Integer> hm = new HashMap<HashMap<String,String>,Integer>();
		for(int i = 0;i < zbs.size();i++)
			for(int j = i + 1;j < zbs.size();j++)
				if(ss.containsKey(zbs.get(i)) && ss.containsKey(zbs.get(j))) {
					int num = 0;
					for(int m = 0;m < ss.get(zbs.get(i)).size();m++) 
						for(int n = 0;n < ss.get(zbs.get(j)).size();n++) {
							if((String)(ss.get(zbs.get(i)).toArray()[m]) == (String)(ss.get(zbs.get(j)).toArray()[n]))
								num++;							
						}
					if(num != 0) {
						HashMap<String,String> t = new HashMap<String,String>();
						t.put(zbs.get(i),zbs.get(j));
						hm.put(t, num);												
					}
				}
//				else {
//					HashMap<String,String> t = new HashMap<String,String>();
//					t.put(zbs.get(i),zbs.get(j));
//					hm.put(t,0);
//				}
		return hm;
	}
	
	/**
	 * 计算两个装备的jaccard相似度
	 * @param args
	 */
	
	public static List<Map.Entry<HashMap<String,String>,Double>> getJaccardSimilarity(HashMap<String,Integer> hm,HashMap<HashMap<String,String>,Integer> hmm) {
		HashMap<HashMap<String,String>,Double> jaccard = new HashMap<HashMap<String,String>,Double>();
		Iterator iter = hmm.entrySet().iterator();

		while(iter.hasNext()) {
			Map.Entry<HashMap<String,String>, Integer> entry = (Map.Entry<HashMap<String,String>, Integer>)iter.next();
			HashMap<String,String> key = entry.getKey();
			Integer value = entry.getValue();
			if(!key.entrySet().iterator().hasNext()) continue;
			Map.Entry<String,String> entry2 = key.entrySet().iterator().next();
//			if(entry2.getKey() == null || entry2.getValue() == null) continue;
			String word1 = entry2.getKey();
			String word2 = entry2.getValue();
			if(hm.containsKey(word1) && hm.containsKey(word2))
				jaccard.put(key, value*1.0/(hm.get(word1) + hm.get(word2)));
			
		}
		
		//TODO 按value进行排序
		List<Map.Entry<HashMap<String,String>,Double>> jaccard_list = new ArrayList<Map.Entry<HashMap<String,String>,Double>>(jaccard.entrySet());
		Collections.sort(jaccard_list,new Comparator<Map.Entry<HashMap<String,String>,Double>>(){
			public int compare(Entry<HashMap<String,String>,Double> o1,Entry<HashMap<String,String>,Double> o2) {
				return -o1.getValue().compareTo(o2.getValue());
			}
		});
		
		return jaccard_list;
		
	}
	/**
	 * 装备字典（变量为zbs）初始化，从字典txt中读取装备名称后加入到zbs中去
	 * @param dictPath
	 */
	public void initializeZbs(String dictPath) {
		File file = new File(dictPath);
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
		
	}
	/**
	 * 获取某文件中所有字典中的装备对应出现的次数
	 * @return
	 */
	public HashMap<String,Integer> getAppearTimePerWord() {
		HashMap<String,Integer> hm = new HashMap<String,Integer>();
		for(String line:content) {
			String maxString = line;
			for(int i = 0;i < zbs.size();i++) {
				int count = 0;
				int index = maxString.indexOf(zbs.get(i));
				while(index != -1){
					count++;
					maxString = maxString.substring(index + zbs.get(i).length());
					index = maxString.indexOf(zbs.get(i));
				}
				if(count != 0)
					if(hm.containsKey(zbs.get(i)))
						hm.put(zbs.get(i), count + hm.get(zbs.get(i)));
					else hm.put(zbs.get(i), count);
			}
		}
		System.out.println(hm);
		return hm;
	}
	
	public static List<Map.Entry<HashMap<String,String>,Double>> getjaccard() {
		
		//遍历目录下的txt文件
		File dir = new File("C:\\Users\\dragon\\Desktop\\33知识图谱txt数据\\txt\\junbiaotxt");
		File[] files = dir.listFiles();
		HashMap<String,Integer> hmall = new HashMap<String,Integer>();
		HashMap<HashMap<String,String>,Integer> hm1all = new HashMap<HashMap<String,String>,Integer>();
		HashMap<HashMap<String,String>,Integer> hm2all = new HashMap<HashMap<String,String>,Integer>();
		for(int i = 0;i < files.length;i++)
			if(files[i].getName().endsWith("txt")) {
				List<String> content = new LinkedList<String>();
				
				//1.将txt文件抽取为LinkedList<String>
				BufferedReader reader = null;
				try {
					InputStreamReader isr = new InputStreamReader(new FileInputStream(files[i]),"gbk");
					reader = new BufferedReader(isr);
					String tempString = null;
					while((tempString = reader.readLine()) != null) {
						content.add(tempString);
					}
				}catch(IOException e) {
					e.printStackTrace();
				}finally {
					if(reader != null)
						try {
							reader.close();
						}catch(IOException e) {}
				}
				
				//2.初始化字典
				Doc doc = new Doc(files[i].getName(),content);
				doc.initializeZbs("C:\\Users\\dragon\\Desktop\\dict（去重后）.txt");
				
				//3.结构化Doc对象
				try {
					doc.structure();
					try {
						doc.getLevel(doc.docLevelObj);
					}catch(Exception e) {
						System.out.println("文件获取层级出错：" + files[i].getName());
					}
				}catch(Exception e) {
					System.out.println(files[i].getName());
				}
				
				HashMap<String,Integer> atpw = doc.getAppearTimePerWord();
				Iterator iter = atpw.entrySet().iterator();
				while(iter.hasNext()) {
					Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>)iter.next();
					String key = entry.getKey();
					Integer value = entry.getValue();
					if(hmall.containsKey(key))
						hmall.put(key, value + atpw.get(key));
					else hmall.put(key, value);
				}
				
				System.out.println(files[i].getName() + " " + doc.ss);
								
				HashMap<HashMap<String,String>,Integer> hm1 = doc.getAppearTimeInSameFile();
				System.out.println("同一文档中的装备的共现次数：" + hm1);
				
				iter = hm1.entrySet().iterator();
				while(iter.hasNext()) {
					Map.Entry<HashMap<String,String>, Integer> entry = (Map.Entry<HashMap<String,String>, Integer>)iter.next();
					HashMap<String,String> key = entry.getKey();
					Integer value = entry.getValue();
					if(hm1all.containsKey(key))
						hm1all.put(key, value + hm1all.get(key));
					else hm1all.put(key, value);
				}
				
				HashMap<HashMap<String,String>,Integer> hm2 = doc.getAppearTimeInSameLevel(doc.docLevelObj);
				System.out.println("同一文档中同一等级的的装备的共现次数为：" + hm2);
				
				iter = hm2.entrySet().iterator();
				while(iter.hasNext()) {
					Map.Entry<HashMap<String,String>, Integer> entry = (Map.Entry<HashMap<String,String>, Integer>)iter.next();
					HashMap<String,String> key = entry.getKey();
					Integer value = entry.getValue();
					if(hm2all.containsKey(key))
						hm2all.put(key, value + hm2all.get(key));
					else hm2all.put(key, value);
				}
			}
		System.out.println(hmall);
		System.out.println(hm1all);
		System.out.println(hm2all);
		List<Map.Entry<HashMap<String,String>,Double>> sameDoc = getJaccardSimilarity(hmall,hm1all);
		System.out.println("同一文档中的装备的jaccard相似度为：" + sameDoc);
//		List<Map.Entry<HashMap<String,String>,Double>> sameLevel = getJaccardSimilarity(hmall,hm1all)
//		System.out.println("同一文档中同一等级的的装备的jaccard相似度为：" + );
		
		return sameDoc;
	}
}
