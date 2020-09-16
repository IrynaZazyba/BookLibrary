package com.itechart.javalab.library.dao.impl;

import com.itechart.javalab.library.dao.ReaderDao;
import com.itechart.javalab.library.dao.conn.ConnectionPool;
import com.itechart.javalab.library.dao.exception.DaoRuntimeException;
import com.itechart.javalab.library.model.BorrowRecord;
import com.itechart.javalab.library.model.Status;
import lombok.extern.log4j.Log4j2;

import java.sql.*;
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


    private static final String UPDATE_BORROW_LIST = "UPDATE borrow_list SET return_date=?, comment=?, " +
            "status_id=(SELECT id FROM status WHERE status=?) WHERE id=? " +
            "AND status_id IS NULL AND book_id=?";

    private static final String REDUCE_BOOK_TOTAL_AMOUNT_ON_VALUE = "UPDATE book SET total_amount=total_amount-?, " +
            "in_stock=in_stock+? WHERE id=?";

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

    @Override
    public boolean setBorrowRecordStatus(List<BorrowRecord> borrowRecord) {
        boolean result = true;
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement psBorrowListTable = connection.prepareStatement(UPDATE_BORROW_LIST);
                 PreparedStatement psBookTable = connection
                         .prepareStatement(REDUCE_BOOK_TOTAL_AMOUNT_ON_VALUE)) {
                connection.setAutoCommit(false);

                for (BorrowRecord record : borrowRecord) {
                    Status borrowRecordStatus = record.getStatus();
                    psBorrowListTable.setTimestamp(1, Timestamp.valueOf(record.getReturnDate()));
                    psBorrowListTable.setString(2, record.getComment());
                    psBorrowListTable.setString(3, borrowRecordStatus.toString());
                    psBorrowListTable.setInt(4, record.getId());
                    psBorrowListTable.setInt(5, record.getBook().getId());
                    int countUpdatedRecords = psBorrowListTable.executeUpdate();

                    if (countUpdatedRecords == 0) {
                        result = false;
                        continue;
                    }
                    int countDamaged = 0;
                    if (borrowRecordStatus != Status.RETURNED) {
                        countDamaged++;
                    }
                    psBookTable.setInt(1, countDamaged);
                    psBookTable.setInt(2, countUpdatedRecords - countDamaged);
                    psBookTable.setInt(3, record.getBook().getId());
                    connection.commit();
                }

                connection.setAutoCommit(true);
            } catch (SQLException e) {
                connection.rollback();
                connection.setAutoCommit(true);
                log.error("SqlException in setBorrowRecordStatus() method", e);
                throw new DaoRuntimeException("SqlException in SqlBookDao setBorrowRecordStatus() method", e);
            }
        } catch (SQLException e) {
            log.error("SqlException in attempt to get Connection", e);
            throw new DaoRuntimeException("SqlException in SqlReaderDao setBorrowRecordStatus() method", e);
        }
        return result;
    }

}
