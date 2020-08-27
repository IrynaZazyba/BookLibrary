package com.itechart.javalab.library.dao.impl;

import com.itechart.javalab.library.dao.BookDao;
import com.itechart.javalab.library.dao.conn.ConnectionPool;
import com.itechart.javalab.library.model.Book;

import java.util.List;

public class SqlBookDaoImpl implements BookDao {

    private ConnectionPool connectionPool;

    public SqlBookDaoImpl(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public List<Book> getBooks() {
        return null;
    }

    @Override
    public void addBook() {

    }
}
