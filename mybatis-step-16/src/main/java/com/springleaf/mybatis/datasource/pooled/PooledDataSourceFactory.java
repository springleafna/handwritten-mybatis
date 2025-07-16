package com.springleaf.mybatis.datasource.pooled;

import com.springleaf.mybatis.datasource.pooled.PooledDataSource;
import com.springleaf.mybatis.datasource.unpooled.UnpooledDataSourceFactory;

/**
 * 有连接池的数据源工厂
 */
public class PooledDataSourceFactory extends UnpooledDataSourceFactory {

    public PooledDataSourceFactory() {
        this.dataSource = new PooledDataSource();
    }

}
