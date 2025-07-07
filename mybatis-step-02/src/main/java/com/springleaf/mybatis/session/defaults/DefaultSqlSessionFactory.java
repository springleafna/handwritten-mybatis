package com.springleaf.mybatis.session.defaults;

import com.springleaf.mybatis.binding.MapperRegistry;
import com.springleaf.mybatis.session.Configuration;
import com.springleaf.mybatis.session.SqlSession;
import com.springleaf.mybatis.session.SqlSessionFactory;
import com.springleaf.mybatis.session.defaults.DefaultSqlSession;

public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private final Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(configuration);
    }
}
