package com.sinsiway.intern.model;

import java.sql.Timestamp;

import lombok.Data;
import lombok.Getter;

@Data
public class ConnModel {
	private long id;
	private String clientIp;
	private Timestamp connectDate;
	private boolean result;
	private long databaseId;
}
