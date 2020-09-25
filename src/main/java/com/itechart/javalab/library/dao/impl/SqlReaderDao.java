package com.itechart.javalab.library.dao.impl;

import com.itechart.javalab.library.dao.ReaderDao;
import com.itechart.javalab.library.dao.conn.ConnectionPool;
import com.itechart.javalab.library.dao.exception.DaoRuntimeException;
import com.itechart.javalab.library.model.BorrowRecord;
import com.itechart.javalab.library.model.Reader;
import com.itechart.javalab.library.model.Status;
import lombok.extern.log4j.Log4j2;

import java.sql.*;
import java.util.*;

@Log4j2
public class SqlReaderDao implements ReaderDao {

    private final ConnectionPool connectionPool;
    private static volatile ReaderDao instance;

    private static final String GET_BOOK_READERS = "SELECT borrow_list.id, borrow_date, due_date, return_date, " +
            "reader.id, reader.name, reader.email, status,comment  FROM borrow_list " +
            "INNER JOIN reader on reader.id=borrow_list.reader_id " +
            "LEFT JOIN status on status.id=borrow_list.status_id " +
            "WHERE borrow_list.book_id=?";

    private static final String UPDATE_BORROW_LIST = "UPDATE borrow_list SET return_date=?, comment=?, " +
            "status_id=(SELECT id FROM status WHERE status=?) WHERE id=? " +
            "AND status_id IS NULL AND book_id=?";

    private static final String REDUCE_BOOK_TOTAL_AMOUNT_ON_VALUE = "UPDATE book SET total_amount=total_amount-?, " +
            "in_stock=in_stock+? WHERE id=?";

    private static final String REDUCE_IN_STOCK_BOOK_VALUE_ON_NUMBER = "UPDATE book SET in_stock=in_stock-? " +
            "WHERE in_stock>0 and id=? and total_amount>=in_stock-?;";
    private static final String INSERT_READER = "INSERT INTO reader(`name`, email) VALUES (?,?)";
    private static final String UPDATE_READER_NAME = "UPDATE reader SET name=? WHERE email=? and name!=?";
    private static final String ADD_BORROW_RECORD = "INSERT INTO borrow_list(borrow_date, due_date,comment, book_id, " +
            "reader_id) VALUES (?,?,?,?,(SELECT id FROM reader WHERE email=?))";

    private static final String GET_READER_BY_EMAIL = "SELECT id FROM reader WHERE email=?";
    private static final String GET_BORROW_STATUS_BY_ID = "SELECT status  FROM `borrow_list` " +
            "LEFT JOIN status on borrow_list.status_id=status.id WHERE borrow_list.id=?";

    private static final String UPDATE_BORROW_RECORD_STATUS = "UPDATE `borrow_list` SET " +
            "`status_id`=(SELECT id FROM status WHERE status=?), comment=? WHERE id=?";

    private static final String UPDATE_BOOK_STOCK_TOTAL = "UPDATE `book` SET total_amount=total_amount+?, " +
            "in_stock=in_stock+? WHERE id=?";

    private static final String GET_READERS = "SELECT id, email, name FROM `reader` WHERE email LIKE ?";

    private static final String GET_INFO_TO_RETURN_NOTIFICATION = "SELECT borrow_list.id,reader.name,reader.email, " +
            "reader.id, book.title,isbn, due_date FROM `borrow_list` " +
            "INNER JOIN reader on reader.id=borrow_list.reader_id " +
            "INNER JOIN book on book.id=borrow_list.book_id " +
            "WHERE DATEDIFF(due_date, NOW())=8";

    private static final String GET_DELAY_NOTIFICATION_INFO = "SELECT borrow_list.id, due_date,reader.name, " +
            "reader.email, reader.id, book.title,isbn, borrow_date FROM `borrow_list` " +
            "INNER JOIN reader on reader.id=borrow_list.reader_id " +
            "INNER JOIN book on book.id=borrow_list.book_id " +
            "WHERE DATEDIFF(due_date, NOW())<0 and return_date is NULL";

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
            try {
                connection.setAutoCommit(false);
                for (BorrowRecord record : borrowRecord) {
                    if (updateBorrowStatus(connection, record) == 0) {
                        result = false;
                        continue;
                    }
                    int countDamaged = 0;
                    if (record.getStatus() != Status.RETURNED) {
                        countDamaged++;
                    }
                    updateBookStock(connection, record, countDamaged);
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


    private void updateBookStock(Connection connection, BorrowRecord record, int countDamaged) throws SQLException {
        try (PreparedStatement psBookTable = connection.prepareStatement(REDUCE_BOOK_TOTAL_AMOUNT_ON_VALUE)) {
            psBookTable.setInt(1, countDamaged);
            psBookTable.setInt(2, 1 - countDamaged);
            psBookTable.setInt(3, record.getBook().getId());
            psBookTable.executeUpdate();
        }
    }

    private int updateBorrowStatus(Connection connection, BorrowRecord record) throws SQLException {
        try (PreparedStatement psBorrowListTable = connection.prepareStatement(UPDATE_BORROW_LIST)) {
            psBorrowListTable.setTimestamp(1, Timestamp.valueOf(record.getReturnDate()));
            psBorrowListTable.setString(2, record.getComment());
            psBorrowListTable.setString(3, record.getStatus().toString());
            psBorrowListTable.setInt(4, record.getId());
            psBorrowListTable.setInt(5, record.getBook().getId());
            return psBorrowListTable.executeUpdate();
        }
    }

    @Override
    public boolean createBorrowRecord(List<BorrowRecord> borrowRecords) {
        boolean result = true;
        try (Connection connection = connectionPool.getConnection()) {
            try {
                connection.setAutoCommit(false);
                for (BorrowRecord record : borrowRecords) {
                    if (reduceBookInStock(connection, record) == 0) {
                        result = false;
                        continue;
                    }
                    Reader reader = record.getReader();
                    if (!getReaderByEmail(reader.getEmail())) {
                        insertReader(connection, reader);
                    } else {
                        updateReaderName(connection, reader);
                    }
                    addBorrowRecord(connection, record);
                }
                connection.commit();
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                connection.rollback();
                connection.setAutoCommit(true);
                log.error("SqlException in createBorrowRecord() method", e);
                throw new DaoRuntimeException("SqlException in SqlBookDao createBorrowRecord() method", e);
            }
        } catch (SQLException e) {
            log.error("SqlException in attempt to get Connection", e);
            throw new DaoRuntimeException("SqlException in SqlReaderDao createBorrowRecord() method", e);
        }
        return result;
    }

    @Override
    public boolean updateStatusBorrowRecords(List<BorrowRecord> borrowRecords) {
        boolean result = true;
        try (Connection connection = connectionPool.getConnection()) {
            try {
                int totalAmount = 0;
                int inStock = 0;
                for (BorrowRecord record : borrowRecords) {
                    connection.setAutoCommit(false);
                    Optional<Status> borrowRecordStatus = getBorrowRecordStatus(record.getId());
                    if (borrowRecordStatus.isEmpty()) {
                        result = false;
                        continue;
                    }
                    Status status = borrowRecordStatus.get();
                    if (record.getStatus() == Status.RETURNED && Status.RETURNED != status) {
                        totalAmount++;
                        inStock++;
                        updateBorrowRecordStatus(connection, record);
                    }
                    if (record.getStatus() != Status.RETURNED && Status.RETURNED == status) {
                        totalAmount--;
                        inStock--;
                        updateBorrowRecordStatus(connection, record);

                    }
                    updateBookOnValue(connection, inStock, totalAmount, record.getBook().getId());
                    connection.commit();
                    connection.setAutoCommit(true);
                    totalAmount = 0;
                    inStock = 0;
                }
            } catch (SQLException e) {
                connection.rollback();
                connection.setAutoCommit(true);
                log.error("SqlException in updateStatusBorrowRecords() method", e);
                throw new DaoRuntimeException("SqlException in SqlBookDao updateStatusBorrowRecords() method", e);
            }
        } catch (SQLException e) {
            log.error("SqlException in attempt to get Connection", e);
            throw new DaoRuntimeException("SqlException in SqlReaderDao updateStatusBorrowRecords() method", e);
        }
        return result;
    }

    @Override
    public Optional<Set<Reader>> getReadersByEmail(String email) {
        Set<Reader> readers = new HashSet<>();
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_READERS)) {
            preparedStatement.setString(1, "%" + email + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Reader reader = Reader.buildFrom(resultSet);
                readers.add(reader);
            }
        } catch (SQLException e) {
            log.error("SqlException in getReadersByEmail() method", e);
            throw new DaoRuntimeException("SqlException in SqlReaderDao getReadersByEmail() method", e);
        }
        return Optional.of(readers);
    }

    @Override
    public List<BorrowRecord> getReturnNotificationInfo() {
        List<BorrowRecord> borrowRecords = new ArrayList<>();
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_INFO_TO_RETURN_NOTIFICATION)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                BorrowRecord record = BorrowRecord.extractForNotification(resultSet);
                borrowRecords.add(record);
            }
        } catch (SQLException e) {
            log.error("SqlException in getReturnNotificationInfo() method", e);
            throw new DaoRuntimeException("SqlException in SqlReaderDao getReturnNotificationInfo() method", e);
        }
        return borrowRecords;
    }

    @Override
    public List<BorrowRecord> getDelayNotificationInfo() {
        List<BorrowRecord> borrowRecords = new ArrayList<>();
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_DELAY_NOTIFICATION_INFO)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                BorrowRecord record = BorrowRecord.extractForNotification(resultSet);
                borrowRecords.add(record);
            }
        } catch (SQLException e) {
            log.error("SqlException in getDelayNotificationInfo() method", e);
            throw new DaoRuntimeException("SqlException in SqlReaderDao getDelayNotificationInfo() method", e);
        }
        return borrowRecords;
    }

    private void updateBookOnValue(Connection conn, int inStockCountToUpdate, int totalAmountCountToUpdate, int bookId)
            throws SQLException {
        try (PreparedStatement psUpdateBook = conn.prepareStatement(UPDATE_BOOK_STOCK_TOTAL)) {
            psUpdateBook.setInt(1, totalAmountCountToUpdate);
            psUpdateBook.setInt(2, inStockCountToUpdate);
            psUpdateBook.setInt(3, bookId);
            psUpdateBook.executeUpdate();
        }
    }

    private void updateBorrowRecordStatus(Connection connection, BorrowRecord record) throws SQLException {
        try (PreparedStatement psUpdateStatus = connection.prepareStatement(UPDATE_BORROW_RECORD_STATUS)) {
            psUpdateStatus.setString(1, record.getStatus().toString());
            psUpdateStatus.setString(2, record.getComment());
            psUpdateStatus.setInt(3, record.getId());
            psUpdateStatus.executeUpdate();
        }
    }

    private Optional<Status> getBorrowRecordStatus(int borrowRecordId) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement psGetStatus = connection.prepareStatement(GET_BORROW_STATUS_BY_ID)) {
            psGetStatus.setInt(1, borrowRecordId);
            ResultSet rs = psGetStatus.executeQuery();
            if (rs.next()) {
                Status status = Status.valueOf(rs.getString("status"));
                return Optional.of(status);
            }
        } catch (SQLException e) {
            log.error("SqlException in getBorrowRecordStatus method", e);
            throw new DaoRuntimeException("SqlException in SqlReaderDao getBorrowRecordStatus() method", e);
        }
        return Optional.empty();
    }

    private boolean getReaderByEmail(String email) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_READER_BY_EMAIL)) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            log.error("SqlException in getReaderByEmail method", e);
            throw new DaoRuntimeException("SqlException in SqlReaderDao getReaderByEmail() method", e);
        }
        return false;
    }

    private int reduceBookInStock(Connection connection, BorrowRecord record) throws SQLException {
        try (PreparedStatement psBookTable = connection.prepareStatement(REDUCE_IN_STOCK_BOOK_VALUE_ON_NUMBER)) {
            psBookTable.setInt(1, 1);
            psBookTable.setInt(2, record.getBook().getId());
            psBookTable.setInt(3, 1);
            return psBookTable.executeUpdate();
        }
    }

    private void insertReader(Connection connection, Reader reader) throws SQLException {
        try (PreparedStatement psReaderAdd = connection.prepareStatement(INSERT_READER)) {
            psReaderAdd.setString(1, reader.getName());
            psReaderAdd.setString(2, reader.getEmail());
            psReaderAdd.executeUpdate();
        }
    }

    private void updateReaderName(Connection connection, Reader reader) throws SQLException {
        try (PreparedStatement psReaderUpdate = connection.prepareStatement(UPDATE_READER_NAME)) {
            psReaderUpdate.setString(1, reader.getName());
            psReaderUpdate.setString(2, reader.getEmail());
            psReaderUpdate.setString(3, reader.getName());
            psReaderUpdate.executeUpdate();
        }
    }

    private void addBorrowRecord(Connection connection, BorrowRecord record) throws SQLException {
        try (PreparedStatement psBorrowRecordTable = connection.prepareStatement(ADD_BORROW_RECORD)) {
            psBorrowRecordTable.setTimestamp(1, Timestamp.valueOf(record.getBorrowDate()));
            psBorrowRecordTable.setTimestamp(2, Timestamp.valueOf(record.getDueDate()));
            psBorrowRecordTable.setString(3, record.getComment());
            psBorrowRecordTable.setInt(4, record.getBook().getId());
            psBorrowRecordTable.setString(5, record.getReader().getEmail());
            psBorrowRecordTable.executeUpdate();
        }
    }
}
