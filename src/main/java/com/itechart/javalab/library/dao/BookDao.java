package com.itechart.javalab.library.dao;

import com.itechart.javalab.library.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookDao {

    Optional<List<Book>> getBooks();

}
