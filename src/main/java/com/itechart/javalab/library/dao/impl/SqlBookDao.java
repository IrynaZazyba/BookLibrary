package com.itechart.javalab.library.dao.impl;

import com.itechart.javalab.library.dao.BookDao;
import com.itechart.javalab.library.dao.conn.ConnectionPool;
import com.itechart.javalab.library.model.Author;
import com.itechart.javalab.library.model.Book;
import com.itechart.javalab.library.model.Paginator;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Log4j2
public class SqlBookDao implements BookDao {

    private final ConnectionPool connectionPool;
    private static volatile BookDao instance;

    private final static String GET_ALL_BOOKS = "SELECT book.id, title, publish_date, in_stock,author.id,author.name " +
            "FROM book INNER JOIN author ON book.id=author.book_id";

    private final static String GET_COUNT_BOOKS_RECORDS = "SELECT count(id) FROM book";

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
    public Optional<List<Book>> getBooks(Paginator paginator, boolean isFiltered) {

        List<Book> books;

        String query = addAvailableConditions(GET_ALL_BOOKS, isFiltered);
        query = addConditionLimit(query);

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, paginator.getStart());
            preparedStatement.setInt(2, paginator.getRecordsPerPage());

            ResultSet resultSet = preparedStatement.executeQuery();
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
            books = new ArrayList<>((tempBooks.values()));
        } catch (SQLException ex) {
            log.error("SqlException in attempt to get Connection", ex);
            return Optional.empty();
        }
        return Optional.of(books);
    }

    @Override
    public Optional<Integer> getCountOfBooksId(boolean isFiltered) {

        int countBooksRecords = 0;

        String query = addAvailableConditions(GET_COUNT_BOOKS_RECORDS, isFiltered);

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                countBooksRecords = resultSet.getInt(1);
            }

        } catch (SQLException e) {
            log.error("SqlException in attempt to get Connection", e);
            return Optional.empty();
        }
        return Optional.of(countBooksRecords);
    }


    private String addAvailableConditions(String query, boolean isFiltered) {
        return isFiltered ? query + " WHERE in_stock > 0" : query;
    }

    private String addConditionLimit(String query) {
        return query + " LIMIT ?,?";
    }

}
