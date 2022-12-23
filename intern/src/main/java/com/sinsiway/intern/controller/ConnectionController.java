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
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.sinsiway.intern.model.ConnModel;
import com.sinsiway.intern.model.ConnSet;
import com.sinsiway.intern.service.DatabaseService;
import com.sinsiway.intern.service.LogService;
import com.sinsiway.intern.util.InternUtil;
import com.sinsiway.intern.util.JDBCTemplate;

@Controller
public class ConnectionController {

	@Autowired
	private LogService logService;
	@Autowired
	private DatabaseService databaseService;

	/**
	 * 등록되있는 데이터베이스 접속
	 * @param databaseId
	 * @param req
	 * @param session
	 * @return
	 */
	@GetMapping("connection/{databaseId}")
	@ResponseBody
	public String getConnnectByDatabaseId(@PathVariable("databaseId") long databaseId, HttpServletRequest req,
			HttpSession session) {
		Gson gson = new Gson();

		// 접속 얻어오기
		HashMap<String, Object> connResult = databaseService.getConnectionByDatabaseId(databaseId);

		// 접속 정보 작성
		ConnModel connModel = new ConnModel();
		connModel.setId(InternUtil.getIntId());
		connModel.setClientIp(req.getRemoteAddr());
		connModel.setConnectDate(Timestamp.valueOf(LocalDateTime.now()));
		connModel.setDatabaseId(databaseId);
		connModel.setResult((boolean) connResult.get("result"));

		// 성공시
		if (connModel.isResult()) {
			Connection conn = (Connection) connResult.get("connection");

			// 커넥션 정보 작성
			ConnSet connSet = new ConnSet();
			connSet.setConnModel(connModel);
			connSet.setConn(conn);

			// 세션에 담기
			session.setAttribute(InternUtil.getConnId(), connSet);
			InternUtil.addConnId();
		} else {
			connModel.setDatabaseId(-1);
		}

		// 접속 정보 로그 테이블에 삽입
		int insertLogResult = logService.isnertConnLog(connModel);
		if (insertLogResult == 0) {
			// TODO : 로깅
		}

		// 돌려줄 결과 만들기
		HashMap<String, Object> result = new HashMap<>();
		result.put("connModel", connModel);
		result.put("msg", connResult.get("msg"));

		return gson.toJson(result);
	}

	/**
	 * 접속 끊기
	 * @param connectionId
	 * @param session
	 * @return
	 */
	@DeleteMapping("connection/{connectionId}")
	@ResponseBody
	public String disconnectConnection(@PathVariable("connectionId") String connectionId, HttpSession session) {
		Gson gson = new Gson();
		ArrayList<Long> disconnConnIdList = new ArrayList<>();
		
		//세션을 돌면서 만들어진 커넥션 정보를 검색
		for (String connId:InternUtil.getConnIdList()) {
			ConnSet connSet = (ConnSet) session.getAttribute(connId);
			
			// 가져온 커넥션 정보중 아이디가 일치하는 정보를 찾아서 커넥션 닫고 세션 어트리부트 삭제
			if (connSet != null) {
				if(connSet.getConnModel().getId() == Long.parseLong(connectionId)) {
					JDBCTemplate.close(connSet.getConn());
					
					session.removeAttribute(connId);
					InternUtil.deleteConnId(connId);
					
					disconnConnIdList.add(connSet.getConnModel().getId());
					//TODO:로깅
				}
			}
		}
		
		HashMap<String,Object> resultMap = new HashMap<>();
		resultMap.put("disconnCount", disconnConnIdList.toArray().length);
		resultMap.put("msg", disconnConnIdList.toArray().length + "개의 접속 해제");
		resultMap.put("disconnConnIdList", disconnConnIdList);
		
		return gson.toJson(resultMap);
	}
	
	/**
	 * 모든 접속 해제
	 * @param session
	 * @return
	 */
	@DeleteMapping("allConnection")
	@ResponseBody
	public String disconnectAllConnection(HttpSession session) {
		Gson gson = new Gson();
		ArrayList<Long> disconnConnIdList = new ArrayList<>();
		
		//세션을 돌면서 만들어진 커넥션 정보를 검색
		for (String connId:InternUtil.getConnIdList()) {
			ConnSet connSet = (ConnSet) session.getAttribute(connId);
			
			// 가져온 커넥션 커넥션 닫고 세션 어트리부트 삭제
			if (connSet != null) {
				JDBCTemplate.close(connSet.getConn());
				
				session.removeAttribute(connId);
				InternUtil.deleteConnId(connId);
				
				disconnConnIdList.add(connSet.getConnModel().getId());
				//TODO:로깅
			}
		}
		
		// 결과리턴
		HashMap<String,Object> resultMap = new HashMap<>();
		resultMap.put("disconnCount", disconnConnIdList.toArray().length);
		resultMap.put("msg", disconnConnIdList.toArray().length + "개의 접속 해제");
		resultMap.put("disconnConnIdList", disconnConnIdList);
		
		return gson.toJson(resultMap);
	}
	
	/**
	 * 모든 접속 조회
	 * @param session
	 * @return
	 */
	@GetMapping("allConnection")
	@ResponseBody
	public String getAllConnection(HttpSession session) {
		Gson gson = new Gson();
		ArrayList<ConnModel> connModelList = new ArrayList<>();
		
		for (String connId:InternUtil.getConnIdList()) {
			ConnSet connSet = (ConnSet) session.getAttribute(connId);
			connModelList.add(connSet.getConnModel());
		}
		
		HashMap<String, ArrayList<ConnModel>> resultMap = new HashMap<>();
		resultMap.put("connectionList", connModelList);
		
		return gson.toJson(resultMap);
	}
}
