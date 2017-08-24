package mylucene;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GetProperties {

	/**
	 * @param str
	 * @return
	 * @throws IOException
	 */

	public static String getproperties(String str) {
		InputStream fis = null;
		Properties prop = new Properties();//属性集合对象 
		try{
			String path = GetProperties.class.getClassLoader().getResource("").toString();
			path = path.substring(6, path.lastIndexOf("/WEB-INF/classes/") + 1) + "WEB-INF/classes/mylucene/settings.properties";
//			System.out.println(path);
			fis = new FileInputStream(new File(path));
			prop.load(fis);
			return prop.getProperty(str);			
		}catch(IOException e){
			e.printStackTrace();
		}
		return "";
	}
	public static void main(String[] args) throws IOException {
		System.out.println(GetProperties.getproperties("databaseName"));
	}
}
