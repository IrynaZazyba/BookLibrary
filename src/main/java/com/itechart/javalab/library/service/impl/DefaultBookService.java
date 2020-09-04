package com.itechart.javalab.library.service.impl;

import com.itechart.javalab.library.dao.BookDao;
import com.itechart.javalab.library.dao.impl.SqlBookDao;
import com.itechart.javalab.library.model.Book;
import com.itechart.javalab.library.model.BookFilter;
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
    public Optional<List<Book>> getBooks(Paginator paginator, boolean isAvailableOnly) {
        BookFilter bookFilter = BookFilter
                .builder()
                .isAvailableOnly(isAvailableOnly)
                .build();
        return bookDao.getBooks(paginator, bookFilter);
    }

    @Override
    public Optional<Integer> getNumberOfBooksRecords(boolean isAvailableOnly) {
        BookFilter bookFilter = BookFilter
                .builder()
                .isAvailableOnly(isAvailableOnly)
                .build();
        return bookDao.getNumberOfBooksRecords(bookFilter);
    }

    @Override
    public Optional<List<Book>> findBooksByParameters(boolean isAvailableOnly,
                                                      String bookTitleSearchParameter,
                                                      String bookAuthorSearchParameter,
                                                      String bookGenreSearchParameter,
                                                      String bookDescriptionSearchParameter) {
        BookFilter bookFilter = BookFilter
                .builder()
                .isAvailableOnly(isAvailableOnly)
                .bookAuthor(bookAuthorSearchParameter)
                .bookDescription(bookDescriptionSearchParameter)
                .bookGenre(bookGenreSearchParameter)
                .bookTitle(bookTitleSearchParameter)
                .build();
        return bookDao.findBooksByParameters(bookFilter);
    }
}
