package com.sinsiway.intern.util;

import java.util.UUID;

public class InternUtil {
	
	public static long getIntId() {
		long currunt = System.currentTimeMillis();
		long random = (long) (Math.random()*100000);
				
		String randomStr = String.format("%06d", random);
		String currentStr = Long.toString(currunt);
		
		String idStr = randomStr.concat(currentStr);
		
		long id = Long.parseLong(idStr);
		
		return id;
	}
	
	public static String getUUID() {
		return UUID.randomUUID().toString();
	}
}
