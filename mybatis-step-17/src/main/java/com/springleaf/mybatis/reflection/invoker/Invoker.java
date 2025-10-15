package com.springleaf.mybatis.reflection.invoker;

/**
 * 为了在处理 Java Bean 的 getter/setter 方法、字段（Field）时，提供一个统一的调用方式。
 * 不直接调用 Java Bean 的 getter 和 setter 方法，而是通过封装 Method 或 Field，
 * 使用统一的 invoke(...) 方法进行属性访问（取值或赋值），从而实现更灵活、统一、可扩展的反射操作。
 *
 * MethodInvoker 是通过反射方式执行底层封装的 Method 方法（例如，getter/setter 方法）完成属性读写效果的
 * Get/SetFieldInvoker 是通过反射方式读写底层封装的 Field 字段，进而实现属性读写效果的
 */
public interface Invoker {

    /**
     * 调用底层封装的Method方法或是读写指定的字段
     */
    Object invoke(Object target, Object[] args) throws Exception;

    /**
     * 返回属性的类型
     */
    Class<?> getType();

}
