package com.itechart.javalab.library.dao.impl;

import com.itechart.javalab.library.dao.BookDao;
import com.itechart.javalab.library.dao.conn.ConnectionPool;
import com.itechart.javalab.library.model.Author;
import com.itechart.javalab.library.model.Book;
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
    public Optional<List<Book>> getBooks() {

        List<Book> books;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_BOOKS)) {

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




}
