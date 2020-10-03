package com.itechart.javalab.library.dao;

import com.itechart.javalab.library.domain.entity.Book;

import java.util.Optional;

public interface AlterBookDao {

    /**
     * Update book info
     *
     * @param book see{@link Book}
     */
    Optional<Boolean> updateBookInfo(Book book);

    /**
     * Delete book info, performed transactionally,
     * deleting all links of a book table to other tables,
     * fails if the condition total_amount = in_stock is violated
     *
     * @param bookId book id
     */
    boolean deleteBooks(int[] bookId);

    /**
     * Create book, performed transactionally,
     * authors and genres are added, if necessary, and linking
     * with the book, added publisher, if necessary, and linking
     * with the book, inserted book info
     *
     * @param book see{@link Book}
     * @return generated id
     */
    int createBook(Book book);

    /**
     * Update book cover name
     *
     * @param book see{@link Book}
     */
    void updateBookCover(Book book);
}
