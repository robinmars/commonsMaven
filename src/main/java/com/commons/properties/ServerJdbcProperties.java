package com.commons.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;



public class ServerJdbcProperties {
	private static Properties props;

	 static{
		 loadProps();
	 }
	 
	 synchronized static private void loadProps(){
		String url = "/properties/server-jdbc.properties";
		props = new Properties();
		InputStream in = null;
		try {
			in = ServerJdbcProperties.class.getResourceAsStream(url);
			props.load(in);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	 }
	 
	 public static  String getValue(String key){
		 if(props==null){
			 loadProps();
		 }
		 return props.getProperty(key);
	 }
	 
	
}
