package com.itechart.javalab.library.dao.impl;

import com.itechart.javalab.library.dao.LibraryInfoDao;
import com.itechart.javalab.library.dao.connection.ConnectionPool;
import com.itechart.javalab.library.dao.exception.DaoRuntimeException;
import com.itechart.javalab.library.domain.entity.LibraryEmailInfo;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.util.Strings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Log4j2
public class SqlLibraryInfoDao implements LibraryInfoDao {

    private final ConnectionPool connectionPool;
    private static volatile LibraryInfoDao instance;
    private static final String GET_LIBRARY_INFO = "SELECT `id`,`address`, `name`, `signature` FROM `email_template`";
    private static final String INSERT_LIBRARY_INFO = "INSERT INTO `email_template`(`address`, `name`, " +
            "`signature`) VALUES (?,?,?)";
    private static final String UPDATE_LIBRARY_INFO = "UPDATE `email_template` SET `address`=?,`name`=?, " +
            "`signature`=? WHERE id=?";

    private SqlLibraryInfoDao(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public static LibraryInfoDao getInstance() {
        if (instance == null) {
            synchronized (LibraryInfoDao.class) {
                if (instance == null) {
                    instance = new SqlLibraryInfoDao(ConnectionPool.getInstance());
                }
            }
        }
        return instance;
    }

    @Override
    public Optional<LibraryEmailInfo> getLibraryInfo() {
        LibraryEmailInfo info = null;
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(GET_LIBRARY_INFO)) {
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                info = LibraryEmailInfo.buildFrom(resultSet);
            }
        } catch (SQLException e) {
            log.error("SqlException in SqlLibraryInfoDao getLibraryInfo() method", e);
            throw new DaoRuntimeException("SqlException in SqlLibraryInfoDao getLibraryInfo() method", e);
        }
        return Optional.ofNullable(info);
    }

    @Override
    public void addLibraryInfo(LibraryEmailInfo info) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT_LIBRARY_INFO)) {
            ps.setString(1, info.getAddress());
            ps.setString(2, info.getName());
            ps.setString(3, info.getSignature());
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("SqlException in SqlLibraryInfoDao addLibraryInfo() method", e);
            throw new DaoRuntimeException("SqlException in SqlLibraryInfoDao addLibraryInfo() method", e);
        }
    }

    @Override
    public void updateLibraryInfo(LibraryEmailInfo info) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(UPDATE_LIBRARY_INFO)) {
            ps.setString(1, info.getAddress());
            ps.setString(2, info.getName());
            ps.setString(3, Strings.isEmpty(info.getSignature())?null:info.getSignature());
            ps.setInt(4, info.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("SqlException in SqlLibraryInfoDao updateLibraryInfo() method", e);
            throw new DaoRuntimeException("SqlException in SqlLibraryInfoDao updateLibraryInfo() method", e);
        }
    }
}
