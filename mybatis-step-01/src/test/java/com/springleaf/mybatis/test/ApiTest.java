package com.springleaf.mybatis.test;

import com.springleaf.mybatis.binding.MapperRegistry;
import com.springleaf.mybatis.session.SqlSession;
import com.springleaf.mybatis.session.SqlSessionFactory;
import com.springleaf.mybatis.session.defaults.DefaultSqlSessionFactory;
import com.springleaf.mybatis.test.mapper.UserMapper;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApiTest {

    private final Logger logger = LoggerFactory.getLogger(ApiTest.class);

    @Test
    public void test_MapperProxyFactory() {
        // 1. 注册 Mapper
        MapperRegistry mapperRegistry = new MapperRegistry();
        mapperRegistry.addMappers("com.springleaf.mybatis.test.mapper");

        // 2. 从 SqlSession 工厂获取 Session
        SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(mapperRegistry);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        // 3. 获取映射器对象
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        // 4. 测试验证
        String res = userMapper.queryUserName("10001");
        logger.info("测试结果：{}", res);
    }
}
