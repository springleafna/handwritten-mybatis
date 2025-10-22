package com.springleaf.mybatis.datasource.pooled;

import java.util.ArrayList;
import java.util.List;

/**
 * 池状态
 * 负责管理连接池中所有 PooledConnection 对象的状态
 */
public class PoolState {

    protected PooledDataSource dataSource;

    // 空闲连接
    protected final List<PooledConnection> idleConnections = new ArrayList<>();
    // 活跃连接
    protected final List<PooledConnection> activeConnections = new ArrayList<>();
    // 请求数据库连接的次数
    protected long requestCount = 0;
    // 获取连接的累积耗时
    protected long accumulatedRequestTime = 0;
    // 所有连接的 checkoutTime 累加。PooledConnection 中有一个 checkoutTime 属性，表示的是使用方从连接池中取出连接到归还连接的总时长，也就是连接被使用的时长
    protected long accumulatedCheckoutTime = 0;
    // 当连接长时间未归还给连接池时，会被认为该连接超时，该字段记录了超时的连接个数
    protected long claimedOverdueConnectionCount = 0;
    // 记录累积超时时间
    protected long accumulatedCheckoutTimeOfOverdueConnections = 0;
    // 当连接池全部连接已经被占用之后，新的请求会阻塞等待，该字段就记录了累积的阻塞等待总时间
    protected long accumulatedWaitTime = 0;
    // 记录了阻塞等待总次数
    protected long hadToWaitCount = 0;
    // 无效的连接数
    protected long badConnectionCount = 0;

    public PoolState(PooledDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public synchronized long getRequestCount() {
        return requestCount;
    }

    public synchronized long getAverageRequestTime() {
        return requestCount == 0 ? 0 : accumulatedRequestTime / requestCount;
    }

    public synchronized long getAverageWaitTime() {
        return hadToWaitCount == 0 ? 0 : accumulatedWaitTime / hadToWaitCount;
    }

    public synchronized long getHadToWaitCount() {
        return hadToWaitCount;
    }

    public synchronized long getBadConnectionCount() {
        return badConnectionCount;
    }

    public synchronized long getClaimedOverdueConnectionCount() {
        return claimedOverdueConnectionCount;
    }

    public synchronized long getAverageOverdueCheckoutTime() {
        return claimedOverdueConnectionCount == 0 ? 0 : accumulatedCheckoutTimeOfOverdueConnections / claimedOverdueConnectionCount;
    }

    public synchronized long getAverageCheckoutTime() {
        return requestCount == 0 ? 0 : accumulatedCheckoutTime / requestCount;
    }

    public synchronized int getIdleConnectionCount() {
        return idleConnections.size();
    }

    public synchronized int getActiveConnectionCount() {
        return activeConnections.size();
    }

}
