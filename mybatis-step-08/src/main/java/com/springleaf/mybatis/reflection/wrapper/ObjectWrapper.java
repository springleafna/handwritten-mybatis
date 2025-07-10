package com.springleaf.mybatis.reflection.wrapper;

import com.springleaf.mybatis.reflection.MetaObject;
import com.springleaf.mybatis.reflection.factory.ObjectFactory;
import com.springleaf.mybatis.reflection.property.PropertyTokenizer;

import java.util.List;

/**
 * 这是对对象的“包装器”，负责实际的属性访问逻辑。
 * ObjectWrapper 是一个抽象接口，
 * 定义了对任意对象（POJO、Map、Collection 等）进行属性读写的统一方法。
 * 无论底层对象是 POJO、Map 还是 Collection，
 * ObjectWrapper 提供一致的 get() 和 set() 方法，屏蔽底层差异。
 */
public interface ObjectWrapper {

    // get
    Object get(PropertyTokenizer prop);

    // set
    void set(PropertyTokenizer prop, Object value);

    // 查找属性
    String findProperty(String name, boolean useCamelCaseMapping);

    // 取得getter的名字列表
    String[] getGetterNames();

    // 取得setter的名字列表
    String[] getSetterNames();

    //取得setter的类型
    Class<?> getSetterType(String name);

    // 取得getter的类型
    Class<?> getGetterType(String name);

    // 是否有指定的setter
    boolean hasSetter(String name);

    // 是否有指定的getter
    boolean hasGetter(String name);

    // 实例化属性
    MetaObject instantiatePropertyValue(String name, PropertyTokenizer prop, ObjectFactory objectFactory);

    // 是否是集合
    boolean isCollection();

    // 添加属性
    void add(Object element);

    // 添加属性
    <E> void addAll(List<E> element);
}
