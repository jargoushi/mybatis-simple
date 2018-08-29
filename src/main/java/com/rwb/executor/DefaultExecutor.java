package com.rwb.executor;

import com.rwb.config.Configuration;
import com.rwb.config.MappedStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * executor实现, 真实操作数据库
 */
public class DefaultExecutor implements Executor {

    private static final Logger logger = LoggerFactory.getLogger(DefaultExecutor.class);

    private Configuration configuration;

    public DefaultExecutor(Configuration configuration) {
        this.configuration = configuration;
    }

    public <E> List<E> query(MappedStatement ms, Object parameter) {

        List<E> resultList = new ArrayList<E>();

        try {
            Class.forName(configuration.getDriver());
        } catch (ClassNotFoundException e) {
            logger.error("load dataSource err.", e);
        }

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(configuration.getUrl(), configuration.getUserName(), configuration.getPassword());
            statement = connection.prepareStatement(ms.getSql());
            parameterize(statement, parameter);
            resultSet = statement.executeQuery();
            handlerResultSet(resultSet, resultList, ms.getResultType());
        } catch (SQLException e) {
            logger.error("load dataSource err.", e);
        } finally {
            try {
                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                logger.warn("close dataSource err.", e);
            }
        }

        return resultList;
    }

    /**
     * 将查询的结果转换为指定类型的结果
     *
     * @param resultSet         数据库查询的结果
     * @param resultList        最终返回的结果
     * @param className        返回结果的对象类型
     * @param <E>               泛型对象
     */
    private <E> void handlerResultSet(ResultSet resultSet, List<E> resultList, String className) {
        Class<E> clazz = null;
        try {
            clazz = (Class<E>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            logger.error("reflect err.", e);
        }
        try {
            while (resultSet.next()) {
                Object entity = clazz.newInstance();
                // 反射将查询结果塞到结果对象中
                // TODO
                resultList.add((E) entity);
            }
        } catch (Exception e) {
            logger.error("transformation err.", e);
        }
    }

    /**
     * 处理占位符
     * @param statement     statement对象
     * @param parameter     参数类型
     */
    private void parameterize(PreparedStatement statement, Object parameter) throws SQLException {

        if (parameter instanceof Integer) {
            statement.setInt(1, (Integer) parameter);
        } else if (parameter instanceof Long) {
            statement.setLong(1, (Long) parameter);
        } else if (parameter instanceof String) {
            statement.setString(1, (String) parameter);
        }
    }
}
