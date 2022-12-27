package com.sinsiway.intern.sqltool.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.sinsiway.intern.sqltool.model.DatabaseModel;

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
	 * @param disconnConnIdList 
	 * @return
	 */
	Map<String,Object> deleteDatabase(long databaseId, ArrayList<Long> disconnConnIdList);

	/**
	 * 모든 데이터베이스 가져오기
	 * @return
	 */
	ArrayList<HashMap<String, Object>> selectAllDatabases();

	/**
	 * 데이터베이스 아이디로 데이터베이스 객체 반환
	 * @param databaseIdL
	 * @return
	 */
	DatabaseModel getDatabaseById(Long databaseIdL);

	/**
	 * 데이터베이스 수정
	 * @param databaseModel
	 * @return
	 */
	HashMap<String, Object> updateDatabase(DatabaseModel databaseModel);


}
