package com.itechart.javalab.library.service;

import com.itechart.javalab.library.model.Book;

import java.util.List;

public interface BookService {

    void addBook();
    List<Book> getBooks();
}
