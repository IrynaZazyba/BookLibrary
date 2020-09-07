package com.itechart.javalab.library.dao;

import com.itechart.javalab.library.model.Book;
import com.itechart.javalab.library.model.BookFilter;
import com.itechart.javalab.library.model.Paginator;

import java.util.List;
import java.util.Optional;

public interface BookDao {

    Optional<List<Book>> getBooks(Paginator paginator, boolean isAvailableOnly);

    Optional<Integer> getNumberBooksRecords(boolean isAvailableOnly);

    Optional<List<Book>> findBooksByParameters(Paginator paginator, BookFilter bookFilter);

    Optional<Integer> getNumberFoundBooksRecords(BookFilter bookFilter);
}
