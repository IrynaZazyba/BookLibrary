package com.itechart.javalab.library.service;

import com.itechart.javalab.library.dto.BookDto;
import com.itechart.javalab.library.model.Book;
import com.itechart.javalab.library.model.BookFilter;
import com.itechart.javalab.library.model.Paginator;

import javax.servlet.http.Part;
import java.util.List;
import java.util.Optional;

public interface BookService {

    Optional<List<Book>> getBooks(Paginator paginator, boolean isAvailableOnly);

    Optional<Integer> getNumberBooksRecords(boolean isAvailableOnly);

    Optional<List<Book>> findBooksByParameters(Paginator paginator, BookFilter bookFilter);

    Optional<Integer> getNumberFoundBooksRecords(BookFilter bookFilter);

    Optional<Book> getBookById(int bookId);

    Optional<Boolean> updateBookInfo(BookDto bookDto, Part part, String savePath);
}
