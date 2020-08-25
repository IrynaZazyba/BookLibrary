package com.itechart.javalab.library.dao;

import com.itechart.javalab.library.model.Book;

import java.util.List;

public interface BookDao {

    List<Book> getBooks();
    void addBook();

}
