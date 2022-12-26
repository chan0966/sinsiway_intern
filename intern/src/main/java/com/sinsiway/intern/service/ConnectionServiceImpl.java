package com.sinsiway.intern.service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sinsiway.intern.mapper.system.SystemDatabaseMapper;
import com.sinsiway.intern.model.DatabaseModel;
import com.sinsiway.intern.model.RejectPolicyModel;
import com.sinsiway.intern.util.JDBCTemplate;

@Service
public class ConnectionServiceImpl implements ConnectionService{
	@Autowired
	private SystemDatabaseMapper h2Mapper;
	
	/**
	 * 데이터베이스 아이디로 커넥션결과 가져오기
	 */
	@Override
	public HashMap<String, Object> getConnectionByDatabaseId(long databaseId, String clientIp) {
		HashMap<String, Object> connResult = new HashMap<>();
		DatabaseModel databaseModel = h2Mapper.selectDatabaseById(databaseId);
		
		//실패시 결과, 메세지 담아서 반환
		if(databaseModel == null) {
			connResult.put("result", false);
			connResult.put("msg", "데이터베이스를 찾을 수 없습니다.");
			
		}else {
			ArrayList<RejectPolicyModel> rejectPolicyModelList = h2Mapper.getRejectPolicyByDatabaseId(databaseId);
			for(RejectPolicyModel model:rejectPolicyModelList) {
				if(clientIp.equals(model.getClientIp())) {
					connResult.put("result", false);
					connResult.put("msg", "거부 정책에 의해 접속 거부됨 policy-id::" + model.getPolicyId());
					return connResult;
				}
			}
			
			//성공시 커넥션 얻어서 반환
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
