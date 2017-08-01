package com.commons.util;

import java.util.UUID;

public class UUIDUtil {
	
	//获取UUID
	public static String getUUID() {
		return UUID.randomUUID().toString();
	}

	//获取UUID,去"-"
	public static String getUUIDStr() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	// 获得指定数量的UUID
	public static String[] getUUID(int number) {
		if (number < 1) {
			return null;
		}
		String[] ss = new String[number];
		for (int i = 0; i < number; i++) {
			ss[i] = getUUID();
		}
		return ss;
	}

	public static void main(String[] args) {
		// String[] ss = getUUID(10);
		 for (int i = 0; i < 100; i++) {
			 System.out.println("i====="+getUUIDStr());
		 }

//		System.out.println(getUUIDStr());
	}
}
