package com.sinsiway.intern.sqltool.mapper.system;

import java.util.ArrayList;
import java.util.HashMap;

import com.sinsiway.intern.sqltool.model.ConnModel;
import com.sinsiway.intern.sqltool.model.DatabaseModel;
import com.sinsiway.intern.sqltool.model.ExecuteQueryModel;
import com.sinsiway.intern.sqltool.model.RejectPolicyModel;

public interface SystemDatabaseMapper {

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

	/**
	 * 데이터베이스 수행로그 삽입
	 * @param childExecuteQueryModel
	 * @return
	 */
	int insertExecuteLog(ExecuteQueryModel childExecuteQueryModel);

	/**
	 * 거부 아이피 추가
	 * @param rejectPolicyModel
	 * @return
	 */
	int InsertRejectPolicy(RejectPolicyModel rejectPolicyModel)throws Exception;

	/**
	 * 데이터베이스 아이디로 거부 아이피 조회
	 * @param databaseIdL
	 * @return
	 */
	ArrayList<RejectPolicyModel> getRejectPolicyByDatabaseId(long databaseIdL);

	/**
	 * 거부 정책 삭제
	 * @param policyIdL
	 * @return
	 */
	int deleteRejectPolicy(long policyIdL);

	/**
	 * 데이터베이스 수정
	 * @param databaseModel
	 * @return
	 */
	int updateDatabase(DatabaseModel databaseModel);

	
	
}
