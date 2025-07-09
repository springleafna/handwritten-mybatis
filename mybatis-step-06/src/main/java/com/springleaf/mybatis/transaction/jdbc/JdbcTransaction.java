package com.springleaf.mybatis.transaction.jdbc;

import com.springleaf.mybatis.session.TransactionIsolationLevel;
import com.springleaf.mybatis.transaction.Transaction;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class JdbcTransaction implements Transaction {
    
    protected Connection connection;
    protected DataSource dataSource;
    protected boolean autocommit;
    protected TransactionIsolationLevel level = TransactionIsolationLevel.NONE;

    public JdbcTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autocommit) {
        this.dataSource = dataSource;
        this.autocommit = autocommit;
        this.level = level;
    }

    public JdbcTransaction(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Connection getConnection() throws SQLException {
        connection = dataSource.getConnection();
        connection.setAutoCommit(autocommit);
        connection.setTransactionIsolation(level.getLevel());
        connection.setAutoCommit(autocommit);
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
