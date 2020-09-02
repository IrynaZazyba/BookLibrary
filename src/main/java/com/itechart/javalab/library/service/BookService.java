package com.itechart.javalab.library.service;

import com.itechart.javalab.library.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    Optional<List<Book>> getAllBooks(boolean isFiltered);


}
