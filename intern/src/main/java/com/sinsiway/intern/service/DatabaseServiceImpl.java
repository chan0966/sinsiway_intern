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
		
		try {
			//테스트 접속
			JDBCTemplate jdbctemplate = new JDBCTemplate(databaseModel);
			Connection connection = jdbctemplate.getConnection();//안되면 여기서 에러 발생
			JDBCTemplate.close(connection);
			
			//존재하지 않는 데이터베이스일때 데이터베이스 데이블에 삽입
			String searchedDatabaseId = h2Mapper.selectDatabaseIdByDatabaseModel(databaseModel);
			if(searchedDatabaseId == null) {
				int result = h2Mapper.insertDatabase(databaseModel);
				
				if(result == 0) {//실패시
					//TODO : 로깅
					databaseModel.setDatabaseId(-1);
					resultMap.put("result", false);
					resultMap.put("msg", "데이터베이스 등록 실패");
				}else {
					resultMap.put("result", true);
					resultMap.put("msg", "데이터베이스 등록 성공");
				}
			}else {//이미 존재하는 데이터베이스일 경우
				databaseModel.setDatabaseId(Long.parseLong(searchedDatabaseId));
				resultMap.put("result", false);
				resultMap.put("msg", "이미 등록된 데이터베이스");
			}
			
			//결과 맵에 다시 넣어주기
			resultMap.put("databaseModel", databaseModel);
			
		} catch (Exception e) { // 접속 안될시
			//TODO:로깅
			databaseModel.setDatabaseId(-1);
			resultMap.put("result", false);
			resultMap.put("msg", "테스트 접속 실패");
		}
		
		return resultMap;
	}

	/**
	 * 데이터베이스 등록 삭제
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

	/**
	 * 데이터베이스 아이디로 커넥션결과 가져오기
	 */
	@Override
	public HashMap<String, Object> getConnectionByDatabaseId(long databaseId) {
		HashMap<String, Object> connResult = new HashMap<>();
		DatabaseModel databaseModel = h2Mapper.selectDatabaseById(databaseId);
		
		//실패시 결과, 메세지 담아서 반환
		if(databaseModel == null) {
			connResult.put("result", false);
			connResult.put("msg", "데이터베이스를 찾을 수 없습니다.");
			
		//성공시 커넥션 얻어서 봔환
		}else {
			try {
				Connection conn = new JDBCTemplate(databaseModel).getConnection();
				
				connResult.put("connection", conn);
				connResult.put("result", true);
				connResult.put("msg", "접속 성공");
				
			} catch (Exception e) {
				connResult.put("result", false);
				connResult.put("msg", "커넥션을 얻는데 실패했습니다.");
				//TODO : 로깅
			}
		}
		
		return connResult;
	}
}
