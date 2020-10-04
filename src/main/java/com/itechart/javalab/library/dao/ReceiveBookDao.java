package com.itechart.javalab.library.dao;

import com.itechart.javalab.library.domain.BookFilter;
import com.itechart.javalab.library.domain.Paginator;
import com.itechart.javalab.library.domain.entity.Book;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReceiveBookDao {

    /**
     * Retrieves books according to params
     *
     * @param paginator       see {@link Paginator},
     * @param isAvailableOnly allow to choose available
     *                        or all books
     */
    Optional<List<Book>> getBooks(Paginator paginator, boolean isAvailableOnly);

    /**
     * Retrieves number books` records according to params,
     * used to define count pages in {@link Paginator}
     *
     * @param isAvailableOnly, allow to choose available or
     *                         all books
     */
    Optional<Integer> getNumberBooksRecords(boolean isAvailableOnly);

    /**
     * Retrieves books according to params
     *
     * @param paginator  see {@link Paginator},
     * @param bookFilter see{@link BookFilter}
     */
    Optional<List<Book>> findBooksByParameters(Paginator paginator, BookFilter bookFilter);

    /**
     * Retrieves number books records according to params,
     * used to define count pages in {@link Paginator}
     *
     * @param bookFilter see{@link BookFilter}
     */
    Optional<Integer> getNumberFoundBooksRecords(BookFilter bookFilter);

    /**
     * Retrieves book by
     *
     * @param bookId book id
     */
    Optional<Book> getBookById(int bookId);

    /**
     * Retrieves min due date where return date is NULL by
     *
     * @param bookId book id,
     */
    Optional<LocalDateTime> getEarliestDueDate(int bookId);

    /**
     * Retrieves book cover name
     *
     * @param bookId book id
     */
    String getBookCover(int bookId);
}
