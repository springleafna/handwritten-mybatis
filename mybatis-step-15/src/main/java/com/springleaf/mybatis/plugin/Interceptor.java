package com.springleaf.mybatis.plugin;

import java.util.Properties;

/**
 * 拦截器接口
 */
public interface Interceptor {

    // 拦截，使用方实现，执行拦截逻辑
    // 这个方法会在目标方法执行前被调用，可以在这里添加自己的增强逻辑。
    // Invocation里面指定了该要拦截的的类的方法
    Object intercept(Invocation invocation) throws Throwable;

    // 代理
    default Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    // 设置属性
    default void setProperties(Properties properties) {
        // NOP
    }

}