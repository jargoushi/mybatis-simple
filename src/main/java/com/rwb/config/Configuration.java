package com.rwb.config;

import java.util.HashMap;
import java.util.Map;

public class Configuration {

    /** 数据库连接信息  start */
    private String driver;

    private String url;

    private String userName;

    private String password;
    /** 数据库连接信息  end*/

    /**
     * 存放所有的xml映射对象
     */
    private Map<String, MappedStatement> mappedStatementMap = new HashMap<String, MappedStatement>();


    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Map<String, MappedStatement> getMappedStatementMap() {
        return mappedStatementMap;
    }

    public void setMappedStatementMap(Map<String, MappedStatement> mappedStatementMap) {
        this.mappedStatementMap = mappedStatementMap;
    }
}
