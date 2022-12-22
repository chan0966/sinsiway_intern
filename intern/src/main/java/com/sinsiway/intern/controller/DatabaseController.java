package com.sinsiway.intern.controller;

import java.sql.Connection;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.sinsiway.intern.model.ConnModel;
import com.sinsiway.intern.model.DatabaseModel;
import com.sinsiway.intern.service.DatabaseService;
import com.sinsiway.intern.service.LogService;
import com.sinsiway.intern.util.InternUtil;
import com.sinsiway.intern.util.JDBCTemplate;

@Controller
public class DatabaseController {
	
	@Autowired
	private DatabaseService databaseService;
	@Autowired
	private LogService logService;
	
	private static int connCount = 0;
	
	@GetMapping("/allDatabases")
	@ResponseBody
	public String selectAllDatabases() {
		Gson gson = new Gson();
		
		ArrayList<HashMap<String, Object>> resultSet = databaseService.selectAllDatabases();
		
		return gson.toJson(resultSet);
	}
	
	@PostMapping("database")
	@ResponseBody
	public String CreateConnDatabase(@RequestBody HashMap<String, String> param, HttpSession session, HttpServletRequest req) {
		Gson gson = new Gson();
		
		//받아온 정보로 데이터베이스 모델 작성
		DatabaseModel databaseModel = new DatabaseModel();
		databaseModel.setDatabase(param.get("database"));
		databaseModel.setIp(param.get("ip"));
		databaseModel.setUsername(param.get("username"));
		databaseModel.setPassword(param.get("password"));
		databaseModel.setPort(Integer.parseInt(param.get("port")));
		databaseModel.setType(Integer.parseInt(param.get("type")));
		
		//서비스로 전달 후 아이디 부여, JDBCTemplate 얻기	
		HashMap<String, Object> resultMap = databaseService.CreateConnDatabase(databaseModel);
		Connection connection = (Connection) resultMap.get("connection");
		
		//접속 정보 작성
		ConnModel connModel = new ConnModel();
		connModel.setId(InternUtil.getIntId());
		connModel.setClientIp(req.getRemoteAddr());
		connModel.setConnectDate(Timestamp.valueOf(LocalDateTime.now()));
		connModel.setDatabaseId(databaseModel.getDatabaseId());
		
		//접속 성공 실패여부
		if(databaseModel.getDatabaseId() == -1) {// 실패시
			connModel.setResult(false);
			
		}else { // 성공시 ConnInstance생성 후 세션에 담기
			connModel.setResult(true);
			
			HashMap<String, Object> ConnInstance = new HashMap<String, Object>();
			
			ConnInstance.put("connModel", connModel);
			ConnInstance.put("connection", connection);
			
			//세션에 담기
			session.setAttribute("connInstance"+Integer.toString(connCount), ConnInstance);
			connCount++;
		}
		
		//로그 테이블에 데이터 입력
		int insertLogResult = logService.isnertConnLog(connModel);
		if(insertLogResult == 0) {
			//로그 출력
		}
		
		
		HashMap<String, Object> CreateConnDatabaseResultMap = new HashMap<String, Object>();
		CreateConnDatabaseResultMap.put("connModel", connModel);
		CreateConnDatabaseResultMap.put("databaseModel", databaseModel);
		
		return gson.toJson(CreateConnDatabaseResultMap);
	}
	
	/**
	 * 데이터베이스 삭제
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@DeleteMapping("database/{databaseId}")
	@ResponseBody
	public String deleteDatabase(@PathVariable("databaseId") String databaseId, HttpSession session){
		Gson gson = new Gson();
		Long DatabaseIdL = Long.parseLong(databaseId);
		
		// 먼저 세션에서 데이터베이스 아이디에 맞는 접속 해제
		for(int i = 0;i<connCount;i++) {
			HashMap<String, Object> connInstance = (HashMap<String, Object>) session.getAttribute("connInstance" + Integer.toString(i));
			
			if(connInstance != null) {
				ConnModel connModel = (ConnModel) connInstance.get("connModel");
				Connection connection = (Connection) connInstance.get("connection");
				if(DatabaseIdL == connModel.getDatabaseId()) {
					JDBCTemplate.close(connection);
					session.removeAttribute("connInstance" + Integer.toString(i));
				}
			}
		}
		
		//삭제 수행
		int result = databaseService.deleteDatabase(DatabaseIdL);
		
		//결과 전달용 map
		HashMap<String, Object> resultMap = new HashMap<>();
		if(result != 0) {
			resultMap.put("result", true);
			resultMap.put("msg", "Delete Database Success databaseId:" + databaseId);
		}else {
			resultMap.put("result", false);
			resultMap.put("msg", "Delete Database fail databaseId:" + databaseId);
		}
		
		return gson.toJson(resultMap);
	}
	
	/**
	 * 데이터가 존재하는 데이터베이스 연결
	 * @param databaseId
	 * @return
	 */
	@GetMapping("database/{databaseId}")
	@ResponseBody
	public String connnectDatabase(@PathVariable long databaseId) {
		
		return null;
	}
}
