package com.springleaf.mybatis.test;

import cn.hutool.json.JSONUtil;
import com.springleaf.mybatis.mapper.ActivityMapper;
import com.springleaf.mybatis.po.Activity;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class ApiTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void test_spring_mybatis_query() {
        BeanFactory beanFactory = new ClassPathXmlApplicationContext("spring-config.xml");
        ActivityMapper activityMapper = beanFactory.getBean("activityMapper", ActivityMapper.class);
        Activity res = activityMapper.queryActivityById(new Activity(100001L));
        logger.info("测试结果：{}", JSONUtil.toJsonStr(res));
    }

    @Test
    public void test_spring_mybatis_queryList() {
        BeanFactory beanFactory = new ClassPathXmlApplicationContext("spring-config.xml");
        ActivityMapper activityMapper = beanFactory.getBean("activityMapper", ActivityMapper.class);

        List<Activity> res = activityMapper.listByActivityName("测试活动");
        for (Activity activity : res) {
            logger.info("测试结果：{}", JSONUtil.toJsonStr(activity));
        }
    }

    @Test
    public void test_spring_mybatis_insert() {
        BeanFactory beanFactory = new ClassPathXmlApplicationContext("spring-config.xml");
        ActivityMapper activityMapper = beanFactory.getBean("activityMapper", ActivityMapper.class);

        Activity activity = new Activity();
        activity.setActivityDesc("hhh");
        activity.setActivityName("hd");
        activity.setActivityId(798L);

        int i = activityMapper.insertActivity(activity);
        System.out.println(i);
    }

    @Test
    public void test_spring_mybatis_delete() {
        BeanFactory beanFactory = new ClassPathXmlApplicationContext("spring-config.xml");
        ActivityMapper activityMapper = beanFactory.getBean("activityMapper", ActivityMapper.class);

        int i = activityMapper.deleteByActivityIdInt(10230L);
        System.out.println(i);
    }
}
