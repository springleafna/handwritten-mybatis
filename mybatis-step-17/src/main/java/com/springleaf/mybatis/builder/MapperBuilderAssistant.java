package com.springleaf.mybatis.builder;

import com.springleaf.mybatis.cache.Cache;
import com.springleaf.mybatis.cache.decorators.FifoCache;
import com.springleaf.mybatis.cache.impl.PerpetualCache;
import com.springleaf.mybatis.executor.keygen.KeyGenerator;
import com.springleaf.mybatis.mapping.*;
import com.springleaf.mybatis.reflection.MetaClass;
import com.springleaf.mybatis.scripting.LanguageDriver;
import com.springleaf.mybatis.session.Configuration;
import com.springleaf.mybatis.type.TypeHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * 映射构建器助手，建造者
 * MapperBuilderAssistant 是 MyBatis 在解析 XML 映射文件时使用的助手类，
 * 负责处理命名空间、构建 MappedStatement 和 ResultMap，并将它们注册到全局配置中。
 */
public class MapperBuilderAssistant extends BaseBuilder {

    // 当前正在解析的 Mapper 文件的 namespace（如：com.example.mapper.UserMapper）
    private String currentNamespace;
    // 当前 XML 文件的路径（如：mapper/UserMapper.xml）
    private String resource;

    private Cache currentCache;

    public MapperBuilderAssistant(Configuration configuration, String resource) {
        super(configuration);
        this.resource = resource;
    }

    public ResultMapping buildResultMapping(
            Class<?> resultType,
            String property,
            String column,
            List<ResultFlag> flags) {

        Class<?> javaTypeClass = resolveResultJavaType(resultType, property, null);
        TypeHandler<?> typeHandlerInstance = resolveTypeHandler(javaTypeClass, null);

        ResultMapping.Builder builder = new ResultMapping.Builder(configuration, property, column, javaTypeClass);
        builder.typeHandler(typeHandlerInstance);
        builder.flags(flags);

        return builder.build();

    }

    private Class<?> resolveResultJavaType(Class<?> resultType, String property, Class<?> javaType) {
        if (javaType == null && property != null) {
            try {
                MetaClass metaResultType = MetaClass.forClass(resultType);
                javaType = metaResultType.getSetterType(property);
            } catch (Exception ignore) {
            }
        }
        if (javaType == null) {
            javaType = Object.class;
        }
        return javaType;
    }

    public String getCurrentNamespace() {
        return currentNamespace;
    }

    public void setCurrentNamespace(String currentNamespace) {
        this.currentNamespace = currentNamespace;
    }


    /**
     * 根据是否是引用类型（如 <resultMap> 的引用），决定是否给名称加上 currentNamespace 前缀。
     * 假设 currentNamespace = com.example.mapper.UserMapper
     * 如果传入的 base = "selectUser" 且 isReference = false，则返回 com.example.mapper.UserMapper.selectUser
     * 如果传入的 base = "com.example.mapper.UserMapper.selectUser" 且 isReference = true，则返回 com.example.mapper.UserMapper.selectUser
     * 目的：避免不同 Mapper 中 ID 冲突、确保每个 SQL 语句都有唯一标识符（用于缓存、执行等）
     * @param base 原始名称（如 selectById）
     * @param isReference 是否是引用（比如引用一个已有的 resultMap）
     */
    public String applyCurrentNamespace(String base, boolean isReference) {
        if (base == null) {
            return null;
        }

        if (isReference) {
            if (base.contains(".")) return base;
        } else {
            if (base.startsWith(currentNamespace + ".")) {
                return base;
            }
            if (base.contains(".")) {
                throw new RuntimeException("Dots are not allowed in element names, please remove it from " + base);
            }
        }

        return currentNamespace + "." + base;
    }


    /**
     * 添加映射器语句
     * 将一个 <select>, <insert> 等标签转换为 MappedStatement 对象，并注册到全局配置中。
     */
    public MappedStatement addMappedStatement(
            String id,
            SqlSource sqlSource,
            SqlCommandType sqlCommandType,
            Class<?> parameterType,
            String resultMap,
            Class<?> resultType,
            boolean flushCache,
            boolean useCache,
            KeyGenerator keyGenerator,
            String keyProperty,
            LanguageDriver lang
    ) {
        // 1. 给id加上namespace前缀：com.springleaf.mybatis.test.mapper.UserMapper.queryUserInfoById
        id = applyCurrentNamespace(id, false);
        //是否是select语句
        boolean isSelect = sqlCommandType == SqlCommandType.SELECT;
        // 2. 创建 MappedStatement.Builder
        MappedStatement.Builder statementBuilder = new MappedStatement.Builder(configuration, id, sqlCommandType, sqlSource, resultType);
        statementBuilder.resource(resource);
        statementBuilder.keyGenerator(keyGenerator);
        statementBuilder.keyProperty(keyProperty);
        // 3. 结果映射，给 MappedStatement#resultMaps
        setStatementResultMap(resultMap, resultType, statementBuilder);
        setStatementCache(isSelect, flushCache, useCache, currentCache, statementBuilder);
        // 4. 构建 MappedStatement
        MappedStatement statement = statementBuilder.build();
        // 5. 映射语句信息，建造完存放到配置项中
        configuration.addMappedStatement(statement);

        return statement;
    }

    private void setStatementCache(
            boolean isSelect,
            boolean flushCache,
            boolean useCache,
            Cache cache,
            MappedStatement.Builder statementBuilder) {
        flushCache = valueOrDefault(flushCache, !isSelect);
        useCache = valueOrDefault(useCache, isSelect);
        statementBuilder.flushCacheRequired(flushCache);
        statementBuilder.useCache(useCache);
        statementBuilder.cache(cache);
    }

    /**
     * 处理 <select> 标签中的 resultMap 或 resultType 属性。
     * 如果指定了 resultMap，则从配置中取出对应的 ResultMap 对象
     * 如果只指定了 resultType，则自动创建一个内联的 ResultMap（MyBatis 自动按字段名映射）
     * 最终设置到 MappedStatement.Builder 中
     */
    private void setStatementResultMap(
            String resultMap,
            Class<?> resultType,
            MappedStatement.Builder statementBuilder) {
        // 因为暂时还没有在 Mapper XML 中配置 Map 返回结果，所以这里返回的是 null
        resultMap = applyCurrentNamespace(resultMap, true);

        List<ResultMap> resultMaps = new ArrayList<>();

        if (resultMap != null) {
            String[] resultMapNames = resultMap.split(",");
            for (String resultMapName : resultMapNames) {
                resultMaps.add(configuration.getResultMap(resultMapName.trim()));
            }
        }
        /*
         * 通常使用 resultType 即可满足大部分场景
         * <select id="queryUserInfoById" resultType="com.springleaf.mybatis.test.po.User">
         * 使用 resultType 的情况下，Mybatis 会自动创建一个 ResultMap，基于属性名称映射列到 JavaBean 的属性上。
         */
        else if (resultType != null) {
            ResultMap.Builder inlineResultMapBuilder = new ResultMap.Builder(
                    configuration,
                    statementBuilder.id() + "-Inline",
                    resultType,
                    new ArrayList<>());
            resultMaps.add(inlineResultMapBuilder.build());
        }
        statementBuilder.resultMaps(resultMaps);
    }

    /**
     * 构建并注册一个自定义的 ResultMap，用于复杂的结果集映射。
     */
    public ResultMap addResultMap(String id, Class<?> type, List<ResultMapping> resultMappings) {
        // 补全ID全路径，如：com.springleaf.mybatis.test.mapper.ActivityMapper + activityMap
        id = applyCurrentNamespace(id, false);

        ResultMap.Builder inlineResultMapBuilder = new ResultMap.Builder(
                configuration,
                id,
                type,
                resultMappings);

        ResultMap resultMap = inlineResultMapBuilder.build();
        configuration.addResultMap(resultMap);
        return resultMap;
    }

    public Cache useNewCache(Class<? extends Cache> typeClass,
                             Class<? extends Cache> evictionClass,
                             Long flushInterval,
                             Integer size,
                             boolean readWrite,
                             boolean blocking,
                             Properties props) {
        // 判断为null，则用默认值
        typeClass = valueOrDefault(typeClass, PerpetualCache.class);
        evictionClass = valueOrDefault(evictionClass, FifoCache.class);

        // 建造者模式构建 Cache [currentNamespace=com.springleaf.mybatis.test.dao.IActivityDao]
        Cache cache = new CacheBuilder(currentNamespace)
                .implementation(typeClass)
                .addDecorator(evictionClass)
                .clearInterval(flushInterval)
                .size(size)
                .readWrite(readWrite)
                .blocking(blocking)
                .properties(props)
                .build();

        // 添加缓存
        configuration.addCache(cache);
        currentCache = cache;
        return cache;
    }

    private <T> T valueOrDefault(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }

}
