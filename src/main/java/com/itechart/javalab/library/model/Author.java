package com.itechart.javalab.library.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Author {

    private int id;
    private String name;

    public static Author buildFrom(ResultSet resultSet) throws SQLException {
        int authorId = resultSet.getInt("author.id");
        String authorName = resultSet.getString("author.name");
        return Author.builder()
                .id(authorId)
                .name(authorName)
                .build();
    }

}
