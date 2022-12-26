package com.sinsiway.intern.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.stereotype.Repository;

import com.sinsiway.intern.util.JDBCTemplate;

@Repository
public class QueryDaoImpl implements QueryDao {

	/**
	 * executeQuery
	 */
	@Override
	public ArrayList<ArrayList<Object>> executeQuery(Connection conn, String sqlText) throws Exception {
		PreparedStatement pstmt = conn.prepareStatement(sqlText);
		ResultSet rs = pstmt.executeQuery();

		ArrayList<ArrayList<Object>> result = convertResultSetToArrayList(rs);

		JDBCTemplate.close(pstmt);
		JDBCTemplate.close(rs);

		return result;
	}

	/**
	 * executeUpdate
	 */
	

	@Override
	public void commit(Connection conn) throws SQLException {
		conn.commit();
	}

	@Override
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
	private ArrayList<ArrayList<Object>> convertResultSetToArrayList(ResultSet rs) throws SQLException {
		ResultSetMetaData md = rs.getMetaData();
		int columns = md.getColumnCount();
		ArrayList<ArrayList<Object>> list = new ArrayList<ArrayList<Object>>();

		ArrayList<Object> colNames = new ArrayList<Object>();
		for (int i = 1; i <= columns; ++i) {
			colNames.add(md.getColumnName(i));
		}
		list.add(colNames);

		while (rs.next()) {
			ArrayList<Object> row = new ArrayList<Object>();
			for (int i = 1; i <= columns; ++i) {
				row.add(rs.getObject(i));
			}
			list.add(row);
		}

		return list;
	}

}
