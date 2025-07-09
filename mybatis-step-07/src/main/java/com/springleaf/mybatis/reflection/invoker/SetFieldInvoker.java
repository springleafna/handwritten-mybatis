package com.springleaf.mybatis.reflection.invoker;

import java.lang.reflect.Field;

/**
 * setter 调用者
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