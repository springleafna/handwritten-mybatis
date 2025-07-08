package com.springleaf.mybatis.session;

import com.springleaf.mybatis.binding.MapperRegistry;
import com.springleaf.mybatis.datasource.druid.DruidDataSourceFactory;
import com.springleaf.mybatis.datasource.pooled.PooledDataSourceFactory;
import com.springleaf.mybatis.datasource.unpooled.UnpooledDataSourceFactory;
import com.springleaf.mybatis.mapping.Environment;
import com.springleaf.mybatis.mapping.MappedStatement;
import com.springleaf.mybatis.session.SqlSession;
import com.springleaf.mybatis.transaction.jdbc.JdbcTransactionFactory;
import com.springleaf.mybatis.type.TypeAliasRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * 配置项
 */
public class Configuration {

    /**
     * 环境
     */
    protected Environment environment;

    /**
     * 映射注册机
     */
    protected MapperRegistry mapperRegistry = new MapperRegistry(this);

    /**
     * 保存所有解析好的 SQL 映射语句（MappedStatement），键是全限定名（如 com.example.mapper.UserMapper.selectUserById）
     */
    protected final Map<String, MappedStatement> mappedStatements = new HashMap<>();

    /**
     * 类型别名注册机
     */
    protected final TypeAliasRegistry typeAliasRegistry = new TypeAliasRegistry();

    public Configuration() {
        typeAliasRegistry.registerAlias("JDBC", JdbcTransactionFactory.class);
        typeAliasRegistry.registerAlias("DRUID", DruidDataSourceFactory.class);
        typeAliasRegistry.registerAlias("UNPOOLED", UnpooledDataSourceFactory.class);
        typeAliasRegistry.registerAlias("POOLED", PooledDataSourceFactory.class);
    }

    /**
     * 扫描指定包路径下的所有接口，并尝试将它们注册为 Mapper
     * @param packageName 包名
     */
    public void addMappers(String packageName) {
        mapperRegistry.addMappers(packageName);
    }

    /**
     * 手动添加一个 Mapper 接口（如 UserMapper.class）
     */
    public <T> void addMapper(Class<T> type) {
        mapperRegistry.addMapper(type);
    }

    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return mapperRegistry.getMapper(type, sqlSession);
    }

    public boolean hasMapper(Class<?> type) {
        return mapperRegistry.hasMapper(type);
    }

    public void addMappedStatement(MappedStatement ms) {
        mappedStatements.put(ms.getId(), ms);
    }

    public MappedStatement getMappedStatement(String id) {
        return mappedStatements.get(id);
    }

    public TypeAliasRegistry getTypeAliasRegistry() {
        return typeAliasRegistry;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
