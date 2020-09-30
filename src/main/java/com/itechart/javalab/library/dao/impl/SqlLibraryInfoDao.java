package com.itechart.javalab.library.dao.impl;

import com.itechart.javalab.library.dao.LibraryInfoDao;
import com.itechart.javalab.library.dao.conn.ConnectionPool;
import com.itechart.javalab.library.dao.exception.DaoRuntimeException;
import com.itechart.javalab.library.model.LibraryEmailInfo;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Log4j2
public class SqlLibraryInfoDao implements LibraryInfoDao {

    private final ConnectionPool connectionPool;
    private static volatile LibraryInfoDao instance;
    private static final String GET_LIBRARY_INFO = "SELECT `address`, `name`, `signature` FROM `email_template`";

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
                String address = resultSet.getString("address");
                String name = resultSet.getString("name");
                String signature = resultSet.getString("signature");
                info = LibraryEmailInfo.builder()
                        .address(address).name(name).signature(signature).build();
            }
        } catch (SQLException e) {
            log.error("SqlException in SqlLibraryInfoDao getLibraryInfo() method", e);
            throw new DaoRuntimeException("SqlException in SqlLibraryInfoDao getLibraryInfo() method", e);
        }
        return Optional.ofNullable(info);
    }
}
