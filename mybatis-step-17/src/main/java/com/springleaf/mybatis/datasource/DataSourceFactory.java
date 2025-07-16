package com.springleaf.mybatis.datasource;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * 数据源工厂接口
 */
public interface DataSourceFactory {

    // 设置DataSource的相关属性，一般紧跟在初始化完成之后
    void setProperties(Properties props);

    DataSource getDataSource();
}
