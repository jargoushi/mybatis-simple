package com.rwb;

import com.rwb.mapper.IBookMapper;
import com.rwb.session.SqlSession;
import com.rwb.session.SqlSessionFactory;

/**
 * Mybatis 的工作原理:
 *  分为三部分, 分别是初始化, 代理, 操作数据库读写
 *      1. 初始化加载配置文件解析加载到内存configuration中
 *      2. 生成代理对象, 最终回调SqlSession中的方法
 *      3. sqlSession仅仅是对外提供数据库操作API, 实际操作数据库的过程委托给Executor来实现
 *      executor对数据库的操作依然要遵循jdbc规范
 *
 * mybatis中的核心类有:
 *      sqlSession
 *      configuration
 *      executor
 *      mapperProxy
 */
public class TestMybatis {

    public static void main(String[] args) {

        SqlSessionFactory factory = new SqlSessionFactory();

        SqlSession sqlSession = factory.openSession();

        IBookMapper mapper = sqlSession.getMapper(IBookMapper.class);

        mapper.selectByPrimaryKey(1);
    }
}
