package com.itechart.javalab.library.dao.factory.impl;

import com.itechart.javalab.library.dao.BookDao;
import com.itechart.javalab.library.dao.conn.ConnectionPool;
import com.itechart.javalab.library.dao.factory.DAOFactory;
import com.itechart.javalab.library.dao.impl.SqlBookDaoImpl;

public class SqlDAOFactoryImpl implements DAOFactory {

    private static volatile SqlDAOFactoryImpl instance;
    private BookDao bookDao;

    private SqlDAOFactoryImpl() {
    }

    public static SqlDAOFactoryImpl getInstance() {
        if (instance == null) {
            synchronized (SqlDAOFactoryImpl.class) {
                if (instance == null) {
                    instance = new SqlDAOFactoryImpl();
                }
            }
        }
        return instance;
    }


    public BookDao getBookDAO() {
        if (bookDao == null) {
            synchronized (SqlBookDaoImpl.class) {
                if (bookDao == null) {
                    bookDao = new SqlBookDaoImpl(ConnectionPool.getInstance());
                }
            }
        }
        return bookDao;
    }
}
