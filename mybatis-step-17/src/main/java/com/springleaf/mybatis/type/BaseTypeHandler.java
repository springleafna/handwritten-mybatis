package com.springleaf.mybatis.type;

import com.springleaf.mybatis.session.Configuration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 类型处理器的基类
 */
public abstract class BaseTypeHandler<T> implements TypeHandler<T> {

    protected Configuration configuration;

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void setParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        // 判断传入的 parameter 实参是否为空，如果为空，则调用 PreparedStatement.setNull() 方法进行设置；如果不为空，则委托 setNonNullParameter() 这个抽象方法进行处理
        if (parameter == null) {
            if (jdbcType == null) {
                throw new RuntimeException("JDBC requires that the JdbcType must be specified for all nullable parameters.");
            }
            try {
                ps.setNull(i, jdbcType.TYPE_CODE);
            } catch (SQLException e) {
                throw new RuntimeException("Error setting null for parameter #" + i + " with JdbcType " + jdbcType + " . " +
                        "Try setting a different JdbcType for this parameter or a different jdbcTypeForNull configuration property. " +
                        "Cause: " + e, e);
            }
        } else {
            // 定义抽象方法，由子类实现不同类型的属性设置
            setNonNullParameter(ps, i, parameter, jdbcType);
        }
    }

    @Override
    public T getResult(ResultSet rs, String columnName) throws SQLException {
        return getNullableResult(rs, columnName);
    }

    @Override
    public T getResult(ResultSet rs, int columnIndex) throws SQLException {
        return getNullableResult(rs, columnIndex);
    }

    protected abstract void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException;

    protected abstract T getNullableResult(ResultSet rs, String columnName) throws SQLException;

    public abstract T getNullableResult(ResultSet rs, int columnIndex) throws SQLException;

}
