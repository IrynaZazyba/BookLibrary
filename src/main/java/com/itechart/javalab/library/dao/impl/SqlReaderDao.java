package com.itechart.javalab.library.dao.impl;

import com.itechart.javalab.library.dao.ReaderDao;
import com.itechart.javalab.library.dao.connection.ConnectionPool;
import com.itechart.javalab.library.dao.exception.DaoRuntimeException;
import com.itechart.javalab.library.domain.Paginator;
import com.itechart.javalab.library.domain.entity.Reader;
import lombok.extern.log4j.Log4j2;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Log4j2
public class SqlReaderDao implements ReaderDao {

    private final ConnectionPool connectionPool;
    private static volatile ReaderDao instance;
    private static final String GET_READERS = "SELECT reader.id, `name`, `lastName`, `email`, `phone`, gender.gender, " +
            "`registrationDate` FROM `reader` INNER JOIN gender on reader.gender_id=gender.id LIMIT ?,?";
    private static final String GET_NUMBER_OF_READER_RECORDS = "SELECT COUNT(id) FROM `reader`";
    private static final String GET_READERS_BY_EMAIL = "SELECT id, email, name FROM `reader` WHERE email LIKE ?";
    private static final String UPDATE_READER = "UPDATE `reader` SET `name`=?,`lastName`=?,`email`=?,`phone`=?," +
            "`gender_id`=(SELECT id FROM gender WHERE gender=?) WHERE id=?";
    private static final String GET_READER_BY_EMAIL_EXCEPT_ID = "SELECT id FROM `reader` WHERE email=? and id<>?";
    private static final String CREATE_READER = "INSERT INTO `reader`(`name`, `lastName`, `email`, `phone`, " +
            "`registrationDate`, `gender_id`) VALUES (?,?,?,?,?,(SELECT id FROM gender WHERE gender=?))";

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
    public Optional<Set<Reader>> getReaders(Paginator paginator) {
        Set<Reader> readers;
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_READERS)) {
            preparedStatement.setInt(1, paginator.getStart());
            preparedStatement.setInt(2, paginator.getRecordsPerPage());
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

    @Override
    public Optional<Set<Reader>> getReadersByEmail(String email) {
        Set<Reader> readers = null;
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_READERS_BY_EMAIL)) {
            preparedStatement.setString(1, "%" + email + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                if (readers == null) {
                    readers = new HashSet<>();
                }
                Reader reader = Reader.buildFrom(resultSet);
                readers.add(reader);
            }
        } catch (SQLException e) {
            log.error("SqlException in getReadersByEmail() method", e);
            throw new DaoRuntimeException("SqlException in SqlReaderDao getReadersByEmail() method", e);
        }
        return Optional.ofNullable(readers);
    }

    @Override
    public void updateReader(Reader reader) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_READER)) {
            preparedStatement.setString(1, reader.getName());
            preparedStatement.setString(2, reader.getLastName());
            preparedStatement.setString(3, reader.getEmail());
            preparedStatement.setString(4, reader.getPhone());
            preparedStatement.setString(5, reader.getGender().toString());
            preparedStatement.setInt(6, reader.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("SqlException in updateReader() method", e);
            throw new DaoRuntimeException("SqlException in SqlReaderDao updateReader() method", e);
        }
    }

    @Override
    public void createReader(Reader reader) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CREATE_READER)) {
            preparedStatement.setString(1, reader.getName());
            preparedStatement.setString(2, reader.getLastName());
            preparedStatement.setString(3, reader.getEmail());
            preparedStatement.setString(4, reader.getPhone());
            preparedStatement.setDate(5, Date.valueOf(reader.getRegistrationDate()));
            preparedStatement.setString(6, reader.getGender().toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("SqlException in createReader() method", e);
            throw new DaoRuntimeException("SqlException in SqlReaderDao createReader() method", e);
        }
    }

    @Override
    public Optional<Integer> checkExistsEmail(String email, int readerId) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_READER_BY_EMAIL_EXCEPT_ID)) {
            preparedStatement.setString(1, email);
            preparedStatement.setInt(2, readerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(resultSet.getInt(1));
            }
        } catch (SQLException e) {
            log.error("SqlException in checkExistsEmail() method", e);
            throw new DaoRuntimeException("SqlException in SqlReceiveReaderDao checkExistsEmail() method", e);
        }
        return Optional.empty();
    }

}
