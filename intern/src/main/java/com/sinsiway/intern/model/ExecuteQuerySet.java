package com.sinsiway.intern.model;

import java.sql.Connection;

import lombok.Data;

@Data
public class ExecuteQuerySet {
	private ExecuteQueryModel executeQueryModel;
	private Connection connection;
	private String sqlText;
}
