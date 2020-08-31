package com.itechart.javalab.library.service.impl;

import com.itechart.javalab.library.dao.BookDao;
import com.itechart.javalab.library.dao.impl.SqlBookDao;
import com.itechart.javalab.library.service.BookService;

public class DefaultBookService implements BookService {


    private final BookDao bookDao;
    public static volatile BookService instance;

    private DefaultBookService(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    public static BookService getInstance() {
        if (instance == null) {
            synchronized (BookService.class) {
                if (instance == null) {
                    instance = new DefaultBookService(SqlBookDao.getInstance());
                }
            }
        }
        return instance;
    }



}
