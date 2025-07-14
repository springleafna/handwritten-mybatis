package com.springleaf.mybatis.test;

import cn.hutool.json.JSONUtil;
import com.springleaf.mybatis.builder.xml.XMLConfigBuilder;
import com.springleaf.mybatis.executor.Executor;
import com.springleaf.mybatis.io.Resources;
import com.springleaf.mybatis.mapping.Environment;
import com.springleaf.mybatis.session.*;
import com.springleaf.mybatis.session.defaults.DefaultSqlSession;
import com.springleaf.mybatis.test.mapper.ActivityMapper;
import com.springleaf.mybatis.test.po.Activity;
import com.springleaf.mybatis.transaction.Transaction;
import com.springleaf.mybatis.transaction.TransactionFactory;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;

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
        Activity res = activityMapper.queryActivityById(15004L);
        logger.info("测试结果：{}", JSONUtil.toJsonStr(res));
    }

    @Test
    public void test_insert() {
        // 1. 获取映射器对象
        ActivityMapper activityMapper = sqlSession.getMapper(ActivityMapper.class);

        Activity activity = new Activity();
        activity.setActivityId(15004L);
        activity.setActivityName("测试活动");
        activity.setActivityDesc("测试数据插入");
        activity.setCreator("springleaf");

        // 2. 测试验证
        Integer res = activityMapper.insert(activity);
        sqlSession.commit();

        logger.info("测试结果：count：{} idx：{}", res, activity.getId());
    }

    @Test
    public void test_insert_select() throws IOException {
        // 解析 XML
        Reader reader = Resources.getResourceAsReader("mybatis-config.xml");
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder(reader);
        Configuration configuration = xmlConfigBuilder.parse();

        // 获取 DefaultSqlSession
        final Environment environment = configuration.getEnvironment();
        TransactionFactory transactionFactory = environment.getTransactionFactory();
        Transaction tx = transactionFactory.newTransaction(configuration.getEnvironment().getDataSource(), TransactionIsolationLevel.READ_COMMITTED, false);

        // 创建执行器
        final Executor executor = configuration.newExecutor(tx);
        SqlSession sqlSession = new DefaultSqlSession(configuration, executor);

        // 执行查询：默认是一个集合参数
        Activity activity = new Activity();
        activity.setActivityId(10230L);
        activity.setActivityName("测试活动");
        activity.setActivityDesc("测试数据插入");
        activity.setCreator("springleaf");
        int res = sqlSession.insert("com.springleaf.mybatis.test.mapper.ActivityMapper.insert", activity);

        Object obj = sqlSession.selectOne("com.springleaf.mybatis.test.mapper.ActivityMapper.insert!selectKey");
        logger.info("测试结果：count：{} idx：{}", res, obj);

        sqlSession.commit();
    }
}
