package com.sinsiway.intern.service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sinsiway.intern.mapper.h2.H2Mapper;
import com.sinsiway.intern.model.DatabaseModel;
import com.sinsiway.intern.util.JDBCTemplate;

@Service
public class DatabaseServiceImpl implements DatabaseService{
	
	@Autowired
	private H2Mapper h2Mapper;

	/**
	 * 데이터베이스 처음 접속-데이터베이스 테이블에 삽입, 연결, 정보 리턴
	 */
	@Override
	public HashMap<String, Object> CreateConnDatabase(DatabaseModel databaseModel) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("databaseModel", databaseModel);
		
		try {
			//테스트 접속
			JDBCTemplate jdbctemplate = new JDBCTemplate(databaseModel);
			Connection connection = jdbctemplate.getConnection();//안되면 여기서 에러 발생
			
			//이미 존재하는 데이터베이스일때 접속만처리 존재하지 않는 데이터베이스일때 데이터베이스 데이블에 삽입
			String searchedDatabaseId = h2Mapper.selectDatabaseIdByDatabaseModel(databaseModel);
			if(searchedDatabaseId == null) {
				int result = h2Mapper.insertDatabase(databaseModel);
				
				if(result == 0) {//실패시
					databaseModel.setDatabaseId(-1);
				}
			}else {
				databaseModel.setDatabaseId(Long.parseLong(searchedDatabaseId));
			}
			
			//결과 생성
			resultMap.put("databaseModel", databaseModel);
			resultMap.put("connection", connection);
			
		} catch (Exception e) {
			//로깅
			databaseModel.setDatabaseId(-1);
		}
		
		return resultMap;
	}

	/**
	 * 데이터베이스 접속 설정 삭제
	 * @return 
	 */
	@Override
	public int deleteDatabase(long databaseId) {
		int result = 0;
		
		try {
			result = h2Mapper.deleteDatabase(databaseId);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception 로깅
		}
		
		return result;
	}

	/**
	 * 모든 데이터베이스 가져오기
	 */
	@Override
	public ArrayList<HashMap<String, Object>> selectAllDatabases() {
		return h2Mapper.selectAllDatabases();
	}
}
