package com.sinsiway.intern.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sinsiway.intern.mapper.system.SystemDatabaseMapper;
import com.sinsiway.intern.model.ConnModel;

@Service
public class LogServiceImpl implements LogService{
	
	@Autowired
	private SystemDatabaseMapper h2Mapper;

	/**
	 * 로그 테이블에 데이터 삽입
	 */
	@Override
	public int insertConnLog(ConnModel connModel) {
		return h2Mapper.isnertConnLog(connModel);
	}

}
