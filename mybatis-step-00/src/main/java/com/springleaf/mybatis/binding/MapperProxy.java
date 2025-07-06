package com.springleaf.mybatis.binding;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 映射器代理类
 */
public class MapperProxy<T> implements InvocationHandler, Serializable {

    private static final long serialVersionUID = -6424540398559729838L;

    /**
     * 存储mapper的方法名和sql语句
     * key: com.xxx.xxxMapper.xxx
     * value: sql语句
     */
    private final Map<String, String> sqlSession;

    /**
     * mapper接口，比如UserMapper
     */
    private final Class<T> mapperInterface;

    public MapperProxy(Map<String, String> sqlSession, Class<T> mapperInterface) {
        this.sqlSession = sqlSession;
        this.mapperInterface = mapperInterface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 需要排除每个类的通用方法，比如hashCode、toString等
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        } else {
            return "你的被代理了！" + sqlSession.get(mapperInterface.getName() + "." + method.getName());
        }
    }
}
