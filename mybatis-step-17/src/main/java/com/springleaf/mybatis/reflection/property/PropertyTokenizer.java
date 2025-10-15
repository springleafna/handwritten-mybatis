package com.springleaf.mybatis.reflection.property;

import java.util.Iterator;

/**
 * 负责解析由 “.” 和 “[]” 构成的表达式。PropertyTokenizer 继承了 Iterator 接口，可以迭代处理嵌套多层表达式。
 * 用于解析嵌套表达式，如 person[0].birthdate.year，将依次取得person[0], birthdate, year
 * 使用场景：如
 * <resultMap id="userMap" type="User">
 *     <result property="person.address.city" column="city"/>
 * </resultMap>
 * 能够将用于将数据库查询结果中的某一列（column="city"）的值，映射到 Java 对象 User 的嵌套属性（property="person.address.city"）上
 */
public class PropertyTokenizer implements Iterable<PropertyTokenizer>, Iterator<PropertyTokenizer> {
    //例子： person[0].birthdate.year
    private String name; //person
    private String indexedName; //person[0]
    private String index; //0
    private String children; //birthdate.year

    public PropertyTokenizer(String fullname) {
        //person[0].birthdate.year
        //找.
        int delim = fullname.indexOf('.');
        if (delim > -1) {
            name = fullname.substring(0, delim);
            children = fullname.substring(delim + 1);
        } else {
            //找不到.的话，取全部部分
            name = fullname;
            children = null;
        }
        indexedName = name;
        //把中括号里的数字给解析出来
        delim = name.indexOf('[');
        if (delim > -1) {
            index = name.substring(delim + 1, name.length() - 1);
            name = name.substring(0, delim);
        }
    }

    public String getName() {
        return name;
    }

    public String getIndex() {
        return index;
    }

    public String getIndexedName() {
        return indexedName;
    }

    public String getChildren() {
        return children;
    }

    @Override
    public boolean hasNext() {
        return children != null;
    }

    //取得下一个,非常简单，直接再通过儿子来new另外一个实例
    @Override
    public PropertyTokenizer next() {
        return new PropertyTokenizer(children);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Remove is not supported, as it has no meaning in the context of properties.");
    }

    @Override
    public Iterator<PropertyTokenizer> iterator() {
        return this;
    }
}
