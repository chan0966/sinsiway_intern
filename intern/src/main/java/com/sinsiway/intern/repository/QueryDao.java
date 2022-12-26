package com.sinsiway.intern.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public interface QueryDao {

	/**
	 * select 문 실행
	 * @param conn
	 * @param sqlText
	 * @return
	 * @throws SQLException
	 */
	ArrayList<ArrayList<Object>> executeQuery(Connection conn, String sqlText) throws Exception;

	void commit(Connection conn) throws SQLException;

	void rollback(Connection conn) throws SQLException;
	
}
