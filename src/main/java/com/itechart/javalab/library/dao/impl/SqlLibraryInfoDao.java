package com.itechart.javalab.library.dao.impl;

import com.itechart.javalab.library.dao.LibraryInfoDao;
import com.itechart.javalab.library.dao.conn.ConnectionPool;
import com.itechart.javalab.library.model.EmailInfo;
import lombok.extern.log4j.Log4j2;

import java.util.Optional;

@Log4j2
public class SqlLibraryInfoDao implements LibraryInfoDao {

    private final ConnectionPool connectionPool;
    private static volatile LibraryInfoDao instance;


    private SqlLibraryInfoDao(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public static LibraryInfoDao getInstance() {
        if (instance == null) {
            synchronized (LibraryInfoDao.class) {
                if (instance == null) {
                    instance = new SqlLibraryInfoDao(ConnectionPool.getInstance());
                }
            }
        }
        return instance;
    }


    @Override
    public Optional<EmailInfo> getLibraryInfo() {
        return Optional.empty();
    }
}
