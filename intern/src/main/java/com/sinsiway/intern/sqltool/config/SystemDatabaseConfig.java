package com.sinsiway.intern.sqltool.config;

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
@MapperScan(value="com.sinsiway.intern.sqltool.mapper.system", sqlSessionFactoryRef="h2SqlSessionFactory")
public class SystemDatabaseConfig {
	
	@Bean(name="h2DataSource")
	@ConfigurationProperties(prefix="spring.datasource")
	DataSource h2DataSource() {
		return DataSourceBuilder.create().build();
	}
	
	@Bean(name="h2SqlSessionFactory")
	SqlSessionFactory h2SqlSessionFactory(@Qualifier("h2DataSource") DataSource h2DataSource, ApplicationContext applicationContext) throws Exception{
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(h2DataSource);
		sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:/mapper/system/*.xml"));
		sqlSessionFactoryBean.setConfigLocation(applicationContext.getResource("classpath:/mybatis/conf.xml"));
		
		return sqlSessionFactoryBean.getObject();
	}
	
	@Bean(name="h2SqlSessionTemplate")
	SqlSessionTemplate h2SqlSessionTemplate(@Qualifier("h2SqlSessionFactory")SqlSessionFactory h2SqlSessionFactory) throws Exception{
		return new SqlSessionTemplate(h2SqlSessionFactory);
	}
}
