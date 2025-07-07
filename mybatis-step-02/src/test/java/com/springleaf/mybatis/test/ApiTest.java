package com.springleaf.mybatis.test;

import com.springleaf.mybatis.io.Resources;
import com.springleaf.mybatis.session.SqlSession;
import com.springleaf.mybatis.session.SqlSessionFactory;
import com.springleaf.mybatis.session.SqlSessionFactoryBuilder;
import com.springleaf.mybatis.test.mapper.UserMapper;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;

public class ApiTest {
    
    private final Logger logger = LoggerFactory.getLogger(ApiTest.class);

    @Test
    public void test() throws IOException {
        // 1. 从SqlSessionFactory中获取SqlSession
        Reader reader = Resources.getResourceAsReader("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        // 2. 获取映射器对象
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        // 3. 测试验证
        String res = userMapper.queryUserInfoById("10001");
        logger.info("测试结果：{}", res);

    }
}
