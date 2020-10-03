package com.itechart.javalab.library.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The class {@code LibraryEmailInfo} contains info
 * how to contact the library, used to build
 * automatic emails from library
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LibraryEmailInfo {

    private static final String DEFAULT_NAME = "Book Library";
    private static final String DEFAULT_ADDRESS = "Default address";

    private int id;
    private String name;
    private String signature;
    private String address;

    public static LibraryEmailInfo getDefaultValue() {
        return LibraryEmailInfo.builder().name(DEFAULT_NAME).address(DEFAULT_ADDRESS).build();
    }

    public static LibraryEmailInfo buildFrom(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String address = resultSet.getString("address");
        String name = resultSet.getString("name");
        String signature = resultSet.getString("signature");
        return LibraryEmailInfo.builder()
                .id(id).address(address).name(name).signature(signature).build();
    }
}
