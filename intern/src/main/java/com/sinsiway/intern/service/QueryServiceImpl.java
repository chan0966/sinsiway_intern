package com.sinsiway.intern.service;

import java.sql.Connection;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sinsiway.intern.mapper.system.SystemDatabaseMapper;
import com.sinsiway.intern.model.ExecuteQueryModel;
import com.sinsiway.intern.repository.QueryDao;
import com.sinsiway.intern.repository.SqlType;
import com.sinsiway.intern.util.ConnIdUtill;

@Service
public class QueryServiceImpl implements QueryService {
	@Autowired
	private QueryDao dao;
	@Autowired
	private SystemDatabaseMapper h2Mapper;

	@Override
	public Object execute(ExecuteQueryModel parentExecuteQueryModel, Connection conn, String sqlText) {
		String[] sqlTextArr = sqlText.split(";");

		ArrayList<ExecuteQueryModel> resultExecuteQueryModelList = new ArrayList<>();

		// sqlTextArr별로 각각 작업
		for (String sqlTextEle : sqlTextArr) {
			String sqlType = sqlTextEle.trim().split(" ")[0].toUpperCase();

			// 수행 모델 만들기
			ExecuteQueryModel childExecuteQueryModel = null;
			try {
				childExecuteQueryModel = (ExecuteQueryModel) parentExecuteQueryModel.clone();
			} catch (CloneNotSupportedException e1) {
				continue;
			}
			childExecuteQueryModel.setId(ConnIdUtill.getIntId());
			childExecuteQueryModel.setSqlText(sqlTextEle);
			childExecuteQueryModel.setSqlType(sqlType);

			// sqlType별로 각각 작업
			switch (sqlType) {
			case "SELECT":
				try {
					ArrayList<ArrayList<Object>> resultSet = dao.executeQuery(conn, sqlTextEle);
					childExecuteQueryModel.setResultSet(resultSet);
					childExecuteQueryModel.setResultAndMsg(true, (resultSet.toArray().length-1) + "개의 행이 검색됨.");
					// TODO : 로깅
				} catch (Exception e) {
					childExecuteQueryModel.setResultAndMsg(false, e.getMessage());
					// TODO : 로깅
				}
				break;

			case "COMMIT":
			case "ROLLBACK":
				try {
					if ("COMMIT".equals(sqlType)) {
						dao.commit(conn);
					} else {
						dao.rollback(conn);
					}
					childExecuteQueryModel.setResultAndMsg(true, sqlType + " 성공.");
					// TODO : 로깅
				} catch (Exception e) {
					childExecuteQueryModel.setResultAndMsg(false, sqlType + " 실패." + e.getMessage());
					// TODO : 로깅
				}
				break;
				
			case "":break;
			
			default:
				try {
					SqlType sqlTypeObj = (SqlType) Class.forName("com.sinsiway.intern.util.sqltype."+sqlType).getDeclaredConstructor().newInstance();
					sqlTypeObj.execute(conn, sqlTextEle);
					childExecuteQueryModel.setResultAndMsg(sqlTypeObj.getResult(), sqlTypeObj.getMsg());
				} catch (Exception e) {
					childExecuteQueryModel.setResultAndMsg(false, "지원하지않는 SQL 구문입니다 :" + sqlType);
				}
				break;
			}

			if (!"".equals(sqlTextEle)) {
				int insertLogResult = h2Mapper.insertExecuteLog(childExecuteQueryModel);
				if (insertLogResult == 0) {
					// TODO : 로깅
				}
				resultExecuteQueryModelList.add(childExecuteQueryModel);
			}
		}

		return resultExecuteQueryModelList;
	}

}
