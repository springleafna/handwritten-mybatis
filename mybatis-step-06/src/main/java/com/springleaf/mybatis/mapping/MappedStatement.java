package com.springleaf.mybatis.mapping;

import com.springleaf.mybatis.session.Configuration;

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
    // 绑定的SQL
    private BoundSql boundSql;

    MappedStatement() {
        // constructor disabled
    }

    /**
     * 建造者
     */
    public static class Builder {

        private final MappedStatement mappedStatement = new MappedStatement();

        public Builder(Configuration configuration, String id, SqlCommandType sqlCommandType, BoundSql boundSql) {
            mappedStatement.configuration = configuration;
            mappedStatement.id = id;
            mappedStatement.sqlCommandType = sqlCommandType;
            mappedStatement.boundSql = boundSql;
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

    public String getId() {
        return id;
    }

    public SqlCommandType getSqlCommandType() {
        return sqlCommandType;
    }

    public BoundSql getBoundSql() {
        return boundSql;
    }
}
