package com.itechart.javalab.library.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Book {

    private int id;
    private String title;
    private LocalDateTime publishDate;
    private String publisher;
    private Set<Author> author;
    private int pageCount;
    private String ISBN;
    private String description;
    private String coverPath;
    private int totalAmount;
    private int inStock;
    private String status;


    public static Book extractForMainPage(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("book.id");
        String title = resultSet.getString("book.title");
        LocalDateTime publishDate = resultSet.getTimestamp("book.publish_date").toLocalDateTime();
        int inStock = resultSet.getInt("book.in_stock");
        Set<Author> authors = new HashSet<>();
        authors.add(Author.buildFrom(resultSet));
        return Book.builder()
                .id(id)
                .title(title)
                .publishDate(publishDate)
                .inStock(inStock)
                .author(authors)
                .build();
    }
}
