package com.springleaf.mybatis.executor.statement;

import com.springleaf.mybatis.executor.Executor;
import com.springleaf.mybatis.executor.parameter.ParameterHandler;
import com.springleaf.mybatis.executor.resultset.ResultSetHandler;
import com.springleaf.mybatis.mapping.BoundSql;
import com.springleaf.mybatis.mapping.MappedStatement;
import com.springleaf.mybatis.session.Configuration;
import com.springleaf.mybatis.session.ResultHandler;
import com.springleaf.mybatis.session.RowBounds;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 语句处理器抽象基类
 */
public abstract class BaseStatementHandler implements StatementHandler {

    protected final Configuration configuration;
    protected final Executor executor;
    protected final MappedStatement mappedStatement;

    protected final Object parameterObject;
    protected final ResultSetHandler resultSetHandler;
    protected final ParameterHandler parameterHandler;

    protected final RowBounds rowBounds;
    protected BoundSql boundSql;

    public BaseStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameterObject, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        this.configuration = mappedStatement.getConfiguration();
        this.executor = executor;
        this.mappedStatement = mappedStatement;
        this.rowBounds = rowBounds;

        // 因为 update 不会传入 boundSql 参数，所以这里要做初始化处理
        if (boundSql == null) {
            boundSql = mappedStatement.getBoundSql(parameterObject);
        }

        this.boundSql = boundSql;

        this.parameterObject = parameterObject;
        this.parameterHandler = configuration.newParameterHandler(mappedStatement, parameterObject, boundSql);
        this.resultSetHandler = configuration.newResultSetHandler(executor, mappedStatement, rowBounds, resultHandler, boundSql);
    }

    @Override
    public Statement prepare(Connection connection) throws SQLException {
        Statement statement = null;
        try {
            // 实例化 Statement
            statement = instantiateStatement(connection);
            // 参数设置，可以被抽取，提供配置
            statement.setQueryTimeout(350);
            statement.setFetchSize(10000);
            return statement;
        } catch (Exception e) {
            throw new RuntimeException("Error preparing statement.  Cause: " + e, e);
        }
    }

    protected abstract Statement instantiateStatement(Connection connection) throws SQLException;

}
