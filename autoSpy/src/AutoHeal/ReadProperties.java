package AutoHeal;

import java.io.InputStream;
import java.util.Properties;

public class ReadProperties{
	public String fetchPropertiesOfXpath(String node) {
		String result = "";
		try {
		Properties prop = new Properties();
		String propFileName = "priority.properties";
		InputStream inputStreamValue = getClass().getClassLoader().getResourceAsStream(propFileName);
			prop.load(inputStreamValue);
			result = prop.getProperty(node);
		}catch(Exception e) {
			e.getMessage();
		}
		return result;
	}
	public static void main(String[] args) {
		ReadProperties r = new ReadProperties();
		System.out.println(r.fetchPropertiesOfXpath("input"));
	}
}
