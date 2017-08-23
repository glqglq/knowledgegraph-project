package mylucene;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class GetProperties {

	/**
	 * @param str
	 * @return
	 * @throws IOException
	 */

	public static String getproperties(String str) {
		FileInputStream fis = null;
		Properties prop = new Properties();//���Լ��϶��� 
		try{
			fis = new FileInputStream(new File("src/lucene/settings.properties"));//�����ļ��� 
			prop.load(fis);
			return prop.getProperty(str);			
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}
	public static void main(String[] args) throws IOException {
		System.out.println(GetProperties.getproperties("databaseName"));
	}
}
