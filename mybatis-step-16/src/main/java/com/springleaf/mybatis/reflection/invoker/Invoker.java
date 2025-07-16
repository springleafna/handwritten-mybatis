package com.springleaf.mybatis.reflection.invoker;

/**
 * 调用者
 * 为了在处理 Java Bean 的 getter/setter 方法、字段（Field）时，提供一个统一的调用方式。
 * 不直接调用 Java Bean 的 getter 和 setter 方法，而是通过封装 Method 或 Field，
 * 使用统一的 invoke(...) 方法进行属性访问（取值或赋值），从而实现更灵活、统一、可扩展的反射操作。
 */
public interface Invoker {

    Object invoke(Object target, Object[] args) throws Exception;

    Class<?> getType();

}
