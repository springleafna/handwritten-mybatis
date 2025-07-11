package com.springleaf.mybatis.test;

import cn.hutool.json.JSONUtil;
import com.springleaf.mybatis.io.Resources;
import com.springleaf.mybatis.reflection.invoker.GetFieldInvoker;
import com.springleaf.mybatis.reflection.invoker.Invoker;
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
import java.lang.reflect.Field;
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
    public void test_insertUserInfo() {
        // 1. 获取映射器对象
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        // 2. 测试验证
        User user = new User();
        user.setUserId("10002");
        user.setUserName("小白");
        user.setUserHead("1_05");
        userMapper.insertUserInfo(user);
        logger.info("测试结果：{}", "Insert OK");

        // 3. 提交事务
        sqlSession.commit();
    }

    @Test
    public void test_deleteUserInfoByUserId() {
        // 1. 获取映射器对象
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        // 2. 测试验证
        int count = userMapper.deleteUserInfoByUserId("10002");
        logger.info("测试结果：{}", count == 1);

        // 3. 提交事务
        sqlSession.commit();
    }


    @Test
    public void test_updateUserInfo() {
        // 1. 获取映射器对象
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        // 2. 测试验证
        int count = userMapper.updateUserInfo(new User(1L, "10001", "叮当猫"));
        logger.info("测试结果：{}", count);

        // 3. 提交事务
        sqlSession.commit();
    }

    @Test
    public void test_queryUserInfoById() {
        // 1. 获取映射器对象
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        // 2. 测试验证：基本参数
        User user = userMapper.queryUserInfoById(1L);
        logger.info("测试结果：{}", JSONUtil.toJsonStr(user));
    }

    @Test
    public void test_queryUserInfo() {
        // 1. 获取映射器对象
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        // 2. 测试验证：对象参数
        User user = userMapper.queryUserInfo(new User(1L, "10001"));
        logger.info("测试结果：{}", JSONUtil.toJsonStr(user));
    }

    @Test
    public void test_queryUserInfoList() {
        // 1. 获取映射器对象
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        // 2. 测试验证：对象参数
        List<User> users = userMapper.queryUserInfoList();
        logger.info("测试结果：{}", JSONUtil.toJsonStr(users));
    }
}
