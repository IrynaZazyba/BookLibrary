package com.itechart.javalab.library.dao.impl;

import com.itechart.javalab.library.dao.AlterBookDao;
import com.itechart.javalab.library.dao.connection.ConnectionPool;
import com.itechart.javalab.library.dao.exception.DaoRuntimeException;
import com.itechart.javalab.library.domain.entity.Author;
import com.itechart.javalab.library.domain.entity.Book;
import com.itechart.javalab.library.domain.entity.Genre;
import com.itechart.javalab.library.domain.entity.Publisher;
import lombok.extern.log4j.Log4j2;

import java.sql.*;
import java.util.Optional;
import java.util.Set;

@Log4j2
public class SqlAlterBookDao implements AlterBookDao {

    private final ConnectionPool connectionPool;
    private static volatile SqlAlterBookDao instance;

    private SqlAlterBookDao(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    private final static String GET_AUTHOR_BY_NAME = "SELECT id FROM `author` WHERE name=?";
    private static final String INSERT_AUTHOR = "INSERT INTO `author`(name) VALUES (?)";
    private static final String ADD_AUTHOR_TO_BOOK = "INSERT INTO `book_has_author`(`book_id`, `author_id`) " +
            "VALUES (?,?)";
    private static final String GET_GENRE = "SELECT `id` FROM `genre` WHERE genre=?";
    private static final String INSERT_GENRE = "INSERT INTO `genre`(`genre`) VALUES (?)";
    private static final String ADD_GENRE_TO_BOOK = "INSERT INTO `genre_has_book`(`genre_id`, `book_id`) " +
            "VALUES (?,?)";
    private static final String GET_PUBLISHER = "SELECT `id` FROM `publisher` WHERE publisher=?";
    private static final String ADD_PUBLISHER = "INSERT INTO `publisher`(`publisher`) VALUES (?)";
    private final static String UPDATE_BOOK_INFO = "UPDATE `book` SET title=?, publish_date=?, page_count=?, " +
            "ISBN=?, description=?, total_amount=?,cover=?,in_stock=(?-(SELECT count(id) FROM `borrow_list` " +
            "WHERE book_id=? and return_date is NULL)) WHERE id=? AND ?>=total_amount-in_stock";
    private final static String UPDATE_BOOK_PUBLISHER = "UPDATE `book` SET " +
            "publisher_id=(SELECT id FROM publisher WHERE publisher=?) WHERE id=?";
    private final static String DELETE_BOOK_AUTHORS = "DELETE FROM `book_has_author` WHERE book_id=?";
    private final static String DELETE_BOOK_GENRE = "DELETE FROM `genre_has_book` WHERE book_id=?";
    private static final String DELETE_BOOK = "DELETE FROM `book` WHERE id=? AND  total_amount=in_stock";
    private static final String DELETE_BORROW_RECORDS = "DELETE FROM `borrow_list` WHERE book_id=?";

    private static final String INSERT_BOOK = "INSERT INTO `book`( `title`, `publish_date`, `page_count`, `ISBN`, " +
            "`description`, `total_amount`, `in_stock`, `publisher_id`) " +
            "VALUES (?,?,?,?,?,?,?,?)";
    private static final String UPDATE_BOOK_COVER = "UPDATE `book` SET `cover`=? WHERE id=?";

    public static AlterBookDao getInstance() {
        if (instance == null) {
            synchronized (SqlAlterBookDao.class) {
                if (instance == null) {
                    instance = new SqlAlterBookDao(ConnectionPool.getInstance());
                }
            }
        }
        return instance;
    }

    @Override
    public Optional<Boolean> updateBookInfo(Book book) {
        try (Connection connection = connectionPool.getConnection()) {
            return executeUpdateByTransaction(connection, book);
        } catch (SQLException e) {
            log.error("SqlException in updateBookInfo() method. Check if connection exists or " +
                    "exception thrown by rollback operation in method executeUpdateByTransaction()", e);
            throw new DaoRuntimeException("SqlException in SqlBookDao updateBookInfo() method", e);
        }
    }

    private Optional<Boolean> executeUpdateByTransaction(Connection connection, Book book) throws SQLException {
        try {
            connection.setAutoCommit(false);
            if (updateBook(connection, book) != 0) {
                deleteBookAuthors(connection, book.getId());
                deleteBookGenre(connection, book.getId());
                linkPublisherWithBook(connection, book);
                updateBookPublisher(connection, book.getPublisher(), book.getId());
                linkGenresWithBook(connection, book.getGenres(), book.getId());
                linkAuthorsWithBook(connection, book.getAuthor(), book.getId());
                connection.commit();
                connection.setAutoCommit(true);
                return Optional.of(true);
            }
            connection.commit();
            connection.setAutoCommit(true);
            return Optional.empty();
        } catch (SQLException e) {
            connection.rollback();
            connection.setAutoCommit(true);
            log.error("SqlException in executeUpdateByTransaction() method", e);
            throw new DaoRuntimeException("SqlException in SqlBookDao executeUpdateByTransaction() method", e);
        }
    }

    @Override
    public boolean deleteBooks(int[] bookId) {
        try (Connection connection = connectionPool.getConnection()) {
            return executeDeleteByTransaction(connection, bookId);
        } catch (SQLException e) {
            log.error("SqlException in deleteBooks method. Check if connection exists or " +
                    " exception thrown by rollback operation in method executeDeleteByTransaction()", e);
            throw new DaoRuntimeException("SqlException in SqlBookDao deleteBooks() method", e);
        }
    }

    private boolean executeDeleteByTransaction(Connection connection, int[] bookId) throws SQLException {
        boolean result = true;
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BOOK)) {
            connection.setAutoCommit(false);
            for (int id : bookId) {
                deleteBookGenre(connection, id);
                deleteBookAuthors(connection, id);
                deleteBorrowRecords(connection, id);
                preparedStatement.setInt(1, id);
                if (preparedStatement.executeUpdate() == 0) {
                    result = false;
                    connection.rollback();
                    continue;
                }
                connection.commit();
            }
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            connection.rollback();
            connection.setAutoCommit(true);
            log.error("SqlException in executeDeleteByTransaction() method", e);
            throw new DaoRuntimeException("SqlException in SqlBookDao executeDeleteByTransaction() method", e);
        }
        return result;
    }

    @Override
    public void updateBookCover(Book book) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(UPDATE_BOOK_COVER)) {
            ps.setString(1, book.getCoverPath());
            ps.setInt(2, book.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("SqlException in updateBookCover() method", e);
            throw new DaoRuntimeException("SqlException in SqlBookDao updateBookCover() method", e);
        }
    }

    @Override
    public int createBook(Book book) {
        try (Connection connection = connectionPool.getConnection()) {
            return executeCreateByTransaction(connection, book);
        } catch (SQLException e) {
            log.error("SqlException in createBook() method. Check if connection exists or " +
                    " exception thrown by rollback operation in method executeCreateByTransaction()", e);
            throw new DaoRuntimeException("SqlException in SqlBookDao createBook() method", e);
        }
    }

    private int executeCreateByTransaction(Connection connection, Book book) throws SQLException {
        int id;
        try {
            connection.setAutoCommit(false);
            linkPublisherWithBook(connection, book);
            id = insertBook(connection, book);
            book.setId(id);
            linkGenresWithBook(connection, book.getGenres(), book.getId());
            linkAuthorsWithBook(connection, book.getAuthor(), book.getId());
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            connection.rollback();
            connection.setAutoCommit(true);
            log.error("SqlException in executeCreateByTransaction() method", e);
            throw new DaoRuntimeException("SqlException in SqlBookDao executeCreateByTransaction() method", e);
        }
        return id;
    }

    private int updateBook(Connection connection, Book book) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(UPDATE_BOOK_INFO)) {
            ps.setString(1, book.getTitle());
            ps.setDate(2, Date.valueOf(book.getPublishDate()));
            ps.setInt(3, book.getPageCount());
            ps.setString(4, book.getISBN());
            ps.setString(5, book.getDescription());
            ps.setInt(6, book.getTotalAmount());
            ps.setString(7, book.getCoverPath());
            ps.setInt(8, book.getTotalAmount());
            ps.setInt(9, book.getId());
            ps.setInt(10, book.getId());
            ps.setInt(11, book.getTotalAmount());
            return ps.executeUpdate();
        }
    }

    private void updateBookPublisher(Connection connection, Publisher publisher, int bookId) throws SQLException {
        try (PreparedStatement ps = connection
                .prepareStatement(UPDATE_BOOK_PUBLISHER)) {
            ps.setString(1, publisher.getPublisherName());
            ps.setInt(2, bookId);
            ps.executeUpdate();
        }
    }

    private void addGenreToBook(Connection connection, int genreId, int bookId) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(ADD_GENRE_TO_BOOK)) {
            preparedStatement.setInt(1, genreId);
            preparedStatement.setInt(2, bookId);
            preparedStatement.executeUpdate();
        }
    }

    private void deleteBookGenre(Connection connection, int bookId) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BOOK_GENRE)) {
            preparedStatement.setInt(1, bookId);
            preparedStatement.executeUpdate();
        }
    }

    private void deleteBorrowRecords(Connection connection, int bookId) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BORROW_RECORDS)) {
            preparedStatement.setInt(1, bookId);
            preparedStatement.executeUpdate();
        }
    }

    private void deleteBookAuthors(Connection connection, int bookId) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BOOK_AUTHORS)) {
            preparedStatement.setInt(1, bookId);
            preparedStatement.executeUpdate();
        }
    }

    private void addAuthorToBook(Connection connection, int authorId, int bookId) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(ADD_AUTHOR_TO_BOOK)) {
            ps.setInt(1, bookId);
            ps.setInt(2, authorId);
            ps.executeUpdate();
        }
    }

    private void linkGenresWithBook(Connection connection, Set<Genre> genres, int bookId) throws SQLException {
        for (Genre genre : genres) {
            Optional<Integer> genreByName = getGenreByName(genre.getGenre());
            if (genreByName.isEmpty()) {
                genre.setId(createGenre(connection, genre));
            } else {
                genre.setId(genreByName.get());
            }
            addGenreToBook(connection, genre.getId(), bookId);
        }
    }

    private void linkAuthorsWithBook(Connection connection, Set<Author> authors, int bookId) throws SQLException {
        for (Author author : authors) {
            Optional<Integer> authorByName = getAuthorByName(author.getName());
            if (authorByName.isEmpty()) {
                author.setId(createAuthor(connection, author));
            } else {
                author.setId(authorByName.get());
            }
            addAuthorToBook(connection, author.getId(), bookId);
        }
    }

    private void linkPublisherWithBook(Connection connection, Book book) throws SQLException {
        Publisher publisher = book.getPublisher();
        Optional<Integer> publisherByName = getPublisherByName(book.getPublisher().getPublisherName());
        if (publisherByName.isEmpty()) {
            publisher.setId(createPublisher(connection, publisher));
        } else {
            publisher.setId(publisherByName.get());
        }
    }

    private int createGenre(Connection connection, Genre genre) throws SQLException {
        try (PreparedStatement preparedStatement = connection
                .prepareStatement(INSERT_GENRE, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, genre.getGenre());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }
            return 0;
        }
    }

    private int createPublisher(Connection connection, Publisher publisher) throws SQLException {
        try (PreparedStatement preparedStatement = connection
                .prepareStatement(ADD_PUBLISHER, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, publisher.getPublisherName());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }
            return 0;
        }
    }

    private int createAuthor(Connection connection, Author author) throws SQLException {
        try (PreparedStatement ps = connection
                .prepareStatement(INSERT_AUTHOR, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, author.getName());
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }
            return 0;
        }
    }

    private int insertBook(Connection connection, Book book) throws SQLException {
        int id = 0;
        try (PreparedStatement ps = connection.prepareStatement(INSERT_BOOK, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, book.getTitle());
            ps.setDate(2, Date.valueOf(book.getPublishDate()));
            ps.setInt(3, book.getPageCount());
            ps.setString(4, book.getISBN());
            ps.setString(5, book.getDescription());
            ps.setInt(6, book.getTotalAmount());
            ps.setInt(7, book.getTotalAmount());
            ps.setInt(8, book.getPublisher().getId());
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                id = generatedKeys.getInt(1);
            }
            return id;
        }
    }

    private Optional<Integer> getAuthorByName(String name) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_AUTHOR_BY_NAME)) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(resultSet.getInt("id"));
            }
        } catch (SQLException e) {
            log.error("SqlException in getAuthorByName() method", e);
            throw new DaoRuntimeException("SqlException in SqlBookDao getAuthorByName() method", e);
        }
        return Optional.empty();
    }

    private Optional<Integer> getGenreByName(String genre) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_GENRE)) {
            preparedStatement.setString(1, genre);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(resultSet.getInt("id"));
            }
        } catch (SQLException e) {
            log.error("SqlException in getGenreByName() method", e);
            throw new DaoRuntimeException("SqlException in SqlBookDao getGenreByName() method", e);
        }
        return Optional.empty();
    }

    private Optional<Integer> getPublisherByName(String name) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_PUBLISHER)) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(resultSet.getInt("id"));
            }
        } catch (SQLException e) {
            log.error("SqlException in getPublisherByName() method", e);
            throw new DaoRuntimeException("SqlException in SqlBookDao getPublisherByName() method", e);
        }
        return Optional.empty();
    }
}
