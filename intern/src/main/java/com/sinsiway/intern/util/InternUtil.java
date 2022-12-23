package com.sinsiway.intern.util;

import java.util.LinkedList;
import java.util.UUID;

public class InternUtil {

	private static LinkedList<String> connIdList;
	static{
		connIdList = new LinkedList<>();
		connIdList.add(getUUID());
	}

	public static LinkedList<String> getConnIdList(){
		return connIdList;
	}
	public static String getConnId() {
		return connIdList.getLast();
	}
	public static void addConnId() {
		connIdList.add(getUUID());
	}
	public static void deleteConnId(String connId) {
		connIdList.remove(connId);
	}

	public static long getIntId() {
		long currunt = System.currentTimeMillis();
		long random = (long) (Math.random() * 100000);

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
