package com.sinsiway.intern.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sinsiway.intern.mapper.h2.H2Mapper;
import com.sinsiway.intern.model.ConnModel;

@Service
public class LogServiceImpl implements LogService{
	
	@Autowired
	private H2Mapper h2Mapper;

	/**
	 * 로그 테이블에 데이터 삽입
	 */
	@Override
	public int isnertConnLog(ConnModel connModel) {
		return h2Mapper.isnertConnLog(connModel);
	}

}
