package com.sinsiway.intern.sqltool.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sinsiway.intern.sqltool.mapper.system.SystemDatabaseMapper;
import com.sinsiway.intern.sqltool.model.DatabaseModel;
import com.sinsiway.intern.sqltool.model.RejectPolicyModel;

@Service
public class RejectPolicyServiceImpl implements RejectPolicyService {

	@Autowired
	private SystemDatabaseMapper Mapper;

	/**
	 * 거부 아이피 추가
	 */
	@Override
	public RejectPolicyModel InsertRejectPolicy(RejectPolicyModel rejectPolicyModel) {
		DatabaseModel database = Mapper.selectDatabaseById(rejectPolicyModel.getDatabaseId());
		if (database == null) {
			rejectPolicyModel.setResult(false);
			rejectPolicyModel.setMsg("존재하지않는 데이터베이스입니다.");
			return rejectPolicyModel;
		}
		try {
			int result = Mapper.InsertRejectPolicy(rejectPolicyModel);
			if (result != 0) {
				rejectPolicyModel.setResult(true);
				rejectPolicyModel.setMsg("거부 아이피 추가 성공.");
			} else {
				rejectPolicyModel.setResult(false);
				rejectPolicyModel.setMsg("거부 아이피 추가 실패.");
			}
		} catch (Exception e) {
			rejectPolicyModel.setResult(false);
			rejectPolicyModel.setMsg(e.getMessage());
		}
		return rejectPolicyModel;

	}

	/**
	 * 데이터베이스 아이디로 거부 아이피 조회
	 */
	@Override
	public ArrayList<RejectPolicyModel> getRejectPolicyByDatabaseId(long databaseIdL) {
		ArrayList<RejectPolicyModel> noDatabase = new ArrayList<>();
		RejectPolicyModel rejectPolicyModel = new RejectPolicyModel();
		rejectPolicyModel.setDatabaseId(databaseIdL);
		DatabaseModel database = Mapper.selectDatabaseById(rejectPolicyModel.getDatabaseId());
		if (database == null) {
			rejectPolicyModel.setResult(false);
			rejectPolicyModel.setMsg("존재하지않는 데이터베이스입니다.");
			noDatabase.add(rejectPolicyModel);
			
			return noDatabase;
		}
		
		return Mapper.getRejectPolicyByDatabaseId(databaseIdL);
	}

	/**
	 * 거부 정책 삭제
	 */
	@Override
	public HashMap<String, Object> deleteRejectPolicy(long policyIdL) {
		int result = Mapper.deleteRejectPolicy(policyIdL);
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		if(result != 0) {
			resultMap.put("result", true);
			resultMap.put("msg", "거부 아이피 삭제 성공");
		}else {
			resultMap.put("result", false);
			resultMap.put("msg", "거부 아이피 삭제 실패");
		}
		
		return resultMap;
	}

}
