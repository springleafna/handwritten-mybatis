package com.springleaf.mybatis.test.plugin;

import com.springleaf.mybatis.executor.statement.StatementHandler;
import com.springleaf.mybatis.mapping.BoundSql;
import com.springleaf.mybatis.plugin.Interceptor;
import com.springleaf.mybatis.plugin.Intercepts;
import com.springleaf.mybatis.plugin.Invocation;
import com.springleaf.mybatis.plugin.Signature;

import java.sql.Connection;
import java.util.Properties;

@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class})})
public class TestPlugin implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 获取StatementHandler
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        // 获取SQL信息
        BoundSql boundSql = statementHandler.getBoundSql();
        String sql = boundSql.getSql();
        // 输出SQL
        System.out.println("拦截SQL：" + sql);
        // 放行
        return invocation.proceed();
    }

    @Override
    public void setProperties(Properties properties) {
        System.out.println("参数输出：" + properties.getProperty("test00"));
    }

}
