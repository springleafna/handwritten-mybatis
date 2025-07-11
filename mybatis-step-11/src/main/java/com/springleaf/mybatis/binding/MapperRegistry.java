package com.springleaf.mybatis.binding;

import cn.hutool.core.lang.ClassScanner;
import com.springleaf.mybatis.builder.annotation.MapperAnnotationBuilder;
import com.springleaf.mybatis.session.Configuration;
import com.springleaf.mybatis.session.SqlSession;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 映射器注册机
 * 自动扫描包路径下的mapper
 * 用于注册和获取 Mapper 接口的代理对象（即我们平时使用的 Mapper）
 */
public class MapperRegistry {

    private Configuration config;

    public MapperRegistry(Configuration config) {
        this.config = config;
    }

    /**
     * 将已添加的映射器代理加入到 HashMap
     * 记录了 Mapper 接口与对应 MapperProxyFactory 之间的关系
     */
    private final Map<Class<?>, MapperProxyFactory<?>> knownMappers = new HashMap<>();

    /**
     * 在需要执行某SQL语句时，会先调用MapperRegistry.getMapper()方法获取实现了Mapper接口的代理对象
     * 例如：
     * session.getMapper（BlogMapper.class）方法
     * 得到的实际上是MyBatis通过JDK动态代理为BlogMapper接口生成的代理对象
     */
    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        final MapperProxyFactory<T> mapperProxyFactory = (MapperProxyFactory<T>) knownMappers.get(type);
        if (mapperProxyFactory == null) {
            throw new RuntimeException("Type " + type + " is not known to the MapperRegistry.");
        }
        try {
            return mapperProxyFactory.newInstance(sqlSession);
        } catch (Exception e) {
            throw new RuntimeException("Error getting mapper instance. Cause: " + e, e);
        }
    }

    /**
     * MyBatis初始化过程中会读取映射配置文件以及Mapper接口中的注解信息，
     * 并调用MapperRegistry.addMapper()方法填充MapperRegistry.knownMappers集合，
     * 该集合的key是Mapper接口对应的Class对象，value为MapperProxyFactory工厂对象，
     * 可以为Mapper接口创建代理对象
     */
    public <T> void addMapper(Class<T> type) {
        // Mapper 必须是接口才会注册
        if (type.isInterface()) {
            if (hasMapper(type)) {
                // 如果重复添加了，报错
                throw new RuntimeException("Type " + type + " is already known to the MapperRegistry.");
            }
            // 注册映射器代理工厂
            knownMappers.put(type, new MapperProxyFactory<>(type));

            // 解析注解类语句配置
            MapperAnnotationBuilder parser = new MapperAnnotationBuilder(config, type);
            parser.parse();
        }
    }

    public <T> boolean hasMapper(Class<T> type) {
        return knownMappers.containsKey(type);
    }

    public void addMappers(String packageName) {
        Set<Class<?>> mapperSet = ClassScanner.scanPackage(packageName);
        for (Class<?> mapperClass : mapperSet) {
            addMapper(mapperClass);
        }
    }
}
