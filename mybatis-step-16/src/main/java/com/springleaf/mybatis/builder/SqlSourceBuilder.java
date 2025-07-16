package com.springleaf.mybatis.builder;

import com.springleaf.mybatis.mapping.ParameterMapping;
import com.springleaf.mybatis.mapping.SqlSource;
import com.springleaf.mybatis.parsing.GenericTokenParser;
import com.springleaf.mybatis.parsing.TokenHandler;
import com.springleaf.mybatis.reflection.MetaClass;
import com.springleaf.mybatis.reflection.MetaObject;
import com.springleaf.mybatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * SQL源码构建器
 * 将带有参数占位符的原始 SQL（如 #{id}、#{name}）解析成可执行的 SQL，并生成对应的参数映射信息。
 */
public class SqlSourceBuilder extends BaseBuilder {

    private static Logger logger = LoggerFactory.getLogger(SqlSourceBuilder.class);

    private static final String parameterProperties = "javaType,jdbcType,mode,numericScale,resultMap,typeHandler,jdbcTypeName";

    public SqlSourceBuilder(Configuration configuration) {
        super(configuration);
    }

    /**
     * 用于解析原始 SQL 中的参数表达式。
     * 创建一个 ParameterMappingTokenHandler 来处理每个 #{...} 表达式；
     * 使用 GenericTokenParser 解析 SQL；
     * 替换 #{...} 成 ?（JDBC 支持的占位符）；
     * @param originalSql 原始 SQL，可能包含 #{id} 这样的占位符
     * @param parameterType 参数类型，比如 User.class 或 Map.class
     * @param additionalParameters 额外参数（比如通过 @Param 添加的）
     * @return StaticSqlSource，包含 SQL 和参数映射。
     */
    public SqlSource parse(String originalSql, Class<?> parameterType, Map<String, Object> additionalParameters) {
        ParameterMappingTokenHandler handler = new ParameterMappingTokenHandler(configuration, parameterType, additionalParameters);
        GenericTokenParser parser = new GenericTokenParser("#{", "}", handler);
        String sql = parser.parse(originalSql);
        // 返回静态 SQL
        return new StaticSqlSource(configuration, sql, handler.getParameterMappings());
    }

    private static class ParameterMappingTokenHandler extends BaseBuilder implements TokenHandler {

        private List<ParameterMapping> parameterMappings = new ArrayList<>();
        private Class<?> parameterType;
        private MetaObject metaParameters;

        public ParameterMappingTokenHandler(Configuration configuration, Class<?> parameterType, Map<String, Object> additionalParameters) {
            super(configuration);
            this.parameterType = parameterType;
            this.metaParameters = configuration.newMetaObject(additionalParameters);
        }

        public List<ParameterMapping> getParameterMappings() {
            return parameterMappings;
        }

        @Override
        public String handleToken(String content) {
            parameterMappings.add(buildParameterMapping(content));
            return "?";
        }

        // 构建参数映射
        private ParameterMapping buildParameterMapping(String content) {
            // 先解析参数映射,就是转化成一个 HashMap | #{favouriteSection,jdbcType=VARCHAR}
            Map<String, String> propertiesMap = new ParameterExpression(content);
            String property = propertiesMap.get("property");
            Class<?> propertyType;
            if (typeHandlerRegistry.hasTypeHandler(parameterType)) {
                propertyType = parameterType;
            } else if (property != null) {
                MetaClass metaClass = MetaClass.forClass(parameterType);
                if (metaClass.hasGetter(property)) {
                    propertyType = metaClass.getGetterType(property);
                } else {
                    propertyType = Object.class;
                }
            } else {
                propertyType = Object.class;
            }

            logger.info("构建参数映射 property：{} propertyType：{}", property, propertyType);
            ParameterMapping.Builder builder = new ParameterMapping.Builder(configuration, property, propertyType);
            return builder.build();
        }

    }
}
