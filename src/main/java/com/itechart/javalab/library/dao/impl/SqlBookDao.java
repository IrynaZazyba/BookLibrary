package com.itechart.javalab.library.dao.impl;

import com.itechart.javalab.library.dao.BookDao;
import com.itechart.javalab.library.dao.conn.ConnectionPool;
import com.itechart.javalab.library.dao.exception.DaoRuntimeException;
import com.itechart.javalab.library.model.*;
import lombok.extern.log4j.Log4j2;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

@Log4j2
public class SqlBookDao implements BookDao {

    private final ConnectionPool connectionPool;
    private static volatile BookDao instance;

    private final static String GET_BOOKS = "SELECT book.id, title, publish_date, in_stock, author.id, author.name " +
            "FROM (SELECT * FROM book WHERE in_stock REGEXP ? LIMIT ?,?) as book " +
            "INNER JOIN book_has_author ON book_has_author.book_id=book.id " +
            "INNER JOIN author ON book_has_author.author_id=author.id";

    private final static String GET_NUMBER_OF_BOOKS_RECORDS = "SELECT count(id) FROM book WHERE in_stock REGEXP ?";

    private final static String SEARCH_BOOKS = "SELECT book.id, title, publish_date, author.id, author.name,in_stock " +
            "FROM book INNER JOIN " +
            "(SELECT book.id as book_id FROM book " +
            "INNER JOIN book_has_author ON book_has_author.book_id=book.id " +
            "INNER JOIN author ON book_has_author.author_id=author.id " +
            "INNER JOIN genre_has_book ON genre_has_book.book_id=book.id " +
            "INNER JOIN genre ON genre.id=genre_has_book.genre_id " +
            "WHERE in_stock REGEXP ? AND title LIKE ? AND author.name LIKE ? AND genre.genre LIKE ? " +
            "AND description LIKE ? GROUP BY book.id LIMIT ?,?) as temp on book.id=temp.book_id " +
            "INNER JOIN book_has_author on book_has_author.book_id=book.id " +
            "INNER JOIN author on author.id=book_has_author.author_id;";


    private final static String GET_NUMBER_OF_FOUND_RECORDS = "SELECT count (DISTINCT book.id) FROM book " +
            "INNER JOIN book_has_author ON book_has_author.book_id=book.id " +
            "INNER JOIN author ON book_has_author.author_id=author.id " +
            "INNER JOIN genre_has_book ON genre_has_book.book_id=book.id " +
            "INNER JOIN genre ON genre.id=genre_has_book.genre_id " +
            "WHERE in_stock REGEXP ? AND title LIKE ? AND author.name LIKE ? AND genre.genre LIKE ?" +
            " AND description LIKE ?";


    private final static String GET_BOOK_BY_ID = "SELECT book.id, title,author.id, author.name, " +
            "publisher.id,publisher.publisher, book.publish_date,book.page_count,book.ISBN,book.description, " +
            "book.total_amount, book.in_stock, genre.id, genre.genre FROM book " +
            "INNER JOIN book_has_author ON book_has_author.book_id=book.id " +
            "INNER JOIN author ON book_has_author.author_id=author.id " +
            "INNER JOIN genre_has_book ON genre_has_book.book_id=book.id " +
            "INNER JOIN genre ON genre.id=genre_has_book.genre_id " +
            "INNER JOIN publisher ON publisher.id=book.publisher_id " +
            "WHERE book.id=?";

    private final static String GET_EARLIEST_DUE_DATE_BY_BOOK_ID = "SELECT MIN(due_date) FROM borrow_list " +
            "WHERE book_id=? and return_date is null";


    private SqlBookDao(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public static BookDao getInstance() {
        if (instance == null) {
            synchronized (SqlBookDao.class) {
                if (instance == null) {
                    instance = new SqlBookDao(ConnectionPool.getInstance());
                }
            }
        }
        return instance;
    }

    @Override
    public Optional<List<Book>> getBooks(Paginator paginator, boolean isAvailableOnly) {
        List<Book> books;
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_BOOKS)) {
            preparedStatement.setString(1, isAvailableOnly ? "[^0]" : "[0-9]");
            preparedStatement.setInt(2, paginator.getStart());
            preparedStatement.setInt(3, paginator.getRecordsPerPage());

            ResultSet resultSet = preparedStatement.executeQuery();
            books = parseBooks(resultSet);
        } catch (SQLException ex) {
            log.error("SqlException in getBooks() method", ex);
            throw new DaoRuntimeException("SqlException in SqlReaderDao getBooks() method", ex);
        }
        return Optional.of(books);
    }

    @Override
    public Optional<Integer> getNumberBooksRecords(boolean isAvailableOnly) {
        int countBooksRecords = 0;
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_NUMBER_OF_BOOKS_RECORDS)) {
            preparedStatement.setString(1, isAvailableOnly ? "[^0]" : "[0-9]");
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                countBooksRecords = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            log.error("SqlException in getNumberBooksRecords() method", e);
            throw new DaoRuntimeException("SqlException in SqlReaderDao getNumberBooksRecords() method", e);
        }
        return Optional.of(countBooksRecords);
    }


    @Override
    public Optional<List<Book>> findBooksByParameters(Paginator paginator, BookFilter bookFilter) {
        List<Book> books;
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_BOOKS)) {
            setQueryParameterValue(preparedStatement, bookFilter);
            preparedStatement.setInt(6, paginator.getStart());
            preparedStatement.setInt(7, paginator.getRecordsPerPage());

            ResultSet resultSet = preparedStatement.executeQuery();
            books = parseBooks(resultSet);
        } catch (SQLException e) {
            log.error("SqlException in findBooksByParameters() method", e);
            throw new DaoRuntimeException("SqlException in SqlReaderDao findBooksByParameters() method", e);
        }
        return Optional.of(books);
    }

    @Override
    public Optional<Integer> getNumberFoundBooksRecords(BookFilter bookFilter) {
        int countBooksRecords = 0;
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_NUMBER_OF_FOUND_RECORDS)) {
            setQueryParameterValue(preparedStatement, bookFilter);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                countBooksRecords = resultSet.getInt(1);
            }

        } catch (SQLException e) {
            log.error("SqlException in getNumberFoundBooksRecords() method", e);
            throw new DaoRuntimeException("SqlException in SqlReaderDao getNumberFoundBooksRecords() method", e);
        }
        return Optional.of(countBooksRecords);
    }

    @Override
    public Optional<Book> getBookById(int bookId) {
        Book book = null;
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_BOOK_BY_ID)) {
            preparedStatement.setInt(1, bookId);
            ResultSet resultSet = preparedStatement.executeQuery();

            Set<Author> authors = new HashSet<>();
            Set<Genre> genres = new HashSet<>();
            while (resultSet.next()) {
                if (book == null) {
                    book = Book.extractForBookPage(resultSet);
                }
                genres.add(Genre.buildFrom(resultSet));
                authors.add(Author.buildFrom(resultSet));
            }

            if (book != null) {
                book.setGenres(genres);
                book.setAuthor(authors);
            }
        } catch (SQLException e) {
            log.error("SqlException in getBookById() method", e);
            throw new DaoRuntimeException("SqlException in SqlBookDao getBookById() method", e);
        }
        return Optional.ofNullable(book);
    }

    @Override
    public Optional<LocalDateTime> getEarliestDueDate(int bookId) {
        LocalDateTime earliestDueDate = null;
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_EARLIEST_DUE_DATE_BY_BOOK_ID)) {
            preparedStatement.setInt(1, bookId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Timestamp timestamp = resultSet.getTimestamp(1);
                if (timestamp != null) {
                    earliestDueDate = timestamp.toLocalDateTime();
                }
            }
        } catch (SQLException e) {
            log.error("SqlException in getEarliestDueDate() method", e);
            throw new DaoRuntimeException("SqlException in SqlBookDao getEarliestDueDate() method", e);
        }
        return Optional.ofNullable(earliestDueDate);
    }


    private void setQueryParameterValue(PreparedStatement preparedStatement, BookFilter bookFilter)
            throws SQLException {
        preparedStatement.setString(1, bookFilter.isAvailableOnly() ? "[^0]" : "[0-9]");
        preparedStatement.setString(2, "%" + bookFilter.getBookTitle() + "%");
        preparedStatement.setString(3, "%" + bookFilter.getBookAuthor() + "%");
        preparedStatement.setString(4, "%" + bookFilter.getBookGenre() + "%");
        preparedStatement.setString(5, "%" + bookFilter.getBookDescription() + "%");
    }

    private List<Book> parseBooks(ResultSet resultSet) throws SQLException {
        Map<Integer, Book> tempBooks = new HashMap<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("book.id");
            if (tempBooks.containsKey(id)) {
                Book existsBook = tempBooks.get(id);
                existsBook.getAuthor().add(Author.buildFrom(resultSet));
            } else {
                Book book = Book.extractForMainPage(resultSet);
                tempBooks.put(id, book);
            }
        }
        return new ArrayList<>((tempBooks.values()));
    }

}



