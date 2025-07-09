package com.springleaf.mybatis.scripting;

import com.springleaf.mybatis.mapping.SqlSource;
import com.springleaf.mybatis.session.Configuration;
import org.dom4j.Element;

/**
 * 脚本语言驱动
 */
public interface LanguageDriver {

    SqlSource createSqlSource(Configuration configuration, Element script, Class<?> parameterType);

}
