package com.springleaf.mybatis.transaction.jdbc;

import com.springleaf.mybatis.session.TransactionIsolationLevel;
import com.springleaf.mybatis.transaction.Transaction;
import com.springleaf.mybatis.transaction.TransactionFactory;
import com.springleaf.mybatis.transaction.jdbc.JdbcTransaction;

import javax.sql.DataSource;
import java.sql.Connection;

public class JdbcTransactionFactory implements TransactionFactory {
    @Override
    public Transaction newTransaction(Connection conn) {
        return new JdbcTransaction(conn);
    }

    @Override
    public Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit) {
        return new JdbcTransaction(dataSource, level, autoCommit);
    }
}
