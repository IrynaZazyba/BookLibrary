package com.itechart.javalab.library.service;

import com.itechart.javalab.library.domain.BookFilter;
import com.itechart.javalab.library.domain.Paginator;
import com.itechart.javalab.library.domain.entity.Book;
import com.itechart.javalab.library.dto.BookDto;

import javax.servlet.http.Part;
import java.util.List;
import java.util.Optional;

public interface BookService {

    /**
     * Getting books, uses Paginator to get paged output,
     * calls a method from dao
     *
     * @param paginator       see {@link Paginator}
     * @param isAvailableOnly allow to choose available(true)
     *                        or all(false) books
     */
    Optional<List<Book>> getBooks(Paginator paginator, boolean isAvailableOnly);

    /**
     * Getting number books records according to param
     * used to define count pages in {@link Paginator}
     *
     * @param isAvailableOnly allow to choose available(true)
     *                        or all(false) books
     */
    Optional<Integer> getNumberBooksRecords(boolean isAvailableOnly);

    /**
     * Getting books, uses Paginator to get paged output and
     * {@link BookFilter} to get books by filter parameters,
     * calls a method from dao
     *
     * @param paginator  see {@link Paginator}
     * @param bookFilter see {@link BookFilter}
     */
    Optional<List<Book>> findBooksByParameters(Paginator paginator, BookFilter bookFilter);

    /**
     * Getting number books records according to param
     * used to define count pages in {@link Paginator}
     *
     * @param bookFilter allow to find books by filter parameters
     */
    Optional<Integer> getNumberFoundBooksRecords(BookFilter bookFilter);

    /**
     * Getting book by param
     *
     * @param bookId book id
     */
    Optional<Book> getBookById(int bookId);

    /**
     * Transforming dto object to entity, calls a method from dao to
     * update book, if cover file exists uploads file
     *
     * @param bookDto  book dto
     * @param part     file with cover
     * @param savePath directory to save file
     */
    Optional<Boolean> updateBookInfo(BookDto bookDto, Part part, String savePath);

    /**
     * Delete books by param, calls a method from dao
     *
     * @param booksId array of books ids
     */
    boolean deleteBooks(int[] booksId);

    /**
     * Transforming dto object to entity, calls a method from dao to
     * add book, if cover file exists uploads file and updates cover path
     * calls a method from dao
     *
     * @param bookDto  book dto
     * @param part     file with cover
     * @param savePath directory to save file
     */
    int createBook(BookDto bookDto, Part part, String savePath);
}
