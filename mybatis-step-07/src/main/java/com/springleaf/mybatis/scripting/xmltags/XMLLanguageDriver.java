package com.springleaf.mybatis.scripting.xmltags;

import com.springleaf.mybatis.mapping.SqlSource;
import com.springleaf.mybatis.scripting.LanguageDriver;
import com.springleaf.mybatis.session.Configuration;
import org.dom4j.Element;

/**
 * XML语言驱动器
 */
public class XMLLanguageDriver implements LanguageDriver {

    @Override
    public SqlSource createSqlSource(Configuration configuration, Element script, Class<?> parameterType) {
        // 用XML脚本构建器解析
        XMLScriptBuilder builder = new XMLScriptBuilder(configuration, script, parameterType);
        return builder.parseScriptNode();
    }

}