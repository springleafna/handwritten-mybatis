package com.springleaf.mybatis.scripting.xmltags;

import com.springleaf.mybatis.scripting.xmltags.DynamicContext;

/**
 * SQL 节点
 * SqlNode接⼝主要⽤来处理CRUD节点下的各类动态标签，如if、where等。
 * 对每个动态标签，mybatis都提供了对应的SqlNode实现
 */
public interface SqlNode {

    boolean apply(DynamicContext context);

}