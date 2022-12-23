package com.sinsiway.intern.controller;

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
import com.sinsiway.intern.model.ConnSet;
import com.sinsiway.intern.model.DatabaseModel;
import com.sinsiway.intern.service.DatabaseService;
import com.sinsiway.intern.util.InternUtil;
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
	@GetMapping("/allDatabases")
	@ResponseBody
	public String selectAllDatabases() {
		Gson gson = new Gson();

		ArrayList<HashMap<String, Object>> resultSet = databaseService.selectAllDatabases();

		return gson.toJson(resultSet);
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
	public String CreateConnDatabase(@RequestBody HashMap<String, String> param, HttpSession session,
			HttpServletRequest req) {
		Gson gson = new Gson();

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

		return gson.toJson(regDatabaseResultMap);
	}

	/**
	 * 데이터베이스 삭제
	 * 
	 * @return
	 */
	@DeleteMapping("database/{databaseId}")
	@ResponseBody
	public String deleteDatabase(@PathVariable("databaseId") String databaseId, HttpSession session) {
		Gson gson = new Gson();
		Long DatabaseIdL = Long.parseLong(databaseId);
		ArrayList<Long> disconnConnIdList = new ArrayList<>();

		// 먼저 세션에서 데이터베이스 아이디에 맞는 접속 해제
		for (String connId : InternUtil.getConnIdList()) {
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
		int result = databaseService.deleteDatabase(DatabaseIdL);

		// 결과 전달용 map
		HashMap<String, Object> resultMap = new HashMap<>();
		if (result != 0) {
			resultMap.put("result", true);
			resultMap.put("msg",
					"데이터베이스 등록 정보 삭제 성공 ID:" + databaseId + ". " + disconnConnIdList.toArray().length + "개의 접속 해제");
		} else {
			resultMap.put("result", false);
			resultMap.put("msg", "데이터베이스 등록 정보 삭제 실패 등록되지않은ID:" + databaseId + ". " + disconnConnIdList.toArray().length
					+ "개의 접속 해제");
		}

		resultMap.put("disconnConnIdList", disconnConnIdList);
		resultMap.put("disconnCount", disconnConnIdList.toArray().length);

		return gson.toJson(resultMap);
	}

}
