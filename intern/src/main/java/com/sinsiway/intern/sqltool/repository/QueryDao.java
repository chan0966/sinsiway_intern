package com.sinsiway.intern.sqltool.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.springframework.stereotype.Repository;

import com.sinsiway.intern.sqltool.util.JDBCTemplate;

@Repository
public class QueryDao {

	/**
	 * executeQuery
	 */
	public LinkedHashMap<String, Object> executeQuery(Connection conn, String sqlText) throws Exception {
		PreparedStatement pstmt = conn.prepareStatement(sqlText);
		ResultSet rs = pstmt.executeQuery();

		LinkedHashMap<String,Object> result = convertResultSetToArrayList(rs);

		JDBCTemplate.close(pstmt);
		JDBCTemplate.close(rs);

		return result;
	}

	public void commit(Connection conn) throws SQLException {
		conn.commit();
	}

	public void rollback(Connection conn) throws SQLException {
		conn.rollback();
	}

	/**
	 * resultSet to arrayList<arrayList>
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private LinkedHashMap<String, Object> convertResultSetToArrayList(ResultSet rs) throws SQLException {
		ResultSetMetaData md = rs.getMetaData();
		int columns = md.getColumnCount();
		LinkedHashMap<String,Object> map = new LinkedHashMap<String, Object>();

		ArrayList<Object> colNames = new ArrayList<Object>();
		for (int i = 1; i <= columns; ++i) {
			colNames.add(md.getColumnName(i));
		}
		map.put("colums",colNames);

		int cnt = 1;
		while (rs.next()) {
			LinkedHashMap<String,Object> row = new LinkedHashMap<String, Object>();
			for (int i = 1; i <= columns; ++i) {
				row.put(md.getColumnName(i),rs.getObject(i));
			}
			map.put(Integer.toString(cnt),row);
			cnt++;
		}

		return map;
	}

}
