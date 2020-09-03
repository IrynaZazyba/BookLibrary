package com.itechart.javalab.library.dao;

import com.itechart.javalab.library.model.Book;
import com.itechart.javalab.library.model.BookFilter;
import com.itechart.javalab.library.model.Paginator;

import java.util.List;
import java.util.Optional;

public interface BookDao {

    Optional<List<Book>> getBooks(Paginator paginator, BookFilter bookFilter);
    Optional<Integer> getNumberOfBooksRecords(BookFilter bookFilter);


}
