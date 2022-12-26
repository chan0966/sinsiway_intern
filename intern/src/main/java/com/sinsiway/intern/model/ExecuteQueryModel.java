package com.sinsiway.intern.model;

import java.sql.Timestamp;
import java.util.ArrayList;

import lombok.Data;

@Data
public class ExecuteQueryModel implements Cloneable{
	private long id; // service
	private String clientIp;
	private Timestamp execDate;
	private String message;// service
	private boolean result;// service
	private String sqlText;// service
	private String sqlType;// service
	private long databaseId;

	private ArrayList<ArrayList<Object>> resultSet;// service
	private long connId;
	
	public void setResultAndMsg(boolean result, String message) {
		this.result = result;
		this.message = message;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
