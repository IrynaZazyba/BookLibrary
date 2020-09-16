package com.itechart.javalab.library.dao.impl;

import com.itechart.javalab.library.dao.ReaderDao;
import com.itechart.javalab.library.dao.conn.ConnectionPool;
import com.itechart.javalab.library.dao.exception.DaoRuntimeException;
import com.itechart.javalab.library.model.BorrowRecord;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
public class SqlReaderDao implements ReaderDao {

    private final ConnectionPool connectionPool;
    private static volatile ReaderDao instance;

    private static final String GET_BOOK_READERS = "SELECT borrow_list.id, borrow_date, due_date, return_date, " +
            "reader.id, reader.name, reader.email  FROM borrow_list " +
            "INNER JOIN reader on reader.id=borrow_list.reader_id WHERE borrow_list.book_id=?";

    private SqlReaderDao(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public static ReaderDao getInstance() {
        if (instance == null) {
            synchronized (SqlReaderDao.class) {
                if (instance == null) {
                    instance = new SqlReaderDao(ConnectionPool.getInstance());
                }
            }
        }
        return instance;
    }

    @Override
    public Optional<List<BorrowRecord>> getBorrowRecords(int bookId) {
        List<BorrowRecord> borrowRecords = new ArrayList<>();
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_BOOK_READERS)) {
            preparedStatement.setInt(1, bookId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                borrowRecords.add(BorrowRecord.extractForBookPage(resultSet, bookId));
            }
        } catch (SQLException e) {
            log.error("SqlException in getBorrowRecords() method", e);
            throw new DaoRuntimeException("SqlException in SqlReaderDao getBorrowRecords() method", e);
        }
        return Optional.of(borrowRecords);
    }

}
