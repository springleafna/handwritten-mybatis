package com.springleaf.mybatis.mapping;

import com.springleaf.mybatis.session.Configuration;
import com.springleaf.mybatis.type.JdbcType;
import com.springleaf.mybatis.type.TypeHandler;

/**
 * 结果映射
 */
public class ResultMapping {

    private Configuration configuration;
    private String property;
    private String column;
    private Class<?> javaType;
    private JdbcType jdbcType;
    private TypeHandler<?> typeHandler;

    ResultMapping() {
    }

    public static class Builder {
        private ResultMapping resultMapping = new ResultMapping();
    }
}
