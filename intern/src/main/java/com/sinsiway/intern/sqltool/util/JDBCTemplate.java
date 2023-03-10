package com.sinsiway.intern.sqltool.util;

import java.sql.Statement;

import com.sinsiway.intern.sqltool.model.DatabaseModel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class JDBCTemplate {
	private final DatabaseModel database;

	public JDBCTemplate(DatabaseModel database) {
		this.database = database;
	}

	public Connection getConnection() throws Exception {

		DatabaseType[] dbArr = DatabaseType.values();
		String dbname = dbArr[database.getType()].getDbName();
		String driverClassName = dbArr[database.getType()].getDriverClassName();

		Class.forName(driverClassName);

		String url = "jdbc:" + dbname + "://" + database.getIp() + ":" + database.getPort() + "/"
				+ database.getDatabase();
		String username = database.getUsername();
		String password = database.getPassword();

		Connection conn = DriverManager.getConnection(url, username, password);
		conn.setAutoCommit(false);

		return conn;

	}

	public static void close(Connection conn) {
		try {
			if (conn != null && !conn.isClosed())
				conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void close(ResultSet rs) {
		try {
			if (rs != null && !rs.isClosed())
				rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void close(Statement stmt) {
		try {
			if (stmt != null && !stmt.isClosed())
				stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
