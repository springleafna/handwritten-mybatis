package com.springleaf.mybatis.builder;

import com.springleaf.mybatis.mapping.BoundSql;
import com.springleaf.mybatis.mapping.ParameterMapping;
import com.springleaf.mybatis.mapping.SqlSource;
import com.springleaf.mybatis.session.Configuration;

import java.util.List;

/**
 * 静态SQL源码，其他类型的SqlSource最终都委托给StaticSqlSource
 */
public class StaticSqlSource implements SqlSource {

    private String sql;
    private List<ParameterMapping> parameterMappings;
    private Configuration configuration;

    public StaticSqlSource(Configuration configuration, String sql) {
        this(configuration, sql, null);
    }

    public StaticSqlSource(Configuration configuration, String sql, List<ParameterMapping> parameterMappings) {
        this.sql = sql;
        this.parameterMappings = parameterMappings;
        this.configuration = configuration;
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        return new BoundSql(configuration, sql, parameterMappings, parameterObject);
    }
}
