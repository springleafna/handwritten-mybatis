package com.springleaf.mybatis.mapping;

import com.springleaf.mybatis.session.Configuration;

import java.util.Map;

/**
 * 映射语句类
 * 封装 SQL 映射信息的类 ，用来保存一条完整的 SQL 映射语句的所有相关信息。
 * 可以理解为：一个 Mapper XML 文件中的 <select>、<insert>、<update>、<delete> 标签，
 * 或者一个 Mapper 接口方法上注解定义的 SQL，在程序内部所对应的 Java 对象。
 * 如：
 * <select id="selectUserById" resultType="User">
 *     SELECT * FROM user WHERE id = #{id}
 * </select>
 */
public class MappedStatement {

    // 所属的配置对象，用于访问全局配置信息
    private Configuration configuration;
    // 唯一标识符，通常是接口全限定名 + 方法名，如：com.example.mapper.UserMapper.selectUserById
    private String id;
    // SQL 类型（SELECT / INSERT / UPDATE / DELETE）
    private SqlCommandType sqlCommandType;
    // 参数类型（比如int,java.lang.String, 自定义类等）
    private String parameterType;
    // 返回值类型（查询结果映射的目标类型）
    private String resultType;
    // 实际执行的 SQL 语句（可能包含占位符）
    private String sql;
    // 参数映射关系，例如第 1 个参数对应哪个属性或变量
    private Map<Integer, String> parameter;

    MappedStatement() {
        // constructor disabled
    }

    /**
     * 建造者
     */
    public static class Builder {

        private MappedStatement mappedStatement = new MappedStatement();

        public Builder(Configuration configuration, String id, SqlCommandType sqlCommandType, String parameterType, String resultType, String sql, Map<Integer, String> parameter) {
            mappedStatement.configuration = configuration;
            mappedStatement.id = id;
            mappedStatement.sqlCommandType = sqlCommandType;
            mappedStatement.parameterType = parameterType;
            mappedStatement.resultType = resultType;
            mappedStatement.sql = sql;
            mappedStatement.parameter = parameter;
        }

        public MappedStatement build() {
            assert mappedStatement.configuration != null;
            assert mappedStatement.id != null;
            return mappedStatement;
        }

    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SqlCommandType getSqlCommandType() {
        return sqlCommandType;
    }

    public void setSqlCommandType(SqlCommandType sqlCommandType) {
        this.sqlCommandType = sqlCommandType;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Map<Integer, String> getParameter() {
        return parameter;
    }

    public void setParameter(Map<Integer, String> parameter) {
        this.parameter = parameter;
    }

}
