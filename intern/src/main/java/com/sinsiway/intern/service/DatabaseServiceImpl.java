package com.sinsiway.intern.service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sinsiway.intern.mapper.system.SystemDatabaseMapper;
import com.sinsiway.intern.model.DatabaseModel;
import com.sinsiway.intern.util.JDBCTemplate;

@Service
public class DatabaseServiceImpl implements DatabaseService {

	@Autowired
	private SystemDatabaseMapper Mapper;

	/**
	 * 데이터베이스 처음 접속-데이터베이스 테이블에 삽입, 연결, 정보 리턴
	 */
	@Override
	public HashMap<String, Object> CreateConnDatabase(DatabaseModel databaseModel) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			// 테스트 접속
			JDBCTemplate jdbctemplate = new JDBCTemplate(databaseModel);
			Connection connection = jdbctemplate.getConnection();// 안되면 여기서 에러 발생
			JDBCTemplate.close(connection);

			// 존재하지 않는 데이터베이스일때 데이터베이스 데이블에 삽입
			String searchedDatabaseId = Mapper.selectDatabaseIdByDatabaseModel(databaseModel);
			if (searchedDatabaseId == null) {
				int result = Mapper.insertDatabase(databaseModel);

				if (result == 0) {// 실패시
					// TODO : 로깅
					resultMap.put("result", false);
					resultMap.put("msg", "데이터베이스 등록 실패");
				} else {
					resultMap.put("result", true);
					resultMap.put("msg", "데이터베이스 등록 성공");
				}
			} else {// 이미 존재하는 데이터베이스일 경우
				databaseModel.setDatabaseId(Long.parseLong(searchedDatabaseId));
				resultMap.put("result", false);
				resultMap.put("msg", "이미 등록된 데이터베이스");
			}

			// 결과 맵에 다시 넣어주기
			resultMap.put("databaseModel", databaseModel);

		} catch (Exception e) { // 접속 안될시
			// TODO:로깅
			resultMap.put("result", false);
			resultMap.put("msg", "테스트 접속 실패");
		}

		return resultMap;
	}

	/**
	 * 데이터베이스 등록 삭제
	 * 
	 * @return
	 */
	@Override
	public Map<String, Object> deleteDatabase(long databaseId, ArrayList<Long> disconnConnIdList) {
		int result = 0;
		HashMap<String, Object> resultMap = new HashMap<>();
		try {
			result = Mapper.deleteDatabase(databaseId);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", false);
			resultMap.put("msg", e.getMessage());
			return resultMap;
		}

		if (result != 0) {
			resultMap.put("result", true);
			resultMap.put("msg",
					"데이터베이스 등록 정보 삭제 성공 ID:" + databaseId + ". " + disconnConnIdList.toArray().length + "개의 접속 해제");
		} else {
			resultMap.put("result", false);
			resultMap.put("msg", "데이터베이스 등록 정보 삭제 실패 등록되지않은ID:" + databaseId + ". " + disconnConnIdList.toArray().length
					+ "개의 접속 해제");
		}

		return resultMap;
	}

	/**
	 * 모든 데이터베이스 가져오기
	 */
	@Override
	public ArrayList<HashMap<String, Object>> selectAllDatabases() {
		return Mapper.selectAllDatabases();
	}

	/**
	 * 데이터베이스 아이디로 데이터베이스 객체 반환
	 */
	@Override
	public DatabaseModel getDatabaseById(Long databaseIdL) {
		return Mapper.selectDatabaseById(databaseIdL);
	}

	/**
	 * 데이터베이스 수정
	 */
	@Override
	public HashMap<String, Object> updateDatabase(DatabaseModel databaseModel) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			// 테스트 접속
			JDBCTemplate jdbctemplate = new JDBCTemplate(databaseModel);
			Connection connection = jdbctemplate.getConnection();// 안되면 여기서 에러 발생
			JDBCTemplate.close(connection);

			// 존재하지 않는 데이터베이스일때 데이터베이스 데이블에 삽입
			String searchedDatabaseId = Mapper.selectDatabaseIdByDatabaseModel(databaseModel);
			if (searchedDatabaseId == null) {
				int result = Mapper.updateDatabase(databaseModel);

				if (result == 0) {// 실패시
					// TODO : 로깅
					resultMap.put("result", false);
					resultMap.put("msg", "데이터베이스 수정 실패");
				} else {
					resultMap.put("result", true);
					resultMap.put("msg", "데이터베이스 수정 성공");
				}
			} else {// 이미 존재하는 데이터베이스일 경우
				databaseModel.setDatabaseId(Long.parseLong(searchedDatabaseId));
				resultMap.put("result", false);
				resultMap.put("msg", "이미 등록된 데이터베이스");
			}

			// 결과 맵에 다시 넣어주기
			resultMap.put("databaseModel", databaseModel);

		} catch (Exception e) { // 접속 안될시
			// TODO:로깅
			resultMap.put("result", false);
			resultMap.put("msg", "테스트 접속 실패");
		}

		return resultMap;
	}

}
