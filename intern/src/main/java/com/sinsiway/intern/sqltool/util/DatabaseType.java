package com.sinsiway.intern.sqltool.util;

public enum DatabaseType {
	POSTGRE("postgresql", "org.postgresql.Driver")
	, MARIA("mariadb", "org.mariadb.jdbc.Driver");
	
	String dbname;
	String driverClassName;

	DatabaseType(String dbname, String driverClassName) {
		this.dbname = dbname;
		this.driverClassName = driverClassName;
	}
	
	public String getDbName() {
		return dbname;
	}
	
	public String getDriverClassName() {
		return driverClassName;
	}
}
