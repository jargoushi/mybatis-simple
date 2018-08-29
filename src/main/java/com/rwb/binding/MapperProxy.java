package com.rwb.binding;

import com.rwb.session.SqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * @Auther: ruwenbo
 * @Date: 2018/8/29 21:53
 * @Description:
 */
public class MapperProxy implements InvocationHandler {

    private SqlSession sqlSession;

    public MapperProxy(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Collection.class.isAssignableFrom(method.getReturnType())) {
            return sqlSession.selectList(method.getDeclaringClass().getName() + "." + method.getName(), args == null ? null : args[0]);
        } else {
            return sqlSession.selectOne(method.getDeclaringClass().getName() + "." + method.getName(), args == null ? null : args[0]);
        }
    }
}
