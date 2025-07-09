package com.springleaf.mybatis.executor.resultset;

import com.springleaf.mybatis.executor.Executor;
import com.springleaf.mybatis.mapping.BoundSql;
import com.springleaf.mybatis.mapping.MappedStatement;

import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 默认Map结果处理器
 * 将数据库查询返回的 ResultSet 结果集转换为 Java 对象列表（List<T>）
 */
public class DefaultResultSetHandler implements ResultSetHandler {

    private final BoundSql boundSql;
    private final MappedStatement mappedStatement;

    public DefaultResultSetHandler(Executor executor, MappedStatement mappedStatement, BoundSql boundSql) {
        this.boundSql = boundSql;
        this.mappedStatement = mappedStatement;
    }

    @Override
    public <E> List<E> handleResultSets(Statement stmt) throws SQLException {
        ResultSet resultSet = stmt.getResultSet();
        return resultSet2Obj(resultSet, mappedStatement.getResultType());
    }

    /**
     * 将 ResultSet 转换为 Java 对象
     * 比如查到的结果有三行数据，那么就要转换成包含三个对象的List
     * 并将每个对象的每个字段依次赋值
     */
    private <T> List<T> resultSet2Obj(ResultSet resultSet, Class<?> clazz) {
        List<T> list = new ArrayList<>();
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            // 这个while循环是创建每个对象并赋值
            while (resultSet.next()) {
                T obj = (T) clazz.newInstance();
                /*
                  下面的for循环是为对象的每个字段赋值：
                  获取字段值 value 和字段名 columnName
                  构造对应的 setter 方法名（如 setUserName）
                  使用反射调用该方法，给对象赋值
                 */
                for (int i = 1; i <= columnCount; i++) {
                    Object value = resultSet.getObject(i);
                    String columnName = metaData.getColumnName(i);
                    // 这里是设置方法名，例如：setId()
                    String setMethod = "set" + columnName.substring(0, 1).toUpperCase() + columnName.substring(1);
                    Method method;
                    // 如果字段是 Timestamp 类型，尝试匹配 Date.class 类型的 setter 方法
                    if (value instanceof Timestamp) {
                        method = clazz.getMethod(setMethod, Date.class);
                    } else {
                        method = clazz.getMethod(setMethod, value.getClass());
                    }
                    // 使用反射调用该方法，给对象赋值
                    method.invoke(obj, value);
                }
                list.add(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
