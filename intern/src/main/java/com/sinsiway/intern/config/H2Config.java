package com.sinsiway.intern.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(value="com.sinsiway.intern.mapper.h2", sqlSessionFactoryRef="h2SqlSessionFactory")
public class H2Config {
	
	@Bean(name="h2DataSource")
	@ConfigurationProperties(prefix="spring.datasource")
	DataSource h2DataSource() {
		//application.properties에서 정의한 DB 연결 정보를 빌드
		return DataSourceBuilder.create().build();
	}
	
	@Bean(name="h2SqlSessionFactory")
	SqlSessionFactory h2SqlSessionFactory(@Qualifier("h2DataSource") DataSource h2DataSource, ApplicationContext applicationContext) throws Exception{
		//세션 생성 시, 빌드된 DataSource를 세팅하고 SQL문을 관리할 mapper.xml의 경로를 알려준다.
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(h2DataSource);
		sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:/mapper/h2/*.xml"));
		sqlSessionFactoryBean.setConfigLocation(applicationContext.getResource("classpath:/mybatis/conf.xml"));
		
		return sqlSessionFactoryBean.getObject();
	}
	
	@Bean(name="h2SqlSessionTemplate")
	SqlSessionTemplate h2SqlSessionTemplate(@Qualifier("h2SqlSessionFactory")SqlSessionFactory h2SqlSessionFactory) throws Exception{
		return new SqlSessionTemplate(h2SqlSessionFactory);
	}
}
