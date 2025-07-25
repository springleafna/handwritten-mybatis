package com.springleaf.mybatis.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 拦截注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)   // 可使用在类、接口、注解、枚举上
public @interface Intercepts {

    Signature[] value();

}