package com.springleaf.mybatis.scripting.xmltags;

import com.springleaf.mybatis.reflection.MetaObject;
import com.springleaf.mybatis.session.Configuration;
import ognl.OgnlContext;
import ognl.OgnlException;
import ognl.OgnlRuntime;
import ognl.PropertyAccessor;

import java.util.HashMap;
import java.util.Map;

/**
 * 动态上下文
 * 用于记录解析 动态SQL 语句之后产生的SQL语句片段，可以认为它是一个用于记录动态SQL语句解析结果的容器
 */
public class DynamicContext {

    public static final String PARAMETER_OBJECT_KEY = "_parameter";
    public static final String DATABASE_ID_KEY = "_databaseId";

    static {
        // 定义属性->getter方法映射，ContextMap到ContextAccessor的映射，注册到ognl运行时
        // 参考http://commons.apache.org/proper/commons-ognl/developer-guide.html
        OgnlRuntime.setPropertyAccessor(ContextMap.class, new ContextAccessor());
        // 将传入的参数对象统一封装为ContextMap对象（继承了HashMap对象），
        // 然后Ognl运行时环境在动态计算sql语句时，
        // 会按照ContextAccessor中描述的Map接口的方式来访问和读取ContextMap对象，获取计算过程中需要的参数。
        // ContextMap对象内部可能封装了一个普通的POJO对象，也可以是直接传递的Map对象，当然从外部是看不出来的，因为都是使用Map的接口来读取数据。
    }

    // 参数上下文
    private final ContextMap bindings;
    // 在 SqlNode 解析动态SQL时，会将解析后的Sql语句片段添加到该属性中保存，最终拼凑出一条完成的Sql语句
    private final StringBuilder sqlBuilder = new StringBuilder();
    private int uniqueNumber = 0;

    // 在DynamicContext的构造函数中，根据传入的参数对象是否为Map类型，有两个不同构造ContextMap的方式。
    // 而ContextMap作为一个继承了HashMap的对象，作用就是用于统一参数的访问方式：用Map接口方法来访问数据。
    // 具体来说，当传入的参数对象不是Map类型时，Mybatis会将传入的POJO对象用MetaObject对象来封装，
    // 当动态计算sql过程需要获取数据时，用Map接口的get方法包装 MetaObject对象的取值过程。
    public DynamicContext(Configuration configuration, Object parameterObject) {
        // 绝大多数调用的地方parameterObject为null
        if (parameterObject != null && !(parameterObject instanceof Map)) {
            // 如果是map型  ??  这句是 如果不是map型
            MetaObject metaObject = configuration.newMetaObject(parameterObject);
            bindings = new ContextMap(metaObject);
        } else {
            bindings = new ContextMap(null);
        }
        bindings.put(PARAMETER_OBJECT_KEY, parameterObject);
        bindings.put(DATABASE_ID_KEY, configuration.getDatabaseId());
    }

    public Map<String, Object> getBindings() {
        return bindings;
    }

    public void bind(String name, Object value) {
        bindings.put(name, value);
    }

    public void appendSql(String sql) { // 追加Sql片段
        sqlBuilder.append(sql);
        sqlBuilder.append(" ");
    }

    public String getSql() {    // 获取解析后的、完整的Sql语句
        return sqlBuilder.toString().trim();
    }

    public int getUniqueNumber() {
        return uniqueNumber++;
    }

    // 上下文map，静态内部类
    static class ContextMap extends HashMap<String, Object> {
        private static final long serialVersionUID = 2977601501966151582L;

        // 将用户传入的参数封装成了MetaObject对象
        private MetaObject parameterMetaObject;
        public ContextMap(MetaObject parameterMetaObject) {
            this.parameterMetaObject = parameterMetaObject;
        }

        @Override
        public Object get(Object key) {
            String strKey = (String) key;
            // 先去map里找
            if (super.containsKey(strKey)) {    // 如果ContentMap中已经包含了该Key，则直接返回
                return super.get(strKey);
            }

            // 如果没找到，再用ognl表达式去取值
            // school[0].class.user
            if (parameterMetaObject != null) {
                // issue #61 do not modify the context when reading
                return parameterMetaObject.getValue(strKey);
            }

            return null;
        }
    }

    // 上下文访问器，静态内部类,实现OGNL的PropertyAccessor
    static class ContextAccessor implements PropertyAccessor {

        @Override
        public Object getProperty(Map context, Object target, Object name)
                throws OgnlException {
            Map map = (Map) target;

            Object result = map.get(name);
            if (result != null) {
                return result;
            }

            Object parameterObject = map.get(PARAMETER_OBJECT_KEY);
            if (parameterObject instanceof Map) {
                return ((Map)parameterObject).get(name);
            }

            return null;
        }

        @Override
        public void setProperty(Map context, Object target, Object name, Object value)
                throws OgnlException {
            Map<Object, Object> map = (Map<Object, Object>) target;
            map.put(name, value);
        }

        @Override
        public String getSourceAccessor(OgnlContext arg0, Object arg1, Object arg2) {
            return null;
        }

        @Override
        public String getSourceSetter(OgnlContext arg0, Object arg1, Object arg2) {
            return null;
        }
    }

}
