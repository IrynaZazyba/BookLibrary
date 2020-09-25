package com.itechart.javalab.library.dao.impl;

import com.itechart.javalab.library.dao.ReceiveReaderDao;
import com.itechart.javalab.library.dao.conn.ConnectionPool;
import com.itechart.javalab.library.dao.exception.DaoRuntimeException;
import com.itechart.javalab.library.model.Paginator;
import com.itechart.javalab.library.model.Reader;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;

@Log4j2
public class SqlReceiveReaderDao implements ReceiveReaderDao {

    private final ConnectionPool connectionPool;
    private static volatile ReceiveReaderDao instance;
    private static final String GET_READERS = "SELECT reader.id, `name`, `lastName`, `email`, `phone`, gender.gender, " +
            "`registrationDate` FROM `reader` INNER JOIN gender on reader.gender_id=gender.id LIMIT ?,?";
    private static final String GET_NUMBER_OF_READER_RECORDS = "SELECT COUNT(id) FROM `reader`";

    private SqlReceiveReaderDao(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public static ReceiveReaderDao getInstance() {
        if (instance == null) {
            synchronized (SqlReceiveReaderDao.class) {
                if (instance == null) {
                    instance = new SqlReceiveReaderDao(ConnectionPool.getInstance());
                }
            }
        }
        return instance;
    }

    @Override
    public Optional<Set<Reader>> getReaders(Paginator paginator) {
        Set<Reader> readers;
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_READERS)) {
            preparedStatement.setInt(1,paginator.getStart());
            preparedStatement.setInt(2,paginator.getRecordsPerPage());
            ResultSet resultSet = preparedStatement.executeQuery();
            readers = Reader.extractForReadersPage(resultSet);
        } catch (SQLException e) {
            log.error("SqlException in getReaders() method", e);
            throw new DaoRuntimeException("SqlException in SqlReceiveReaderDao getReaders() method", e);
        }
        return Optional.of(readers);
    }

    @Override
    public Optional<Integer> getNumberReadersRecords() {
        int numberRecords = 0;
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_NUMBER_OF_READER_RECORDS)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                numberRecords = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            log.error("SqlException in getReaders() method", e);
            throw new DaoRuntimeException("SqlException in SqlReceiveReaderDao getReaders() method", e);
        }
        return Optional.of(numberRecords);
    }
}
