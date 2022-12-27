package com.sinsiway.intern.sqltool.service;

import java.util.HashMap;

public interface ConnectionService {


	/**
	 * 데이터베이스 아이디로 커넥션결과 가져오기
	 * @param databaseId
	 * @param clientIp 
	 * @return
	 */
	HashMap<String, Object> getConnectionByDatabaseId(long databaseId, String clientIp);
}
