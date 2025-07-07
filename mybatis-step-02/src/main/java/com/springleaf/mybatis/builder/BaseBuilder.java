package com.springleaf.mybatis.builder;

import com.springleaf.mybatis.session.Configuration;

/**
 * 构建器的基类，建造者模式
 */
public abstract class BaseBuilder {

    protected final Configuration configuration;

    public BaseBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    protected Configuration getConfiguration() {
        return this.configuration;
    }
}
