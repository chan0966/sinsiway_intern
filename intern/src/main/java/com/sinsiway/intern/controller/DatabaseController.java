package com.sinsiway.intern.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sinsiway.intern.model.ConnSet;
import com.sinsiway.intern.model.DatabaseModel;
import com.sinsiway.intern.service.DatabaseService;
import com.sinsiway.intern.util.ConnIdUtill;
import com.sinsiway.intern.util.JDBCTemplate;

@Controller
public class DatabaseController {

	@Autowired
	private DatabaseService databaseService;

	/**
	 * 등록되있는 데이터베이스들 가져오기
	 * 
	 * @return
	 */
	@GetMapping("/alldatabases")
	@ResponseBody
	public ArrayList<HashMap<String, Object>> selectAllDatabases() {
		ArrayList<HashMap<String, Object>> resultSet = databaseService.selectAllDatabases();

		return resultSet;
	}

	/**
	 * 데이터베이스 등록
	 * 
	 * @param param
	 * @param session
	 * @param req
	 * @return
	 */
	@PostMapping("database")
	@ResponseBody
	public Map<String, Object> CreateConnDatabase(@RequestBody HashMap<String, String> param, HttpSession session,
			HttpServletRequest req) {

		// 받아온 정보로 데이터베이스 모델 작성
		DatabaseModel databaseModel = new DatabaseModel();
		databaseModel.setDatabase(param.get("database"));
		databaseModel.setIp(param.get("ip"));
		databaseModel.setUsername(param.get("username"));
		databaseModel.setPassword(param.get("password"));
		databaseModel.setPort(Integer.parseInt(param.get("port")));
		databaseModel.setType(Integer.parseInt(param.get("type")));

		// 서비스로 전달 후 아이디 부여, 결과 메시지 얻기
		HashMap<String, Object> resultMap = databaseService.CreateConnDatabase(databaseModel);
		databaseModel = (DatabaseModel) resultMap.get("databaseModel");

		// 전달용 결과 맵 작성
		HashMap<String, Object> regDatabaseResultMap = new HashMap<String, Object>();
		regDatabaseResultMap.put("databaseModel", databaseModel);
		regDatabaseResultMap.put("msg", resultMap.get("msg"));
		regDatabaseResultMap.put("result", resultMap.get("result"));

		return regDatabaseResultMap;
	}

	/**
	 * 데이터베이스 삭제
	 * 
	 * @return
	 */
	@DeleteMapping("database/{databaseId}")
	@ResponseBody
	public Map<String, Object> deleteDatabase(@PathVariable("databaseId") String databaseId, HttpSession session) {
		HashMap<String, Object> resultMap = new HashMap<>();
		ArrayList<Long> disconnConnIdList = new ArrayList<>();
		Long DatabaseIdL = 0L;

		try {
			DatabaseIdL = Long.parseLong(databaseId);
		} catch (Exception e) {
			resultMap.put("result", false);
			resultMap.put("msg", "올바르지 않은 데이터베이스 아이디입니다. 정수로 입력해주세요");
			return resultMap;
		}

		// 먼저 세션에서 데이터베이스 아이디에 맞는 접속 해제
		for (String connId : ConnIdUtill.getConnIdList()) {
			ConnSet connSet = (ConnSet) session.getAttribute(connId);
			if (connSet != null) {
				if (DatabaseIdL == connSet.getConnModel().getDatabaseId()) {
					JDBCTemplate.close(connSet.getConn());

					session.removeAttribute(connId);
					disconnConnIdList.add(connSet.getConnModel().getId());
				}
			}
		}

		// 삭제 수행
		resultMap.putAll(databaseService.deleteDatabase(DatabaseIdL, disconnConnIdList)) ;

		resultMap.put("disconnConnIdList", disconnConnIdList);
		resultMap.put("disconnCount", disconnConnIdList.toArray().length);

		return resultMap;
	}
	
	/**
	 * 데이터베이스 조회
	 * @param databaseId
	 * @param session
	 * @return
	 */
	@GetMapping("database/{databaseId}")
	@ResponseBody
	public Object selectDatabase(@PathVariable("databaseId") String databaseId, HttpSession session) {
		HashMap<String, Object> resultMap = new HashMap<>();

		Long DatabaseIdL = 0L;
		try {
			DatabaseIdL = Long.parseLong(databaseId);
		} catch (Exception e) {
			resultMap.put("result", false);
			resultMap.put("msg", "올바르지 않은 데이터베이스 아이디입니다. 정수로 입력해주세요");
			return resultMap;
		}
		
		DatabaseModel databaseModel = databaseService.getDatabaseById(DatabaseIdL);
		if(databaseModel == null) {
			resultMap.put("result", false);
			resultMap.put("msg", "존재하지않는 데이터베이스입니다.");
			return resultMap;
		}
		
		return databaseModel;
	}
	
	@PutMapping("database")
	@ResponseBody
	public Map<String, Object> updateDatabase(@RequestBody HashMap<String, String> param, HttpSession session,
			HttpServletRequest req) {
		HashMap<String, Object> resultMap = new HashMap<>();
		DatabaseModel databaseModel = null;
		Long DatabaseIdL = 0L;

		try {
			DatabaseIdL = Long.parseLong(param.get("databaseId"));
		} catch (Exception e) {
			resultMap.put("result", false);
			resultMap.put("msg", "올바르지 않은 데이터베이스 아이디입니다. 정수로 입력해주세요");
			return resultMap;
		}
		
		try {
			databaseModel = databaseCheck(param);
		} catch (Exception e) {
			resultMap.put("result", false);
			resultMap.put("msg", "올바르지 않은 데이터입니다.");
			return resultMap;
		}
		
		databaseModel.setDatabaseId(DatabaseIdL);
		
		resultMap = databaseService.updateDatabase(databaseModel);
		
		return null;
	}
	
	/**
	 * 데이터베이스 유효성 검사기
	 */
	private DatabaseModel databaseCheck(HashMap<String, String> param) throws Exception{
		DatabaseModel databaseModel = new DatabaseModel();
		databaseModel.setDatabaseId(Long.parseLong(param.get("database")));
		databaseModel.setDatabase(param.get("database"));
		databaseModel.setIp(param.get("ip"));
		databaseModel.setUsername(param.get("username"));
		databaseModel.setPassword(param.get("password"));
		databaseModel.setPort(Integer.parseInt(param.get("port")));
		databaseModel.setType(Integer.parseInt(param.get("type")));
		return databaseModel;
	}
}
