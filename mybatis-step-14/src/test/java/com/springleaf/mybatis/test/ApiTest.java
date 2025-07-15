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
import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;
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
        Activity activity = new Activity();
        activity.setActivityId(100001L);
        Activity res = activityMapper.queryActivityById(activity);
        logger.info("测试结果{}", JSONUtil.toJsonStr(res));
    }

    @Test
    public void test_ognl() throws OgnlException {
        Activity req = new Activity();
        req.setActivityId(1L);
        req.setActivityName("测试活动");
        req.setActivityDesc("springleaf的测试内容");

        OgnlContext context = new OgnlContext();
        context.setRoot(req);
        Object root = context.getRoot();

        Object activityName = Ognl.getValue("activityName", context, root);
        Object activityDesc = Ognl.getValue("activityDesc", context, root);
        Object value = Ognl.getValue("activityDesc.length()", context, root);

        System.out.println(activityName + "\t" + activityDesc + " length：" + value);
    }
}
