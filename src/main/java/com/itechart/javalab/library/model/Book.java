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
    private Publisher publisher;
    private Set<Author> author;
    private Set<Genre> genres;
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

    public static Book extractForBookPage(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("book.id");
        String title = resultSet.getString("book.title");
        LocalDateTime publishDate = resultSet.getTimestamp("book.publish_date").toLocalDateTime();
        int inStock = resultSet.getInt("book.in_stock");
        int pageCount = resultSet.getInt("book.page_count");
        String isbn = resultSet.getString("book.isbn");
        String description = resultSet.getString("book.description");
        int totalAmount = resultSet.getInt("book.total_amount");
        int publisherId = resultSet.getInt("publisher.id");
        String publisherName = resultSet.getString("publisher.publisher");

        return Book.builder()
                .id(id).title(title).publishDate(publishDate).inStock(inStock).pageCount(pageCount)
                .ISBN(isbn).description(description).totalAmount(totalAmount)
                .publisher(new Publisher(publisherId, publisherName)).build();
    }
}
