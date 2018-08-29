package com.rwb.session;

import com.rwb.config.Configuration;
import com.rwb.config.MappedStatement;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Properties;

/**
 * 1.加载配置文件到内存configuration
 * 2.生产SqlSession
 */
public class SqlSessionFactory {

    /**
     * 日志组件
     */
    private static final Logger logger = LoggerFactory.getLogger(SqlSessionFactory.class);

    /**
     * 存放配置
     */
    private Configuration configuration = new Configuration();

    /**
     * 记录mapper.xml文件存放的路径
     */
    private static final String MAPPER_CONFIG_LOCATION = "mappers";

    /**
     * 记录数据库连接信息配置文件存放的路径
     */
    private static final String DB_CONFIG_LOCATION = "jdbc.properties";

    public SqlSessionFactory() {
        // 加载数据库信息到configuration
        loadDbInfo();
        // 加载xml到configuration中mappedStatement
        loadMappersInfo();
    }

    public SqlSession openSession() {
        // 每个线程产生一个sqlSession
        return new DefaultSqlSession(configuration);
    }

    /**
     * 加载指定文件夹下所有的mapper.xml
     */
    private void loadMappersInfo() {

        URL resources = this.getClass().getClassLoader().getResource(MAPPER_CONFIG_LOCATION);
        File mappers = new File(resources.getFile());
        if (mappers.isDirectory()) {
            File[] listFiles = mappers.listFiles();
            // 遍历文件夹下所有的mapper.xml, 解析信息后注册到configuration对象
            for (File file : listFiles) {
                loadMapperInfo(file);
            }
        }
    }

    /**
     * 加载指定的mapper.xml
     *
     * @param file
     */
    private void loadMapperInfo(File file) {

        // 创建SaxReader对象
        SAXReader saxReader = new SAXReader();
        // 通过read方法读取一个文件 转换成Document对象
        Document document = null;
        try {
            document = saxReader.read(file);
        } catch (DocumentException e) {
            logger.error("read xml err.", e);
        }
        // 获取根节点mapper元素
        Element rootElement = document.getRootElement();
        // 获取命名空间
        String namespace = rootElement.attribute("namespace").getData().toString();
        // 获取select子节点列表
        List<Element> selects = rootElement.elements("select");
        // 遍历selects节点, 将信息加载到MappedStatement对象, 并记录到Configuration对象中
        for (Element element : selects) {
            MappedStatement statement = new MappedStatement();
            String id = element.attribute("id").getData().toString();
            String resultType = element.attribute("resultType").getData().toString();
            String sql = element.getData().toString();
            String sqlId = namespace + "." + id;

            statement.setNamespace(namespace);
            statement.setResultType(resultType);
            statement.setSql(sql);
            statement.setSqlId(sqlId);

            configuration.getMappedStatementMap().put(sqlId, statement);
        }
    }

    /**
     * 加载数据库信息到configuration
     */
    private void loadDbInfo() {

        // 加载数据库配置信息文件
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(DB_CONFIG_LOCATION);
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            logger.error("load db info err.", e);
        }
        /** 将数据库信息写入到configuration对象 */
        configuration.setDriver(properties.get("jdbc.driver").toString());
        configuration.setUrl(properties.get("jdbc.url").toString());
        configuration.setUserName(properties.get("jdbc.username").toString());
        configuration.setPassword(properties.get("jdbc.password").toString());

    }
}
