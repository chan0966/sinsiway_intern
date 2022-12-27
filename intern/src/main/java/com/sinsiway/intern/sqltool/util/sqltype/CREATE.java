package com.sinsiway.intern.sqltool.util.sqltype;

import java.sql.Connection;

import com.sinsiway.intern.sqltool.repository.SqlType;

public class CREATE extends SqlType{
	@Override
	public void execute(Connection conn, String sqlText) {
		try {
			String[] sqlTextArr = sqlText.toUpperCase().trim().split(" ");
			this.executeUpdate(conn, sqlText);
			setResult(true);
			setMsg(sqlTextArr[1] + ": " + sqlTextArr[2].replaceAll("(", "") + "생성됨");
		} catch (Exception e) {
			setResult(false);
			setMsg(e.getMessage());
		}
	}
}
