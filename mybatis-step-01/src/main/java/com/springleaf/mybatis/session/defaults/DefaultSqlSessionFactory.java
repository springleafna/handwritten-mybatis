package com.springleaf.mybatis.session.defaults;

import com.springleaf.mybatis.binding.MapperRegistry;
import com.springleaf.mybatis.session.SqlSession;
import com.springleaf.mybatis.session.SqlSessionFactory;

public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private final MapperRegistry mapperRegistry;

    public DefaultSqlSessionFactory(MapperRegistry mapperRegistry) {
        this.mapperRegistry = mapperRegistry;
    }

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(mapperRegistry);
    }
}
