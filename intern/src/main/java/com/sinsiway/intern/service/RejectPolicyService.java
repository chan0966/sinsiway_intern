package com.sinsiway.intern.service;

import java.util.ArrayList;
import java.util.HashMap;

import com.sinsiway.intern.model.RejectPolicyModel;

public interface RejectPolicyService {

	/**
	 * 거부 아이피 추가
	 * @param rejectPolicyModel
	 * @return
	 */
	RejectPolicyModel InsertRejectPolicy(RejectPolicyModel rejectPolicyModel);

	/**
	 * 데이터베이스 아이디로 거부 아이피 조회
	 * @param databaseIdL
	 * @return
	 */
	ArrayList<RejectPolicyModel> getRejectPolicyByDatabaseId(long databaseIdL);

	/**
	 * 거부 정책 삭제
	 * @param policyIdL
	 * @return
	 */
	HashMap<String, Object> deleteRejectPolicy(long policyIdL);

}
