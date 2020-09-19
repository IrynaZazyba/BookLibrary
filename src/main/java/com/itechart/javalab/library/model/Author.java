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

    static Set<Author> buildFrom(String authorNames) {
        List<String> namesToList = Arrays.asList(authorNames.split(","));
        Set<Author> authorSet = new HashSet<>();
        namesToList.forEach(authorName -> authorSet.add(Author.builder()
                .name(authorName.trim())
                .build()));
        return authorSet;
    }

}
