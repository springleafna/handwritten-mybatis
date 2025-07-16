package com.springleaf.mybatis.executor.statement;

import com.springleaf.mybatis.executor.Executor;
import com.springleaf.mybatis.executor.statement.BaseStatementHandler;
import com.springleaf.mybatis.mapping.BoundSql;
import com.springleaf.mybatis.mapping.MappedStatement;
import com.springleaf.mybatis.session.ResultHandler;
import com.springleaf.mybatis.session.RowBounds;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * 简单语句处理器 ，用于处理静态 SQL 语句（不带参数，或使用 ${} 拼接 SQL）。
 * 它使用 Statement，直接拼接 SQL 字符串，不进行参数绑定。
 */
public class SimpleStatementHandler extends BaseStatementHandler {

    public SimpleStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameterObject, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        super(executor, mappedStatement, parameterObject, rowBounds, resultHandler, boundSql);
    }

    @Override
    protected Statement instantiateStatement(Connection connection) throws SQLException {
        return connection.createStatement();
    }

    @Override
    public void parameterize(Statement statement) throws SQLException {
        // N/A
    }

    @Override
    public int update(Statement statement) throws SQLException {
        String sql = boundSql.getSql();
        statement.execute(sql);
        return statement.getUpdateCount();
    }

    @Override
    public <E> List<E> query(Statement statement, ResultHandler resultHandler) throws SQLException {
        String sql = boundSql.getSql();
        statement.execute(sql);
        return resultSetHandler.handleResultSets(statement);
    }
}
