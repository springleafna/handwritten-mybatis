package com.springleaf.mybatis.mapping;

/**
 * SQL源码
 */
public interface SqlSource {

    BoundSql getBoundSql(Object parameterObject);

}
