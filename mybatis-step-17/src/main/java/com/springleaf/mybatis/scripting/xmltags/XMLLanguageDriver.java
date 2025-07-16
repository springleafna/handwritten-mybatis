package com.springleaf.mybatis.scripting.xmltags;

import com.springleaf.mybatis.executor.parameter.ParameterHandler;
import com.springleaf.mybatis.mapping.BoundSql;
import com.springleaf.mybatis.mapping.MappedStatement;
import com.springleaf.mybatis.mapping.SqlSource;
import com.springleaf.mybatis.scripting.LanguageDriver;
import com.springleaf.mybatis.scripting.defaults.DefaultParameterHandler;
import com.springleaf.mybatis.scripting.defaults.RawSqlSource;
import com.springleaf.mybatis.scripting.xmltags.XMLScriptBuilder;
import com.springleaf.mybatis.session.Configuration;
import org.dom4j.Element;

/**
 * XML语言驱动器
 */
public class XMLLanguageDriver implements LanguageDriver {

    @Override
    public SqlSource createSqlSource(Configuration configuration, Element script, Class<?> parameterType) {
        // 用XML脚本构建器解析
        XMLScriptBuilder builder = new XMLScriptBuilder(configuration, script, parameterType);
        return builder.parseScriptNode();
    }

    /**
     * 用于处理注解配置 SQL 语句
     */
    @Override
    public SqlSource createSqlSource(Configuration configuration, String script, Class<?> parameterType) {
        // 暂时不解析动态 SQL
        return new RawSqlSource(configuration, script, parameterType);
    }

    @Override
    public ParameterHandler createParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
        return new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
    }

}