package com.sinsiway.intern.model;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class ExecuteQueryModel {
	private long id;
	private String clientIp;
	private Timestamp execDate;
	private String message;
	private boolean result;
	private String sqlText;
	private String sqlType;
	private long databaseId;
	
	private List<Map<String, Object>> resultMap;
}
