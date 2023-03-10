package com.sinsiway.intern.sqltool.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sinsiway.intern.sqltool.model.ConnSet;
import com.sinsiway.intern.sqltool.model.ExecuteQueryModel;
import com.sinsiway.intern.sqltool.service.QueryService;
import com.sinsiway.intern.sqltool.util.ConnIdUtill;

@Controller
public class QueryController {
	@Autowired
	private QueryService queryService;

	@PostMapping("query")
	@ResponseBody
	public Object query(@RequestBody HashMap<String, String> param, HttpSession session, HttpServletRequest req) {
		long connId = Long.parseLong(param.get("connId"));
		String sqlText = param.get("sqlText");
		
		ConnSet connSet = null;
		
		//존재하는 커넥션 검색
		for(String connSetId:ConnIdUtill.getConnIdList()) {
			ConnSet sessionConnSet = (ConnSet) session.getAttribute(connSetId);
			if(sessionConnSet != null) {	
				if(sessionConnSet.getConnModel().getId() == connId) {
					connSet = sessionConnSet;
				}
			}
		}
		
		// 세션에 존재하는 커넥션이 없을 경우
		if(connSet == null) {
			HashMap<String, Object> noConnSetResult = new HashMap<>();
			noConnSetResult.put("result", false);
			noConnSetResult.put("msg", "접속이 존재하지 않습니다.");
			return noConnSetResult;
		}

		// 쿼리 수행 객체 작성
		ExecuteQueryModel parentExecuteQueryModel = new ExecuteQueryModel();
		parentExecuteQueryModel.setClientIp(req.getRemoteAddr());
		parentExecuteQueryModel.setExecDate(Timestamp.valueOf(LocalDateTime.now()));
		parentExecuteQueryModel.setDatabaseId(connSet.getConnModel().getDatabaseId());
		parentExecuteQueryModel.setConnId(connId);
		
		// 서비스 호출
		Object resultObject = queryService.execute(parentExecuteQueryModel, connSet.getConn(), sqlText);
		
		return resultObject;
	}
}
