package com.itechart.javalab.library.controller.listener;

import com.itechart.javalab.library.dao.conn.ConnectionPool;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

@WebListener
public class ConnectionPoolListener implements ServletContextListener {

    private final static Logger logger = LogManager.getLogger(ConnectionPoolListener.class);

    private final static String DB_PROPERTIES_FILE_NAME = "db";
    private final static String DB_URL = "db.url";
    private final static String DB_USER = "db.user";
    private final static String DB_PASSWORD = "db.password";
    private final static String DB_POOL_SIZE = "db.poolsize";
    private final static int DB_DEFAULT_POOL_SIZE = 5;

    private ConnectionPool connectionPool;

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        Properties dbProperties = getDbProperties(sce);
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
            connectionPool.initPoolData(dbUrl, dbUser, dbPassword, dbPoolSize);
        } catch (SQLException e) {
            logger.log(Level.ERROR,"Exception in attempt to initialize connection pool",e);
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        connectionPool.closePool();
    }


    private Properties getDbProperties(ServletContextEvent sce) {
        Properties dbProperties = new Properties();

        String db = sce.getServletContext().getInitParameter(DB_PROPERTIES_FILE_NAME);
        try (InputStream dbResourceAsStream = getClass().getClassLoader().getResourceAsStream(db)) {
            if (dbResourceAsStream != null) {
                dbProperties.load(dbResourceAsStream);
            } else {
                logger.log(Level.ERROR,DB_PROPERTIES_FILE_NAME+".properties resource file not found");
            }
        } catch (IOException e) {
            logger.log(Level.ERROR,"Exception in attempt to read "+DB_PROPERTIES_FILE_NAME+".properties file",e);
        }
        return dbProperties;
    }

}
