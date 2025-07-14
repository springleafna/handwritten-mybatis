package com.springleaf.mybatis.scripting;

import com.springleaf.mybatis.executor.parameter.ParameterHandler;
import com.springleaf.mybatis.mapping.BoundSql;
import com.springleaf.mybatis.mapping.MappedStatement;
import com.springleaf.mybatis.mapping.SqlSource;
import com.springleaf.mybatis.session.Configuration;
import org.dom4j.Element;

/**
 * 脚本语言驱动
 */
public interface LanguageDriver {

    /**
     * 创建SQL源码(mapper xml方式)
     */
    SqlSource createSqlSource(Configuration configuration, Element script, Class<?> parameterType);

    /**
     * 创建SQL源码(annotation 注解方式)
     */
    SqlSource createSqlSource(Configuration configuration, String script, Class<?> parameterType);


    /**
     * 创建参数处理器
     */
    ParameterHandler createParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql);

}
