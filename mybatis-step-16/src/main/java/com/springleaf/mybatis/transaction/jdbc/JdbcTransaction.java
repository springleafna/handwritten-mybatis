package com.springleaf.mybatis.transaction.jdbc;

import com.springleaf.mybatis.session.TransactionIsolationLevel;
import com.springleaf.mybatis.transaction.Transaction;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class JdbcTransaction implements Transaction {
    
    protected Connection connection;
    protected DataSource dataSource;
    protected boolean autoCommit;
    protected TransactionIsolationLevel level = TransactionIsolationLevel.NONE;

    public JdbcTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit) {
        this.dataSource = dataSource;
        this.autoCommit = autoCommit;
        this.level = level;
    }

    public JdbcTransaction(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (null != connection) {
            return connection;
        }
        connection = dataSource.getConnection();
        connection.setTransactionIsolation(level.getLevel());
        connection.setAutoCommit(autoCommit);
        return connection;
    }

    @Override
    public void commit() throws SQLException {
        if (connection != null && !connection.getAutoCommit()) {
            connection.commit();
        }
    }

    @Override
    public void rollback() throws SQLException {
        if (connection != null && !connection.getAutoCommit()) {
            connection.rollback();
        }
    }

    @Override
    public void close() throws SQLException {
        if (connection != null && !connection.getAutoCommit()) {
            connection.close();
        }
    }
}
