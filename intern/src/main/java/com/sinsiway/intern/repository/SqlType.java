package com.sinsiway.intern.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.sinsiway.intern.util.JDBCTemplate;

/**
 * 대문자 구문이름을 가진 클래스에 상속 후 execute 메소드 오버라이딩 execute 메소드 오버라이딩 시에 msg, result 필드 setter이용해서 초기화해야함
 * @author chan
 *
 */
public abstract class SqlType {
	
	private String msg;
	private boolean result;
	
	public String getMsg() {
		return msg;
	}
	public boolean getResult() {
		return result;
	}
	
	protected void setMsg(String msg) {
		this.msg = msg;
	}
	protected void setResult(boolean result) {
		this.result = result;
	}
	/**
	 * 오버라이딩 후에 this.executeUpdate메소드 이용하여 구문 수행 후, msg, result 필드 초기화해야함
	 * @param conn
	 * @param sqlText
	 */
	abstract public void execute(Connection conn, String sqlText);
	
	/**
	 * executeUpdate select 이외의 구문 수행
	 * @param conn
	 * @param sqlText
	 * @return
	 * @throws Exception
	 */
	protected int executeUpdate(Connection conn, String sqlText) throws Exception {
		PreparedStatement pstmt = conn.prepareStatement(sqlText);
		int result = pstmt.executeUpdate();
		
		JDBCTemplate.close(pstmt);
		
		return result;
	}
	
	
	
}
