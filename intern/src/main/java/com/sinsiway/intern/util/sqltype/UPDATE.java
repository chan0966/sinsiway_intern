package com.sinsiway.intern.util.sqltype;

import java.sql.Connection;

import com.sinsiway.intern.repository.SqlType;

public class UPDATE extends SqlType{
	@Override
	public void execute(Connection conn, String sqlText) {
		try {
			int result = this.executeUpdate(conn, sqlText);
			setResult(true);
			setMsg(result + "개의 행이 UPDATE 됨");
		} catch (Exception e) {
			setResult(false);
			setMsg(e.getMessage());
		}
	}
}
