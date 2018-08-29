package com.rwb.executor;

import com.rwb.config.MappedStatement;

import java.util.List;

public interface Executor {

    /**
     * 查询接口
     *
     * @param ms            封装sql语句的MappedStatement对象
     * @param parameter     查询使用的参数
     * @param <E>           泛型对象
     * @return              查询的结果
     */
    <E> List<E> query(MappedStatement ms, Object parameter);
}
