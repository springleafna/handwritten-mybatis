package com.springleaf.mybatis.scripting.xmltags;

/**
 * SQL 节点
 */
public interface SqlNode {

    boolean apply(DynamicContext context);

}