package com.springleaf.mybatis.binding;

import com.springleaf.mybatis.binding.MapperMethod;
import com.springleaf.mybatis.binding.MapperProxy;
import com.springleaf.mybatis.session.SqlSession;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 映射器代理工厂
 * 主要负责创建代理对象
 */
public class MapperProxyFactory<T> {

    // 例如：UserMapper
    private final Class<T> mapperInterface;

    private Map<Method, MapperMethod> methodCache = new ConcurrentHashMap<Method, MapperMethod>();

    public MapperProxyFactory(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    public Map<Method, MapperMethod> getMethodCache() {
        return methodCache;
    }

    @SuppressWarnings("unchecked")
    public T newInstance(SqlSession sqlSession) {
        // 创建MapperProxy对象，每次调用都会创建新的MapperProxy对象
        final MapperProxy<T> mapperProxy = new MapperProxy<>(sqlSession, mapperInterface, methodCache);
        // 创建实现类mapperInterface接口的代理对象并返回
        return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[]{mapperInterface}, mapperProxy);
    }
}
