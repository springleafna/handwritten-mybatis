package com.springleaf.mybatis.reflection;

import com.springleaf.mybatis.reflection.factory.DefaultObjectFactory;
import com.springleaf.mybatis.reflection.factory.ObjectFactory;
import com.springleaf.mybatis.reflection.wrapper.DefaultObjectWrapperFactory;
import com.springleaf.mybatis.reflection.wrapper.ObjectWrapperFactory;

/**
 * 一些系统级别的元对象
 * SystemMetaObject 是一个工具类，
 * 提供静态方法用于创建 MetaObject 或访问系统级对象的元信息（如 NullObject 或默认值处理）。
 */
public class SystemMetaObject {

    public static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
    public static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
    public static final MetaObject NULL_META_OBJECT = MetaObject.forObject(NullObject.class, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);

    private SystemMetaObject() {
        // Prevent Instantiation of Static Class
    }

    /**
     * 空对象
     */
    private static class NullObject {
    }

    public static MetaObject forObject(Object object) {
        return MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);
    }

}
