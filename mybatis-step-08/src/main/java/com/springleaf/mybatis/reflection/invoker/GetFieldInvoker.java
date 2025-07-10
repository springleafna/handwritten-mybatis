package com.springleaf.mybatis.reflection.invoker;

import java.lang.reflect.Field;

/**
 * getter 调用者
 * 模拟 字段的 getter 行为
 * 通过 field.get(target) 获取字段值
 * getType() 返回字段的类型
 */
public class GetFieldInvoker implements Invoker {

    private Field field;

    public GetFieldInvoker(Field field) {
        this.field = field;
    }

    @Override
    public Object invoke(Object target, Object[] args) throws Exception {
        return field.get(target);
    }

    @Override
    public Class<?> getType() {
        return field.getType();
    }

}
