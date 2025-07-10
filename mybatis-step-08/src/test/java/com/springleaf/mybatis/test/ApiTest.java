package com.springleaf.mybatis.test;

import cn.hutool.json.JSONUtil;
import com.springleaf.mybatis.io.Resources;
import com.springleaf.mybatis.session.SqlSession;
import com.springleaf.mybatis.session.SqlSessionFactory;
import com.springleaf.mybatis.session.SqlSessionFactoryBuilder;
import com.springleaf.mybatis.test.mapper.UserMapper;
import com.springleaf.mybatis.test.po.User;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ApiTest {

    private final Logger logger = LoggerFactory.getLogger(ApiTest.class);

    private SqlSession sqlSession;


    @Before
    public void init() throws IOException {
        // 1. 从SqlSessionFactory中获取SqlSession
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader("mybatis-config.xml"));
        sqlSession = sqlSessionFactory.openSession();
    }

    @Test
    public void test_queryUserInfoById() throws IOException {
        // 2. 获取映射器对象
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        // 3. 测试验证
        User user = userMapper.queryUserInfoById(1L);
        logger.info("测试结果：{}", JSONUtil.toJsonStr(user));
    }

    @Test
    public void test_queryUserInfo() {
        // 2. 获取映射器对象
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        // 2. 测试验证：对象参数
        User user = userMapper.queryUserInfo(new User(1L, "10001"));
        logger.info("测试结果：{}", JSONUtil.toJsonStr(user));
    }
}
