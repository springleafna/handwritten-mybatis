package com.springleaf.mybatis.builder;

import com.springleaf.mybatis.session.Configuration;
import com.springleaf.mybatis.type.TypeAliasRegistry;
import com.springleaf.mybatis.type.TypeHandler;
import com.springleaf.mybatis.type.TypeHandlerRegistry;

/**
 * 构建器的基类，建造者模式
 */
public abstract class BaseBuilder {

    protected final Configuration configuration;
    protected final TypeAliasRegistry typeAliasRegistry;
    protected final TypeHandlerRegistry typeHandlerRegistry;

    public BaseBuilder(Configuration configuration) {
        this.configuration = configuration;
        this.typeAliasRegistry = this.configuration.getTypeAliasRegistry();
        this.typeHandlerRegistry = this.configuration.getTypeHandlerRegistry();
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    protected Class<?> resolveAlias(String alias) {
        return typeAliasRegistry.resolveAlias(alias);
    }

    // 根据别名解析 Class 类型别名注册/事务管理器别名
    protected Class<?> resolveClass(String alias) {
        if (alias == null) {
            return null;
        }
        try {
            return resolveAlias(alias);
        } catch (Exception e) {
            throw new RuntimeException("Error resolving class. Cause: " + e, e);
        }
    }

    protected TypeHandler<?> resolveTypeHandler(Class<?> javaType, Class<? extends TypeHandler<?>> typeHandlerType) {
        if (typeHandlerType == null){
            return null;
        }
        return typeHandlerRegistry.getMappingTypeHandler(typeHandlerType);
    }
}
