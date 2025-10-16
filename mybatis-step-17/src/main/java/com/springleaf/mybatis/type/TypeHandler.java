package com.springleaf.mybatis.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 类型处理器
 * @param <T> 参数类型
 * 用于将 Java 类型与 JDBC 类型之间进行转换 ，MyBatis 通过它来处理 Java 对象与数据库字段之间的映射。
 * JDBC 只能识别基本类型（如 String、int）和部分标准类型（如 Date）。
 * 如果我们要将 Java 中的复杂类型（如枚举、自定义类、LocalDate 等）写入数据库或从数据库读取，
 * 就需要自定义类型处理器。
 */
public interface TypeHandler<T> {
    /**
     * 设置参数：在通过PreparedStatement为SQL语句绑定参数时，会将传入的实参数据由JdbcType类型转换成Java类型
     */
    void setParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException;

    /**
     * 获取结果：从ResultSet中获取数据时会使用getResult()方法，其中会将读取到的数据由Java类型转换成JdbcType类型
     */
    T getResult(ResultSet rs, String columnName) throws SQLException;

    /**
     * 获取结果：从ResultSet中获取数据时会使用getResult()方法，其中会将读取到的数据由Java类型转换成JdbcType类型
     */
    T getResult(ResultSet rs, int columnIndex) throws SQLException;

}
