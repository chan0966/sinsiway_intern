package com.sinsiway.intern.sqltool.config;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.stereotype.Component;

import com.sinsiway.intern.sqltool.model.ConnSet;
import com.sinsiway.intern.sqltool.util.ConnIdUtill;
import com.sinsiway.intern.sqltool.util.JDBCTemplate;

@Component
@WebListener
public class InternSessionListener implements HttpSessionListener{
	
	/**
	 * 세션 만료시 남아있는 커넥션 닫기
	 */
	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		HttpSession session = se.getSession();
		
		for (String connId:ConnIdUtill.getConnIdList()) {
			ConnSet connSet = (ConnSet) session.getAttribute(connId);
			
			// 가져온 커넥션 커넥션 닫고 세션 어트리부트 삭제
			if (connSet != null) {
				JDBCTemplate.close(connSet.getConn());
			}
		}
	}
}
