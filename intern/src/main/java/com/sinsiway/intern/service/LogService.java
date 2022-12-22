package com.sinsiway.intern.service;

import com.sinsiway.intern.model.ConnModel;

public interface LogService {

	/**
	 * 좁속 로그 테이블에 데이터 삽입
	 * @param connModel
	 * @return
	 */
	int isnertConnLog(ConnModel connModel);

}
