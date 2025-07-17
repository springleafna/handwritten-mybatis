package com.springleaf.mybatis.spring;

import com.springleaf.mybatis.session.SqlSessionFactory;
import org.springframework.beans.factory.FactoryBean;

/**
 * 这是一个 Spring 的 FactoryBean 实现类 ，
 * 用于为 MyBatis 的 Mapper 接口生成代理对象（即：Spring 容器中实际注入的是这个代理对象）
 * @param <T>
 */
public class MapperFactoryBean<T> implements FactoryBean<T> {

    private Class<T> mapperInterface;

    private SqlSessionFactory sqlSessionFactory;

    public MapperFactoryBean(Class<T> mapperInterface, SqlSessionFactory sqlSessionFactory) {
        this.mapperInterface = mapperInterface;
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public T getObject() throws Exception {
        return sqlSessionFactory.openSession().getMapper(mapperInterface);
    }

    @Override
    public Class<?> getObjectType() {
        return mapperInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
