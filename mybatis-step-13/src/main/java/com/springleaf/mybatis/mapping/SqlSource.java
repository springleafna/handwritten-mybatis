package com.springleaf.mybatis.mapping;

/**
 * SQL源码
 * 代表从XML⽂件或者注解读取的映射语句的内容,它创建的SQL会被传递给数据库。
 * 它表示的SQL语句是不能直接被数据库执行的，因为其中可能含有动态SQL语句相关的节点或是占位符等需要解析的元素
 */
public interface SqlSource {

    /**
     * getBoundSql()方法会根据映射文件或注解描述的SQL语句，以及传入的参数，返回可执行的SQL
     */
    BoundSql getBoundSql(Object parameterObject);

}
