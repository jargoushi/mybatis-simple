package com.rwb.session;

import com.rwb.binding.MapperProxy;
import com.rwb.config.Configuration;
import com.rwb.executor.Executor;
import com.rwb.executor.DefaultExecutor;

import java.lang.reflect.Proxy;
import java.util.List;

/**
 * 默认sqlsession实现
 * 对外提供数据库操作API, 并把真正对数据库的操作委托给Executor
 */
public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    private Executor executor;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
        this.executor = new DefaultExecutor(configuration);
    }

    /**
     * 根据传入的参数查询单笔信息
     *
     * @param sqlId                 查询sqlId, namespace + "." + id
     * @param parameter             参数
     * @param <T>                   泛型对象
     * @return
     */
    public <T> T selectOne(String sqlId, Object parameter) {
        List<T> selectList = this.selectList(sqlId, parameter);
        if (selectList == null || selectList.size() == 0) {
            return null;
        }
        if (selectList.size() == 1) {
            return selectList.get(1);
        }
        throw new RuntimeException("查询单条记录却查询到多条记录");
    }

    /**
     * 根据传入的参数查询多条信息
     *
     * @param sqlId                 查询sqlId, namespace + "." + id
     * @param parameter             参数
     * @param <T>                   泛型对象
     * @return
     */
    public <T> List<T> selectList(String sqlId, Object parameter) {

        return executor.query(configuration.getMappedStatementMap().get(sqlId), parameter);
    }

    /**
     *  根据接口产生代理对象
     *      1.需要用谁的类加载器来加载 (要实现的接口的类加载器)
     *      2.生成的实现类需要实现的接口
     *      3.动态代理类, 需要增强的InvocationHandler. 代理类最终回调SqlSession中的方法
     *
     * @param type          接口类型
     * @param <T>           泛型对象
     * @return
     */
    public <T> T getMapper(Class<T> type) {

        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[] {type}, new MapperProxy(this));
    }
}
