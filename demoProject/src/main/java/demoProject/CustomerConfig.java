package demoProject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class CustomerConfig {
    private static final Properties CUSTOMER_PROPS = new Properties();
	static {
        try(InputStream customerStream = CustomerConfig.class.getResourceAsStream("/customer.properties")) {
            if(customerStream == null) {
            	throw new RuntimeException("Resource customer.properties not found.");
            }
            CUSTOMER_PROPS.load(customerStream);		
        } catch(IOException e) {
        	System.err.println("Cannot read customer.properties");
        	throw new ExceptionInInitializerError(e);
        }
	}
	
	public static String get(String key) {
		return CUSTOMER_PROPS.getProperty(key);
	}
	
	public static String get(String key, String defaultValue) {
		return CUSTOMER_PROPS.getProperty(key, defaultValue);
	}
}
