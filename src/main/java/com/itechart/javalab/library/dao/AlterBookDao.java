package com.itechart.javalab.library.dao;

import com.itechart.javalab.library.model.Book;
import com.itechart.javalab.library.model.BookFilter;
import com.itechart.javalab.library.model.Paginator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AlterBookDao {

    Optional<Boolean> updateBookInfo(Book book);

    boolean deleteBooks(int[] bookId);

    int createBook(Book book);

    void updateBookCover(Book book);
}
