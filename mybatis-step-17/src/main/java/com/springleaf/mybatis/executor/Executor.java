package com.springleaf.mybatis.executor;

import com.springleaf.mybatis.cache.CacheKey;
import com.springleaf.mybatis.mapping.BoundSql;
import com.springleaf.mybatis.mapping.MappedStatement;
import com.springleaf.mybatis.session.ResultHandler;
import com.springleaf.mybatis.session.RowBounds;
import com.springleaf.mybatis.transaction.Transaction;

import java.sql.SQLException;
import java.util.List;

/**
 * 执行器
 * 负责接收 SQL、参数等信息，并最终调用 JDBC 执行 SQL（增删改查），同时管理一级缓存、二级缓存、事务等。
 */
public interface Executor {

    ResultHandler NO_RESULT_HANDLER = null;

    int update(MappedStatement ms, Object parameter) throws SQLException;

    // 查询，含缓存
    <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, CacheKey key, BoundSql boundSql) throws SQLException;

    // 查询
    <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler) throws SQLException;

    Transaction getTransaction();

    void commit(boolean required) throws SQLException;

    void rollback(boolean required) throws SQLException;

    void close(boolean forceRollback);

    // 清理Session缓存
    void clearLocalCache();

    // 创建缓存 Key
    CacheKey createCacheKey(MappedStatement ms, Object parameterObject, RowBounds rowBounds, BoundSql boundSql);

    void setExecutorWrapper(Executor executor);

}
