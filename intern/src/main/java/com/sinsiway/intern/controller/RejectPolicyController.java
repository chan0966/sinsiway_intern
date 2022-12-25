package com.sinsiway.intern.controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.sinsiway.intern.model.RejectPolicyModel;
import com.sinsiway.intern.service.RejectPolicyService;

@Controller
public class RejectPolicyController {
	@Autowired
	private RejectPolicyService service;

	/**
	 * 거부 아이피 추가
	 * 
	 * @param param
	 * @return
	 */
	@PostMapping("reject-policy")
	@ResponseBody
	public String InsertRejectPolicy(@RequestBody HashMap<String, String> param) {
		Gson gson = new Gson();

		HashMap<String, Object> resultMap = new HashMap<>();
		String clientIp = param.get("clientIp");
		long databaseId = 0;
		try {
			databaseId = Long.parseLong(param.get("databaseId"));
		} catch (Exception e) {
			resultMap.put("result", false);
			resultMap.put("msg", "잘못 입력된 데이터베이스 아이디입니다. 정수로 입력하세요.");
			return gson.toJson(resultMap);
		}

		RejectPolicyModel rejectPolicyModel = new RejectPolicyModel();
		rejectPolicyModel.setClientIp(clientIp);
		rejectPolicyModel.setDatabaseId(databaseId);

		rejectPolicyModel = service.InsertRejectPolicy(rejectPolicyModel);
		// 서비스 호출
		return gson.toJson(rejectPolicyModel);
	}

	/**
	 * 데이터베이스아이디로 거부 아이피 조회
	 * 
	 * @param databaseId
	 * @return
	 */
	@GetMapping("reject-policy/{databaseId}")
	@ResponseBody
	public String getRejectPolicyByDatabaseId(@PathVariable("databaseId") String databaseId) {
		Gson gson = new Gson();

		HashMap<String, Object> resultMap = new HashMap<>();
		long databaseIdL = 0;
		try {
			databaseIdL = Long.parseLong(databaseId);
		} catch (Exception e) {
			resultMap.put("result", false);
			resultMap.put("msg", "잘못 입력된 데이터베이스 아이디입니다. 정수로 입력하세요.");
			return gson.toJson(resultMap);
		}

		ArrayList<RejectPolicyModel> resultList = service.getRejectPolicyByDatabaseId(databaseIdL);

		return gson.toJson(resultList);
	}

	/**
	 * 거부 정책 삭제
	 * @param policyId
	 * @return
	 */
	@DeleteMapping("reject-policy/{policyId}")
	@ResponseBody
	public String deleteRejectPolicy(@PathVariable("policyId") String policyId) {
		Gson gson = new Gson();

		HashMap<String, Object> resultMap = new HashMap<>();
		long policyIdL = 0;
		try {
			policyIdL = Long.parseLong(policyId);
		} catch (Exception e) {
			resultMap.put("result", false);
			resultMap.put("msg", "잘못 입력된 데이터베이스 아이디입니다. 정수로 입력하세요.");
			return gson.toJson(resultMap);
		}

		resultMap = service.deleteRejectPolicy(policyIdL);

		return gson.toJson(resultMap);
	}
}