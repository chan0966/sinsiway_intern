package com.sinsiway.intern.mapper.h2;

import java.util.ArrayList;
import java.util.HashMap;

import com.sinsiway.intern.model.ConnModel;
import com.sinsiway.intern.model.DatabaseModel;

public interface H2Mapper {

	/**
	 * 데이터베이스 삽입 후 selectKey로 아이디 부여 후 결과 리턴
	 * @param databaseModel
	 * @return
	 */
	int insertDatabase(DatabaseModel databaseModel);

	/**
	 * 로그 테이블에 데이터 삽입
	 * @param connModel
	 * @return
	 */
	int isnertConnLog(ConnModel connModel);

	/**
	 * 존재하는 데이터베이스인지 아닌지 검색
	 * @param databaseModel
	 * @return
	 */
	String selectDatabaseIdByDatabaseModel(DatabaseModel databaseModel);

	/**
	 * 데이터베이스 데이터 삭제
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
	 * 데이터베이스 아이디로 데이터베이스 가져오기
	 * @param databaseId
	 * @return
	 */
	DatabaseModel selectDatabaseById(long databaseId);

	
	
}
