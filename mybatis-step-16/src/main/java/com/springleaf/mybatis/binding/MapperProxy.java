package com.springleaf.mybatis.binding;

import com.springleaf.mybatis.session.SqlSession;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 映射器代理类
 * MapperProxy实现了InvocationHandler接口，该接口的实现是代理对象的核心逻辑
 */
public class MapperProxy<T> implements InvocationHandler, Serializable {

    private static final long serialVersionUID = -6424540398559729838L;


    private final SqlSession sqlSession;

    // Mapper接口对应的Class对象，比如UserMapper
    private final Class<T> mapperInterface;

    // 用于缓存MapperMethod对象，
    // 其中key是Mapper接口中方法对应的Method对象，value是对应的MapperMethod对象。
    // MapperMethod对象会完成参数转换以及SQL语句的执行功能
    // 需要注意的是，MapperMethod中并不记录任何状态相关的信息，所以可以在多个代理对象之间共享
    private final Map<Method, MapperMethod> methodCache;

    public MapperProxy(SqlSession sqlSession, Class<T> mapperInterface, Map<Method, MapperMethod> methodCache) {
        this.sqlSession = sqlSession;
        this.mapperInterface = mapperInterface;
        this.methodCache = methodCache;
    }

    /**
     * 代理对象的方法调用都会进入这个方法，代理对象执行的主要逻辑
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 需要排除每个类的通用方法，比如hashCode、toString等
        // 如果目标方法继承自Object，则直接调用该方法
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        } else {
            final MapperMethod mapperMethod = cachedMapperMethod(method);
            return mapperMethod.execute(sqlSession, args);
        }
    }

    /**
     * 去缓存中找MapperMethod
     */
    private MapperMethod cachedMapperMethod(Method method) {
        MapperMethod mapperMethod = methodCache.get(method);
        if (mapperMethod == null) {
            //找不到才去new
            mapperMethod = new MapperMethod(mapperInterface, method, sqlSession.getConfiguration());
            methodCache.put(method, mapperMethod);
        }
        return mapperMethod;
    }
}
