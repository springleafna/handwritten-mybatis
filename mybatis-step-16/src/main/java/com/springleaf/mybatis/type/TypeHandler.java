package com.springleaf.mybatis.type;

import com.springleaf.mybatis.type.JdbcType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 类型处理器
 * @param <T> 参数类型
 */
public interface TypeHandler<T> {
    /**
     * 设置参数
     */
    void setParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException;

    /**
     * 获取结果
     */
    T getResult(ResultSet rs, String columnName) throws SQLException;

    /**
     * 获取结果
     */
    T getResult(ResultSet rs, int columnIndex) throws SQLException;

}
