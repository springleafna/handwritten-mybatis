package com.springleaf.mybatis.spring;

import com.springleaf.mybatis.io.Resources;
import com.springleaf.mybatis.session.SqlSessionFactory;
import com.springleaf.mybatis.session.SqlSessionFactoryBuilder;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.io.Reader;

/**
 * 创建 SqlSessionFactory 的 Spring Bean，它是 MyBatis 的核心工厂类。
 */
public class SqlSessionFactoryBean implements FactoryBean<SqlSessionFactory>, InitializingBean {

    private String resource;

    private SqlSessionFactory sqlSessionFactory;

    @Override
    public SqlSessionFactory getObject() throws Exception {
        return sqlSessionFactory;
    }

    @Override
    public Class<?> getObjectType() {
        return SqlSessionFactory.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try (Reader reader = Resources.getResourceAsReader(resource)) {
            this.sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

}
