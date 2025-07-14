package com.springleaf.mybatis.mapping;

import com.springleaf.mybatis.executor.keygen.Jdbc3KeyGenerator;
import com.springleaf.mybatis.executor.keygen.KeyGenerator;
import com.springleaf.mybatis.executor.keygen.NoKeyGenerator;
import com.springleaf.mybatis.scripting.LanguageDriver;
import com.springleaf.mybatis.session.Configuration;

import java.util.Collections;
import java.util.List;

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

    private String resource;
    // 所属的配置对象，用于访问全局配置信息
    private Configuration configuration;
    // 唯一标识符，通常是接口全限定名 + 方法名，如：com.example.mapper.UserMapper.selectUserById
    private String id;
    // SQL 类型（SELECT / INSERT / UPDATE / DELETE）
    private SqlCommandType sqlCommandType;
    private SqlSource sqlSource;
    Class<?> resultType;
    private LanguageDriver lang;
    private List<ResultMap> resultMaps;
    private KeyGenerator keyGenerator;
    private String[] keyProperties;
    private String[] keyColumns;

    MappedStatement() {
        // constructor disabled
    }

    public BoundSql getBoundSql(Object parameterObject) {
        // 调用 SqlSource#getBoundSql
        return sqlSource.getBoundSql(parameterObject);
    }

    /**
     * 建造者
     */
    public static class Builder {

        private MappedStatement mappedStatement = new MappedStatement();

        public Builder(Configuration configuration, String id, SqlCommandType sqlCommandType, SqlSource sqlSource, Class<?> resultType) {
            mappedStatement.configuration = configuration;
            mappedStatement.id = id;
            mappedStatement.sqlCommandType = sqlCommandType;
            mappedStatement.sqlSource = sqlSource;
            mappedStatement.resultType = resultType;
            mappedStatement.keyGenerator = configuration.isUseGeneratedKeys() && SqlCommandType.INSERT.equals(sqlCommandType) ? new Jdbc3KeyGenerator() : new NoKeyGenerator();
            mappedStatement.lang = configuration.getDefaultScriptingLanguageInstance();
        }

        public MappedStatement build() {
            assert mappedStatement.configuration != null;
            assert mappedStatement.id != null;
            mappedStatement.resultMaps = Collections.unmodifiableList(mappedStatement.resultMaps);
            return mappedStatement;
        }

        public Builder resource(String resource) {
            mappedStatement.resource = resource;
            return this;
        }

        public String id() {
            return mappedStatement.id;
        }

        public Builder resultMaps(List<ResultMap> resultMaps) {
            mappedStatement.resultMaps = resultMaps;
            return this;
        }

        public Builder keyGenerator(KeyGenerator keyGenerator) {
            mappedStatement.keyGenerator = keyGenerator;
            return this;
        }

        public Builder keyProperty(String keyProperty) {
            mappedStatement.keyProperties = delimitedStringToArray(keyProperty);
            return this;
        }

    }

    private static String[] delimitedStringToArray(String in) {
        if (in == null || in.trim().length() == 0) {
            return null;
        } else {
            return in.split(",");
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

    public SqlSource getSqlSource() {
        return sqlSource;
    }

    public Class<?> getResultType() {
        return resultType;
    }

    public LanguageDriver getLang() {
        return lang;
    }

    public List<ResultMap> getResultMaps() {
        return resultMaps;
    }

    public String[] getKeyColumns() {
        return keyColumns;
    }

    public String[] getKeyProperties() {
        return keyProperties;
    }

    public KeyGenerator getKeyGenerator() {
        return keyGenerator;
    }

    public String getResource() {
        return resource;
    }

}
