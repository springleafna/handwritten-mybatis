package com.springleaf.mybatis.test;

import cn.hutool.json.JSONUtil;
import com.springleaf.mybatis.io.Resources;
import com.springleaf.mybatis.session.SqlSession;
import com.springleaf.mybatis.session.SqlSessionFactory;
import com.springleaf.mybatis.session.SqlSessionFactoryBuilder;
import com.springleaf.mybatis.test.mapper.ActivityMapper;
import com.springleaf.mybatis.test.po.Activity;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

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
    public void test_queryActivity() {
        // 1. 获取映射器对象
        ActivityMapper activityMapper = sqlSession.getMapper(ActivityMapper.class);
        // 2. 测试验证
        Activity res = activityMapper.queryActivityById(100001L);
        logger.info("测试结果：{}", JSONUtil.toJsonStr(res));
    }
}
