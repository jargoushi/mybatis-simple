package com.rwb.session;


import java.util.List;

/**
 * 暴露给外部的接口, 实现增删改查的能力
 */
public interface SqlSession {

    /**
     * 根据传入的参数查询单笔信息
     *
     * @param sqlId                 查询sqlId, namespace + "." + id
     * @param parameter             参数
     * @param <T>                   泛型对象
     * @return
     */
    <T> T selectOne(String sqlId, Object parameter);

    /**
     * 根据传入的参数查询多条信息
     *
     * @param sqlId                 查询sqlId, namespace + "." + id
     * @param parameter             参数
     * @param <T>                   泛型对象
     * @return
     */
    <T> List<T> selectList(String sqlId, Object parameter);

    /**
     *  根据接口产生代理对象
     *
     * @param type          接口类型
     * @param <T>           泛型对象
     * @return
     */
    <T> T getMapper(Class<T> type);
}
