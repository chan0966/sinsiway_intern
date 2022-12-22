package com.sinsiway.intern.model;

import lombok.Data;

@Data
public class DatabaseModel {
	private long databaseId;
	private int type;
	private String ip;
	private int port;
	private String database;
	private String username;
	private String password;
}
