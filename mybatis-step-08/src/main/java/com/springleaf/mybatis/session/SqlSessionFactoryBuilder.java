package com.springleaf.mybatis.session;

import com.springleaf.mybatis.builder.xml.XMLConfigBuilder;
import com.springleaf.mybatis.session.defaults.DefaultSqlSessionFactory;

import java.io.Reader;

/**
 * 构建SqlSessionFactory的工厂
 */
public class SqlSessionFactoryBuilder {

    public SqlSessionFactory build(Reader reader) {
        // 解析xml文件获取的结果就是Configuration
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder(reader);
        // 然后根据这个Configuration创建一个SqlSessionFactory
        return build(xmlConfigBuilder.parse());
    }

    public SqlSessionFactory build(Configuration config) {
        return new DefaultSqlSessionFactory(config);
    }

}