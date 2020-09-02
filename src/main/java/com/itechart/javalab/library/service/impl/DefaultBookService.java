package com.itechart.javalab.library.service.impl;

import com.itechart.javalab.library.dao.BookDao;
import com.itechart.javalab.library.dao.impl.SqlBookDao;
import com.itechart.javalab.library.model.Book;
import com.itechart.javalab.library.model.Paginator;
import com.itechart.javalab.library.service.BookService;

import java.util.List;
import java.util.Optional;

public class DefaultBookService implements BookService {


    private final BookDao bookDao;
    private static volatile BookService instance;

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


    @Override
    public Optional<List<Book>> getAllBooks(Paginator paginator,boolean isFiltered) {
        return bookDao.getBooks(paginator,isFiltered);
    }

    @Override
    public Optional<Integer> getNumberOfBooksRecords(boolean isFiltered) {
        return bookDao.getCountOfBooksId(isFiltered);
    }
}
