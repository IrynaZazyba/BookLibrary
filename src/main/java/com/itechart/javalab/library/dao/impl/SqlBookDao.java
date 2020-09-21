package com.itechart.javalab.library.dao.impl;

import com.itechart.javalab.library.dao.BookDao;
import com.itechart.javalab.library.dao.conn.ConnectionPool;
import com.itechart.javalab.library.dao.exception.DaoRuntimeException;
import com.itechart.javalab.library.model.*;
import lombok.extern.log4j.Log4j2;

import java.sql.Date;
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
            "book.total_amount, book.in_stock, genre.id, genre.genre,cover FROM book " +
            "INNER JOIN book_has_author ON book_has_author.book_id=book.id " +
            "INNER JOIN author ON book_has_author.author_id=author.id " +
            "INNER JOIN genre_has_book ON genre_has_book.book_id=book.id " +
            "INNER JOIN genre ON genre.id=genre_has_book.genre_id " +
            "INNER JOIN publisher ON publisher.id=book.publisher_id " +
            "WHERE book.id=?";

    private final static String GET_EARLIEST_DUE_DATE_BY_BOOK_ID = "SELECT MIN(due_date) FROM borrow_list " +
            "WHERE book_id=? and return_date is null";

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

    private static final String DELETE_BOOK = "DELETE FROM `book` WHERE id=? AND  total_amount=in_stock;";

    private static final String INSERT_BOOK = "INSERT INTO `book`( `title`, `publish_date`, `page_count`, `ISBN`, " +
            "`description`, `total_amount`, `cover`, `in_stock`, `publisher_id`) " +
            "VALUES (?,?,?,?,?,?,?,?,?)";

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

    @Override
    public Optional<Boolean> updateBookInfo(Book book) {
        try (Connection connection = connectionPool.getConnection()) {
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
                log.error("SqlException in updateBookInfo() method", e);
                throw new DaoRuntimeException("SqlException in SqlBookDao updateBookInfo() method", e);
            }
        } catch (SQLException e) {
            log.error("SqlException in attempt to get Connection", e);
            throw new DaoRuntimeException("SqlException in SqlBookDao updateBookInfo() method", e);
        }
    }

    @Override
    public Optional<Integer> getAuthorByName(String name) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_AUTHOR_BY_NAME)) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(resultSet.getInt("id"));
            }
        } catch (SQLException e) {
            log.error("SqlException in attempt to get Connection", e);
            throw new DaoRuntimeException("SqlException in SqlBookDao getEarliestDueDate() method", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Integer> getGenreByName(String genre) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_GENRE)) {
            preparedStatement.setString(1, genre);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(resultSet.getInt("id"));
            }
        } catch (SQLException e) {
            log.error("SqlException in attempt to get Connection", e);
            throw new DaoRuntimeException("SqlException in SqlBookDao getGenreByName() method", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Integer> getPublisherByName(String name) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_PUBLISHER)) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(resultSet.getInt("id"));
            }
        } catch (SQLException e) {
            log.error("SqlException in attempt to get Connection", e);
            throw new DaoRuntimeException("SqlException in SqlBookDao getPublisherByName() method", e);
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteBooks(int[] bookId) {
        boolean result = true;
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BOOK)) {
                connection.setAutoCommit(false);
                for (int id : bookId) {
                    deleteBookGenre(connection, id);
                    deleteBookAuthors(connection, id);
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
                log.error("SqlException in deleteBooks() method", e);
                throw new DaoRuntimeException("SqlException in SqlBookDao deleteBooks() method", e);
            }
        } catch (SQLException e) {
            log.error("SqlException in attempt to get Connection", e);
            throw new DaoRuntimeException("SqlException in SqlBookDao deleteBooks() method", e);
        }
        return result;
    }

    @Override
    public void createBook(Book book) {
        try (Connection connection = connectionPool.getConnection()) {
            try {
                connection.setAutoCommit(false);
                linkPublisherWithBook(connection, book);
                int id = insertBook(connection, book);
                book.setId(id);
                linkGenresWithBook(connection, book.getGenres(), book.getId());
                linkAuthorsWithBook(connection, book.getAuthor(), book.getId());
                connection.commit();
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                connection.rollback();
                connection.setAutoCommit(true);
                log.error("SqlException in createBook() method", e);
                throw new DaoRuntimeException("SqlException in SqlBookDao createBook() method", e);
            }
        } catch (SQLException e) {
            log.error("SqlException in attempt to get Connection", e);
            throw new DaoRuntimeException("SqlException in SqlBookDao createBook() method", e);
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
            ps.setString(7, book.getCoverPath());
            ps.setInt(8, book.getTotalAmount());
            ps.setInt(9, book.getPublisher().getId());
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                id = generatedKeys.getInt(1);
            }
            return id;
        }
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



