package com.sinsiway.intern.controller;

import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class QueryController {
	
	@PostMapping("query")
	@ResponseBody
	public String query(@RequestBody HashMap<String, String> param) {
		long connId = 
		
		return "";
	}
}
