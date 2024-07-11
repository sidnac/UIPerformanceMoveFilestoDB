package General;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {    
	
	 public static Properties properties = new Properties();

	    static {
	        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
	            if (input == null) {
	            	throw new IOException("config.properties file not found in the classpath");
	            }
	            properties.load(input);
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
	    }

	    
}