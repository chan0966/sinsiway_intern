package com.sinsiway.intern.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.UUID;

public class ConnIdUtill {

	private static LinkedList<String> connIdList = new LinkedList<>();

	public static LinkedList<String> getConnIdList(){
		return connIdList;
	}
	public static String getConnId() {
		connIdList.add(getUUID());
		return connIdList.getLast();
	}
	public static void addConnId() {
		connIdList.add(getUUID());
	}
	public static void deleteConnId(ArrayList<String> deleteList) {
		connIdList.removeAll(deleteList);
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
