package com.commons.redis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Utils {

	public static byte[] toByteArray(Object objValue) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		new ObjectOutputStream(bos).writeObject(objValue);
		byte[] t = bos.toByteArray();
		return t;
	}

	public static Object toObject(byte [] array){
		Object obj = null;
		if (null == array)return null;
		ByteArrayInputStream bis = new ByteArrayInputStream(array);
		try {
			obj = new ObjectInputStream(bis).readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	public static String FirstUpperCase(String columnName) {
		String result = columnName.substring(0, 1).toUpperCase() + columnName.substring(1);
		return result;
	}
	
	public static void main(String [] s){}
		 
}
