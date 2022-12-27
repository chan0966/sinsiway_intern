package com.sinsiway.intern.sqltool.controller;

import java.sql.Connection;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.sinsiway.intern.sqltool.model.ConnModel;
import com.sinsiway.intern.sqltool.model.ConnSet;
import com.sinsiway.intern.sqltool.service.ConnectionService;
import com.sinsiway.intern.sqltool.service.LogService;
import com.sinsiway.intern.sqltool.util.ConnIdUtill;
import com.sinsiway.intern.sqltool.util.JDBCTemplate;

@Controller
public class ConnectionController {

	@Autowired
	private LogService logService;
	@Autowired
	private ConnectionService connectionService;

	/**
	 * 등록되있는 데이터베이스 접속
	 * 
	 * @param databaseId
	 * @param req
	 * @param session
	 * @return
	 */
	@GetMapping("connection/{databaseId}")
	@ResponseBody
	public Map<String, Object> getConnnectByDatabaseId(@PathVariable("databaseId") String databaseIdStr,
			HttpServletRequest req, HttpSession session) {
		HashMap<String, Object> result = new HashMap<>();

		// 접속 정보 작성
		ConnModel connModel = new ConnModel();
		connModel.setId(ConnIdUtill.getIntId());
		connModel.setClientIp(req.getRemoteAddr());
		connModel.setConnectDate(Timestamp.valueOf(LocalDateTime.now()));

		long databaseId = 0;
		try {
			databaseId = Long.parseLong(databaseIdStr);
		} catch (Exception e) {
			connModel.setResult(false);
			result.put("connModel", connModel);
			result.put("msg", "올바르지 않은 데이터베이스 아이디입니다. 정수로 입력해주세요");
		}

		// 접속 얻어오기
		HashMap<String, Object> connResult = connectionService.getConnectionByDatabaseId(databaseId,
				req.getRemoteAddr());

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
			session.setAttribute(ConnIdUtill.getConnId(), connSet);
			ConnIdUtill.addConnId();
		}

		// 접속 정보 로그 테이블에 삽입
		if (connModel.isResult()) {
			logService.insertConnLog(connModel);
		}

		// 돌려줄 결과 만들기
		result.put("connModel", connModel);
		result.put("msg", connResult.get("msg"));

		return result;
	}

	/**
	 * 접속 끊기
	 * 
	 * @param connectionId
	 * @param session
	 * @return
	 */
	@DeleteMapping("connection/{connectionId}")
	@ResponseBody
	public Map<String, Object> disconnectConnection(@PathVariable("connectionId") String connectionIdStr,
			HttpSession session) {
		ArrayList<Long> disconnConnIdList = new ArrayList<>();
		HashMap<String, Object> resultMap = new HashMap<>();

		long connectionId = 0;
		try {
			connectionId = Long.parseLong(connectionIdStr);
		} catch (Exception e) {
			resultMap.put("result", false);
			resultMap.put("msg", "올바르지 않은 접속 아이디입니다. 정수로 입력해주세요");
			return resultMap;
		}

		// 세션을 돌면서 만들어진 커넥션 정보를 검색
		ArrayList<String> deleteList = new ArrayList<String>();
		for (String connId : ConnIdUtill.getConnIdList()) {
			ConnSet connSet = (ConnSet) session.getAttribute(connId);

			// 가져온 커넥션 정보중 아이디가 일치하는 정보를 찾아서 커넥션 닫고 세션 어트리부트 삭제
			if (connSet != null) {
				if (connSet.getConnModel().getId() == connectionId) {
					JDBCTemplate.close(connSet.getConn());

					session.removeAttribute(connId);
					deleteList.add(connId);
					disconnConnIdList.add(connSet.getConnModel().getId());
				}
			}
		}
		ConnIdUtill.deleteConnId(deleteList);

		resultMap.put("disconnCount", disconnConnIdList.toArray().length);
		resultMap.put("msg", disconnConnIdList.toArray().length + "개의 접속 해제");
		resultMap.put("disconnConnIdList", disconnConnIdList);

		return resultMap;
	}

	/**
	 * 모든 접속 해제
	 * 
	 * @param session
	 * @return
	 */
	@DeleteMapping("allconnection")
	@ResponseBody
	public Map<String, Object> disconnectAllConnection(HttpSession session) {
		ArrayList<Long> disconnConnIdList = new ArrayList<>();

		// 세션을 돌면서 만들어진 커넥션 정보를 검색
		ArrayList<String> deleteList = new ArrayList<String>();
		for (String connId : ConnIdUtill.getConnIdList()) {
			ConnSet connSet = (ConnSet) session.getAttribute(connId);

			// 가져온 커넥션 커넥션 닫고 세션 어트리부트 삭제
			if (connSet != null) {
				JDBCTemplate.close(connSet.getConn());
				session.removeAttribute(connId);

				deleteList.add(connId);

				disconnConnIdList.add(connSet.getConnModel().getId());
			}
		}
		ConnIdUtill.deleteConnId(deleteList);

		// 결과리턴
		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("disconnCount", disconnConnIdList.toArray().length);
		resultMap.put("msg", disconnConnIdList.toArray().length + "개의 접속 해제");
		resultMap.put("disconnConnIdList", disconnConnIdList);

		return resultMap;
	}

	/**
	 * 모든 접속 조회
	 * 
	 * @param session
	 * @return
	 */
	@GetMapping("allconnection")
	@ResponseBody
	public Map<String, ArrayList<ConnModel>> getAllConnection(HttpSession session) {
		ArrayList<ConnModel> connModelList = new ArrayList<>();

		for (String connId : ConnIdUtill.getConnIdList()) {
			ConnSet connSet = (ConnSet) session.getAttribute(connId);
			if (connSet != null) {
				connModelList.add(connSet.getConnModel());
			}
		}

		HashMap<String, ArrayList<ConnModel>> resultMap = new HashMap<>();
		resultMap.put("connectionList", connModelList);

		return resultMap;
	}
}
