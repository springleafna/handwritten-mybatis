package com.springleaf.mybatis.scripting.xmltags;

import com.springleaf.mybatis.builder.BaseBuilder;
import com.springleaf.mybatis.mapping.SqlSource;
import com.springleaf.mybatis.scripting.defaults.RawSqlSource;
import com.springleaf.mybatis.scripting.xmltags.MixedSqlNode;
import com.springleaf.mybatis.scripting.xmltags.SqlNode;
import com.springleaf.mybatis.scripting.xmltags.StaticTextSqlNode;
import com.springleaf.mybatis.session.Configuration;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * XML脚本构建器
 */
public class XMLScriptBuilder extends BaseBuilder {

    private Element element;
    private boolean isDynamic;
    private Class<?> parameterType;

    public XMLScriptBuilder(Configuration configuration, Element element, Class<?> parameterType) {
        super(configuration);
        this.element = element;
        this.parameterType = parameterType;
    }

    public SqlSource parseScriptNode() {
        List<SqlNode> contents = parseDynamicTags(element);
        MixedSqlNode rootSqlNode = new MixedSqlNode(contents);
        return new RawSqlSource(configuration, rootSqlNode, parameterType);
    }

    List<SqlNode> parseDynamicTags(Element element) {
        List<SqlNode> contents = new ArrayList<>();
        // element.getText 拿到 SQL
        String data = element.getText();
        contents.add(new StaticTextSqlNode(data));
        return contents;
    }

}
