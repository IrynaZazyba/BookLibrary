package com.itechart.javalab.library.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public static Set<Genre> buildFrom(String genres) {
        List<String> genresToList = Arrays.asList(genres.split(","));
        Set<Genre> bookGenres = new HashSet<>();
        genresToList.forEach(genre -> bookGenres.add(Genre.builder()
                .genre(genre.trim())
                .build()));
        return bookGenres;
    }

}
