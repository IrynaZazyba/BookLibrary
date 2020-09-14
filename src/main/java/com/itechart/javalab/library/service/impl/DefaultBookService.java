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
        return bookDao.getBooks(paginator, isAvailableOnly);
    }

    @Override
    public Optional<Integer> getNumberBooksRecords(boolean isAvailableOnly) {
        return bookDao.getNumberBooksRecords(isAvailableOnly);
    }

    @Override
    public Optional<List<Book>> findBooksByParameters(Paginator paginator, BookFilter bookFilter) {
        return bookDao.findBooksByParameters(paginator, bookFilter);
    }

    @Override
    public Optional<Integer> getNumberFoundBooksRecords(BookFilter bookFilter) {
        return bookDao.getNumberFoundBooksRecords(bookFilter);
    }

    @Override
    public Optional<Book> getBookById(int bookId) {

        Optional<Book> bookById = bookDao.getBookById(bookId);
        if (bookById.isPresent()) {
            Book book = bookById.get();
            calculateBookStatus(book);
            return Optional.of(book);
        }
        return bookById;
    }

    private void calculateBookStatus(Book book) {
        if (book.getInStock() > 0) {
            book.setAvailableStatus();
        } else {
            bookDao.getEarliestDueDate(book.getId()).ifPresent(book::setUnavailableStatus);
        }
    }


}
