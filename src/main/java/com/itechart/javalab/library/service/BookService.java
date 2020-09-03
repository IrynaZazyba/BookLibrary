package com.itechart.javalab.library.service;

import com.itechart.javalab.library.model.Book;
import com.itechart.javalab.library.model.Paginator;

import java.util.List;
import java.util.Optional;

public interface BookService {

    Optional<List<Book>> getAllBooks(Paginator paginator,boolean isAvailableOnly);
    Optional<Integer> getNumberOfBooksRecords(boolean isAvailableOnly);


}
