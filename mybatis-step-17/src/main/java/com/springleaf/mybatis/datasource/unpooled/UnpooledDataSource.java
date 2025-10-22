package com.springleaf.mybatis.datasource.unpooled;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * 无池化数据源实现
 * 每次调用 getConnection() 都会创建一个新的数据库连接
 */
public class UnpooledDataSource implements DataSource {

    // 加载 JDBC 驱动的类加载器
    private ClassLoader driverClassLoader;
    // 驱动配置，也可以扩展属性信息 driver.encoding=UTF8
    private Properties driverProperties;
    // 缓存已注册的 JDBC 驱动
    private static Map<String, Driver> registeredDrivers = new ConcurrentHashMap<>();
    // JDBC 驱动类名（如：com.mysql.cj.jdbc.Driver）
    private String driver;
    // JDBC 连接 URL（如：jdbc:mysql://...）
    private String url;
    // 用户名
    private String username;
    // 密码
    private String password;
    // 是否自动提交事务
    private Boolean autoCommit;
    // 默认事务隔离级别
    private Integer defaultTransactionIsolationLevel;

    // 静态初始化块：注册已有驱动
    // 在类加载时，把 JVM 中已有的 JDBC 驱动缓存到 registeredDrivers 中。
    // 避免重复注册驱动，提高性能。
    static {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            registeredDrivers.put(driver.getClass().getName(), driver);
        }
    }

    public UnpooledDataSource() {
    }

    public UnpooledDataSource(String driver, String url, String username, String password) {
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * 驱动代理
     * 对原始 JDBC 驱动进行包装，防止某些容器（如应用服务器）在卸载时抛出异常。
     * 确保兼容性，避免直接使用 DriverManager.unregisterDriver() 出现问题。
     */
    private static class DriverProxy implements Driver {

        private Driver driver;

        DriverProxy(Driver driver) {
            this.driver = driver;
        }

        @Override
        public Connection connect(String u, Properties p) throws SQLException {
            return this.driver.connect(u, p);
        }

        @Override
        public boolean acceptsURL(String u) throws SQLException {
            return this.driver.acceptsURL(u);
        }

        @Override
        public DriverPropertyInfo[] getPropertyInfo(String u, Properties p) throws SQLException {
            return this.driver.getPropertyInfo(u, p);
        }

        @Override
        public int getMajorVersion() {
            return this.driver.getMajorVersion();
        }

        @Override
        public int getMinorVersion() {
            return this.driver.getMinorVersion();
        }

        @Override
        public boolean jdbcCompliant() {
            return this.driver.jdbcCompliant();
        }

        @Override
        public Logger getParentLogger() throws SQLFeatureNotSupportedException {
            return Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        }
    }

    /**
     * 初始化驱动
     * 作用：
     * 加载指定的 JDBC 驱动类（如 com.mysql.cj.jdbc.Driver）
     * 创建实例并封装成 DriverProxy
     * 注册到 DriverManager，以便后续用于获取连接
     * 安全性与并发控制：
     * 使用 synchronized 保证多线程下只加载一次驱动
     * 使用 ConcurrentHashMap 缓存驱动对象，提升并发性能
     */
    private synchronized void initializerDriver() throws SQLException {
        if (!registeredDrivers.containsKey(driver)) {
            try {
                Class<?> driverType = Class.forName(driver, true, driverClassLoader);
                // https://www.kfu.com/~nsayer/Java/dyn-jdbc.html
                Driver driverInstance = (Driver) driverType.newInstance();
                DriverManager.registerDriver(new DriverProxy(driverInstance));
                registeredDrivers.put(driver, driverInstance);
            } catch (Exception e) {
                throw new SQLException("Error setting driver on UnpooledDataSource. Cause: " + e);
            }
        }
    }

    // 这是整个类最核心的部分，负责建立真正的数据库连接。
    private Connection doGetConnection(String username, String password) throws SQLException {
        Properties props = new Properties();
        if (driverProperties != null) {
            props.putAll(driverProperties);
        }
        if (username != null) {
            props.setProperty("user", username);
        }
        if (password != null) {
            props.setProperty("password", password);
        }
        return doGetConnection(props);
    }

    private Connection doGetConnection(Properties properties) throws SQLException {
        // 初始化数据库驱动
        initializerDriver();
        // 创建数据库连接
        Connection connection = DriverManager.getConnection(url, properties);
        // 配置数据库连接
        configureConnection(connection);
        return connection;
    }

    private void configureConnection(Connection conn) throws SQLException {
        if (autoCommit != null && autoCommit != conn.getAutoCommit()) {
            conn.setAutoCommit(autoCommit);
        }
        if (defaultTransactionIsolationLevel != null) {
            conn.setTransactionIsolation(defaultTransactionIsolationLevel);
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return doGetConnection(username, password);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return doGetConnection(username, password);
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new SQLException(getClass().getName() + " is not a wrapper.");
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return DriverManager.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter logWriter) throws SQLException {
        DriverManager.setLogWriter(logWriter);
    }

    @Override
    public void setLoginTimeout(int loginTimeout) throws SQLException {
        DriverManager.setLoginTimeout(loginTimeout);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return DriverManager.getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }

    public ClassLoader getDriverClassLoader() {
        return driverClassLoader;
    }

    public void setDriverClassLoader(ClassLoader driverClassLoader) {
        this.driverClassLoader = driverClassLoader;
    }

    public Properties getDriverProperties() {
        return driverProperties;
    }

    public void setDriverProperties(Properties driverProperties) {
        this.driverProperties = driverProperties;
    }

    public static Map<String, Driver> getRegisteredDrivers() {
        return registeredDrivers;
    }

    public static void setRegisteredDrivers(Map<String, Driver> registeredDrivers) {
        UnpooledDataSource.registeredDrivers = registeredDrivers;
    }

    public String getDriver() {
        return driver;
    }

    public synchronized void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getAutoCommit() {
        return autoCommit;
    }

    public void setAutoCommit(Boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    public Integer getDefaultTransactionIsolationLevel() {
        return defaultTransactionIsolationLevel;
    }

    public void setDefaultTransactionIsolationLevel(Integer defaultTransactionIsolationLevel) {
        this.defaultTransactionIsolationLevel = defaultTransactionIsolationLevel;
    }
}
