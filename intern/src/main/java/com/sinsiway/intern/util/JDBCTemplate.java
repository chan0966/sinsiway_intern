package com.sinsiway.intern.util;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sinsiway.intern.model.DatabaseModel;

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

	public static void commit(Connection conn) {
		try {
			if (conn != null && !conn.isClosed())
				conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void rollback(Connection conn) {
		try {
			if (conn != null && !conn.isClosed())
				conn.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
