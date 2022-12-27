package com.sinsiway.intern.sqltool.service;

import java.sql.Connection;

import com.sinsiway.intern.sqltool.model.ExecuteQueryModel;

public interface QueryService {

	Object execute(ExecuteQueryModel parentExecuteQueryModel, Connection conn, String sqlText);

}
