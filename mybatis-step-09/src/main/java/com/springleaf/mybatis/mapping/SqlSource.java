package com.springleaf.mybatis.mapping;

/**
 * SQL源码
 * 代表从XML⽂件或者注解读取的映射语句的内容,它创建的SQL会被传递给数据库。
 */
public interface SqlSource {

    BoundSql getBoundSql(Object parameterObject);

}
