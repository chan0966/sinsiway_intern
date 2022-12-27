package com.sinsiway.intern.sqltool.model;

import java.sql.Connection;

import lombok.Data;

@Data
public class ConnSet {
	ConnModel connModel;
	Connection conn;
}
