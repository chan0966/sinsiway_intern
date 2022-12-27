package com.sinsiway.intern.sqltool.util.sqltype;

import java.sql.Connection;

import org.springframework.stereotype.Component;

import com.sinsiway.intern.sqltool.repository.SqlType;
@Component
public class INSERT extends SqlType{
	
	@Override
	public void execute(Connection conn, String sqlText) {
		try {
			int result = this.executeUpdate(conn, sqlText);
			setResult(true);
			setMsg(result + "개의 행이 INSERT 됨");
		} catch (Exception e) {
			setResult(false);
			setMsg(e.getMessage());
		}
	}
	
}
