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
public class Genre {

    private int id;
    private String genre;

    public static Genre buildFrom(ResultSet resultSet) throws SQLException {
        int genreId = resultSet.getInt("genre.id");
        String genre = resultSet.getString("genre.genre");
        return Genre.builder().id(genreId).genre(genre).build();
    }

}
