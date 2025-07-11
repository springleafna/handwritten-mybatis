package com.springleaf.mybatis.datasource;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * 数据源工厂接口
 */
public interface DataSourceFactory {

    void setProperties(Properties props);

    DataSource getDataSource();
}
