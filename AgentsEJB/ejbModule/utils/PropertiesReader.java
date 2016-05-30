package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {
	

	private String master;
	private String local;
	
	
	public PropertiesReader(){		
		Properties properties = new Properties();
		String fileName = "resources/host-info.properties";
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
		
		if(inputStream!=null){
			try {
				properties.load(inputStream);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		master = properties.getProperty("master");
		System.out.println("master" + master);
		local = properties.getProperty("local");

	}
	
	
	public String getMaster(){
		return master;
	}
	
	public String getLocal(){
		return local;
	}
	
}