package com.sinsiway.intern.service;

import java.util.ArrayList;
import java.util.HashMap;

import com.sinsiway.intern.model.DatabaseModel;

public interface DatabaseService {

	/**
	 * 데이터베이스 처음 접속-데이터베이스 테이블에 삽입, 연결, 정보 리턴
	 * @param databaseModel
	 * @return
	 */
	HashMap<String, Object> CreateConnDatabase(DatabaseModel databaseModel);

	/**
	 * 데이터베이스 삭제
	 * @param databaseId
	 * @return
	 */
	int deleteDatabase(long databaseId);

	/**
	 * 모든 데이터베이스 가져오기
	 * @return
	 */
	ArrayList<HashMap<String, Object>> selectAllDatabases();

	/**
	 * 데이터베이스 아이디로 커넥션결과 가져오기
	 * @param databaseId
	 * @return
	 */
	HashMap<String, Object> getConnectionByDatabaseId(long databaseId);

}
