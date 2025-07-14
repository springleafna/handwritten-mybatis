package com.springleaf.mybatis.scripting.xmltags;

import com.springleaf.mybatis.scripting.xmltags.DynamicContext;
import com.springleaf.mybatis.scripting.xmltags.SqlNode;

/**
 * 静态文本SQL节点
 * 静态⽂本节点不做任何处理，直接将⽂本节点的内容追加到已经解析了的SQL⽂本的后⾯
 */
public class StaticTextSqlNode implements SqlNode {

    private String text;

    public StaticTextSqlNode(String text) {
        this.text = text;
    }

    @Override
    public boolean apply(DynamicContext context) {
        //将文本加入context
        context.appendSql(text);
        return true;
    }

}
