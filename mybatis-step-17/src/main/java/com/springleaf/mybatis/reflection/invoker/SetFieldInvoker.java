package com.springleaf.mybatis.reflection.invoker;

import java.lang.reflect.Field;

/**
 * 在 Reflector 对象的初始化过程中，没有 setter方法的字段都会被封装成 SetFieldInvoker 对象
 * 模拟 字段的 setter 行为
 * 通过 field.set(target, args[0]) 给字段赋值
 * getType() 返回字段类型
 */
public class SetFieldInvoker implements Invoker {

    private Field field;

    public SetFieldInvoker(Field field) {
        this.field = field;
    }

    @Override
    public Object invoke(Object target, Object[] args) throws Exception {
        field.set(target, args[0]);
        return null;
    }

    @Override
    public Class<?> getType() {
        return field.getType();
    }

}