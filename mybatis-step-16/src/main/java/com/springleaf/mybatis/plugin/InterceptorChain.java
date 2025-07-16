package com.springleaf.mybatis.plugin;

import com.springleaf.mybatis.plugin.Interceptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 拦截器链
 * 在MyBatis初始化时，
 * 会通过XMLConfigBuilder.pluginElement()方法解析mybatis-config.xml配置文件中定义的＜plugin＞节点，
 * 得到相应的Interceptor对象以及配置的相应属性，
 * 之后会调用Interceptor.setProperties（properties）方法完成对Interceptor对象的初始化配置，
 * 最后将Interceptor对象添加到Configuration.interceptorChain字段中保存
 */
public class InterceptorChain {

    // 记录mybatis-config.xml文件中配置的所有拦截器
    private final List<Interceptor> interceptors = new ArrayList<>();

    /**
     * 遍历interceptors集合，并调用其中每个元素的plugin()方法创建代理对象
     */
    public Object pluginAll(Object target) {
        for (Interceptor interceptor : interceptors) {
            target = interceptor.plugin(target);
        }
        return target;
    }

    public void addInterceptor(Interceptor interceptor) {
        interceptors.add(interceptor);
    }

    public List<Interceptor> getInterceptors(){
        return Collections.unmodifiableList(interceptors);
    }

}
