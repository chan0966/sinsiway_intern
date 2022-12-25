package com.sinsiway.intern.service;

import java.sql.Connection;

import com.sinsiway.intern.model.ExecuteQueryModel;

public interface QueryService {

	Object execute(ExecuteQueryModel parentExecuteQueryModel, Connection conn, String sqlText);

}
