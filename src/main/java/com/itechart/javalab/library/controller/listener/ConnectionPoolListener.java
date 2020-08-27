package com.itechart.javalab.library.controller.listener;

import com.itechart.javalab.library.dao.conn.ConnectionPool;
import com.itechart.javalab.library.dao.conn.ConnectionPoolException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@WebListener
public class ConnectionPoolListener implements ServletContextListener {

    private final static String DB_PROPERTIES_FILE_NAME = "db";

    private final static String DB_DRIVER = "db.driver";
    private final static String DB_URL = "db.url";
    private final static String DB_USER = "db.user";
    private final static String DB_PASSWORD = "db.password";
    private final static String DB_POOL_SIZE = "db.poolsize";
    private final static int DB_DEFAULT_POOL_SIZE = 5;

    private ConnectionPool connectionPool;

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        Properties dbProperties = getDBProperties(sce);
        String dbDriver = dbProperties.getProperty(DB_DRIVER);
        String dbUrl = dbProperties.getProperty(DB_URL);
        String dbUser = dbProperties.getProperty(DB_USER);
        String dbPassword = dbProperties.getProperty(DB_PASSWORD);
        int dbPoolSize;
        try {
            dbPoolSize = Integer.parseInt(dbProperties.getProperty(DB_POOL_SIZE));
        } catch (NumberFormatException ex) {
            dbPoolSize = DB_DEFAULT_POOL_SIZE;
        }
        connectionPool = ConnectionPool.getInstance();
        try {
            connectionPool.initPoolData(dbDriver, dbUrl, dbUser, dbPassword, dbPoolSize);
        } catch (ConnectionPoolException e) {
            //todo logger
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        connectionPool.dispose();
    }


    private Properties getDBProperties(ServletContextEvent sce) {
        Properties dbProperties = new Properties();

        String db = sce.getServletContext().getInitParameter(DB_PROPERTIES_FILE_NAME);
        try {
            InputStream dbResourceAsStream = getClass().getClassLoader().getResourceAsStream(db);
            if (dbResourceAsStream != null) {
                dbProperties.load(dbResourceAsStream);
            } else {
                //todo logger
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dbProperties;
    }

}
