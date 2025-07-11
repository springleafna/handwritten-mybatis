package com.springleaf.mybatis.executor.resultset;

import com.springleaf.mybatis.io.Resources;
import com.springleaf.mybatis.mapping.ResultMap;
import com.springleaf.mybatis.session.Configuration;
import com.springleaf.mybatis.type.JdbcType;
import com.springleaf.mybatis.type.TypeHandler;
import com.springleaf.mybatis.type.TypeHandlerRegistry;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

/**
 * 结果集包装器
 * 对 JDBC 的 ResultSet 进行封装和处理，
 * 提供了列信息获取、类型处理器查找、已映射/未映射字段分类等功能，
 * 是 MyBatis 实现 ORM 映射（尤其是自动映射和嵌套映射）的核心组件之一。
 */
public class ResultSetWrapper {

    // 底层的 JDBC 结果集
    private final ResultSet resultSet;
    // 类型处理器注册中心，用于获取合适的 TypeHandler
    private final TypeHandlerRegistry typeHandlerRegistry;
    // 所有列名（如id,name）
    private final List<String> columnNames = new ArrayList<>();
    // 每个列对应的 Java 类名（如java.lang.Long）
    private final List<String> classNames = new ArrayList<>();
    // 每个列的 JDBC 类型（如BIGINT,VARCHAR）
    private final List<JdbcType> jdbcTypes = new ArrayList<>();
    // 缓存：列名 → Java 类型 → TypeHandler
    private final Map<String, Map<Class<?>, TypeHandler<?>>> typeHandlerMap = new HashMap<>();
    // 已映射的列名缓存
    private Map<String, List<String>> mappedColumnNamesMap = new HashMap<>();
    // 未映射的列名缓存
    private Map<String, List<String>> unMappedColumnNamesMap = new HashMap<>();

    public ResultSetWrapper(ResultSet rs, Configuration configuration) throws SQLException {
        super();
        this.typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        this.resultSet = rs;
        final ResultSetMetaData metaData = rs.getMetaData();
        final int columnCount = metaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            columnNames.add(metaData.getColumnLabel(i));
            jdbcTypes.add(JdbcType.forCode(metaData.getColumnType(i)));
            classNames.add(metaData.getColumnClassName(i));
        }
    }

    /**
     * 根据属性类型（Java 类型）和列名，查找合适的 TypeHandler；
     * 使用了缓存机制（typeHandlerMap），避免重复查找；
     * 如果没有找到，就从全局注册中心 typeHandlerRegistry 中获取。
     */
    public TypeHandler<?> getTypeHandler(Class<?> propertyType, String columnName) {
        TypeHandler<?> handler = null;
        Map<Class<?>, TypeHandler<?>> columnHandlers = typeHandlerMap.get(columnName);
        if (columnHandlers == null) {
            columnHandlers = new HashMap<>();
            typeHandlerMap.put(columnName, columnHandlers);
        } else {
            handler = columnHandlers.get(propertyType);
        }
        if (handler == null) {
            handler = typeHandlerRegistry.getTypeHandler(propertyType, null);
            columnHandlers.put(propertyType, handler);
        }
        return handler;
    }

    private Class<?> resolveClass(String className) {
        try {
            return Resources.classForName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public List<String> getMappedColumnNames(ResultMap resultMap, String columnPrefix) throws SQLException {
        List<String> mappedColumnNames = mappedColumnNamesMap.get(getMapKey(resultMap, columnPrefix));
        if (mappedColumnNames == null) {
            loadMappedAndUnmappedColumnNames(resultMap, columnPrefix);
            mappedColumnNames = mappedColumnNamesMap.get(getMapKey(resultMap, columnPrefix));
        }
        return mappedColumnNames;
    }

    public List<String> getUnmappedColumnNames(ResultMap resultMap, String columnPrefix) throws SQLException {
        List<String> unMappedColumnNames = unMappedColumnNamesMap.get(getMapKey(resultMap, columnPrefix));
        if (unMappedColumnNames == null) {
            loadMappedAndUnmappedColumnNames(resultMap, columnPrefix);
            unMappedColumnNames = unMappedColumnNamesMap.get(getMapKey(resultMap, columnPrefix));
        }
        return unMappedColumnNames;
    }

    private void loadMappedAndUnmappedColumnNames(ResultMap resultMap, String columnPrefix) throws SQLException {
        List<String> mappedColumnNames = new ArrayList<>();
        List<String> unmappedColumnNames = new ArrayList<>();
        final String upperColumnPrefix = columnPrefix == null ? null : columnPrefix.toUpperCase(Locale.ENGLISH);
        final Set<String> mappedColumns = prependPrefixes(resultMap.getMappedColumns(), upperColumnPrefix);
        for (String columnName : columnNames) {
            final String upperColumnName = columnName.toUpperCase(Locale.ENGLISH);
            if (mappedColumns.contains(upperColumnName)) {
                mappedColumnNames.add(upperColumnName);
            } else {
                unmappedColumnNames.add(columnName);
            }
        }
        mappedColumnNamesMap.put(getMapKey(resultMap, columnPrefix), mappedColumnNames);
        unMappedColumnNamesMap.put(getMapKey(resultMap, columnPrefix), unmappedColumnNames);
    }

    private String getMapKey(ResultMap resultMap, String columnPrefix) {
        return resultMap.getId() + ":" + columnPrefix;
    }

    private Set<String> prependPrefixes(Set<String> columnNames, String prefix) {
        if (columnNames == null || columnNames.isEmpty() || prefix == null || prefix.length() == 0) {
            return columnNames;
        }
        final Set<String> prefixed = new HashSet<String>();
        for (String columnName : columnNames) {
            prefixed.add(prefix + columnName);
        }
        return prefixed;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public List<String> getColumnNames() {
        return this.columnNames;
    }

    public List<String> getClassNames() {
        return Collections.unmodifiableList(classNames);
    }

}
