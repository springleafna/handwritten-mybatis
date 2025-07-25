package com.springleaf.mybatis.executor.keygen;

import com.springleaf.mybatis.executor.Executor;
import com.springleaf.mybatis.executor.keygen.KeyGenerator;
import com.springleaf.mybatis.mapping.MappedStatement;

import java.sql.Statement;

/**
 * 不用键值生成器
 */
public class NoKeyGenerator implements KeyGenerator {

    @Override
    public void processBefore(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {
        // Do Nothing
    }

    @Override
    public void processAfter(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {
        // Do Nothing
    }

}
