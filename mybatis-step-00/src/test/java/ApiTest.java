import com.springleaf.mybatis.binding.MapperProxyFactory;
import com.springleaf.mybatis.test.mapper.UserMapper;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class ApiTest {

    private final Logger logger = LoggerFactory.getLogger(ApiTest.class);

    /**
     * 在 Java 中，接口本身不能直接实例化，但可以通过动态代理机制，在运行时生成一个实现了该接口的代理类实例。
     * 这个代理类的行为由 InvocationHandler 来定义，从而实现对接口方法的模拟、拦截或增强。
     * 在 MyBatis 中，接口（如 UserMapper）本身没有实现类，MyBatis 通过动态代理技术为其生成代理类。
     * 当调用接口方法时，MyBatis 会根据方法名匹配 SQL 语句，提取参数，最终通过 JDBC 执行 SQL 并返回结果。
     */
    @Test
    public void text_mapperProxyFactory() {
        MapperProxyFactory<UserMapper> factory = new MapperProxyFactory<>(UserMapper.class);

        Map<String, String> sqlSession = new HashMap<>();
        sqlSession.put("com.springleaf.mybatis.test.mapper.UserMapper.queryUserName", "模拟执行 Mapper.xml 中 SQL 语句的操作：查询用户姓名");
        UserMapper userMapper = factory.newInstance(sqlSession);

        String res = userMapper.queryUserName("101");
        logger.info("测试结果：{}", res);
    }

    @Test
    public void test_proxy_class() {
        UserMapper userMapper = (UserMapper) Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class[]{UserMapper.class}, (proxy, method, args) -> {
                    if  (method.getName().equals("queryUserName")) {
                        return "你被代理了！queryUserName方法被调用，参数是：" + args[0];
                    } else {
                        return "你被代理了！其他方法被调用";
                    }
                });
        String result = userMapper.queryUserName("10001");
        System.out.println("测试结果：" + result);
    }
}
