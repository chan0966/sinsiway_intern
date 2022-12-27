package com.sinsiway.intern.sqltool.model;

import lombok.Data;

@Data
public class RejectPolicyModel {
	private long policyId;
	private long databaseId;
	private String clientIp;
	
	private boolean result;
	private String msg;
}
