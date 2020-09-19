package com.itechart.javalab.library.model;

import com.itechart.javalab.library.dto.BookDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.text.StringEscapeUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Book {

    private static final String AVAILABLE_STATUS = "Available (%d out of %d)";
    private static final String UNAVAILABLE_STATUS = "Unavailable (expected to become available on  %1$tB %1$te, %1$tY)";

    private int id;
    private String title;
    private LocalDate publishDate;
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

    public void setAvailableStatus() {
        this.status = String.format(AVAILABLE_STATUS, this.inStock, this.totalAmount);
    }

    public void setUnavailableStatus(LocalDateTime earliestAvailabilityDate) {
        this.status = String.format(Locale.UK, UNAVAILABLE_STATUS, earliestAvailabilityDate);
    }

    public static Book extractForMainPage(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("book.id");
        String title = resultSet.getString("book.title");
        LocalDate publishDate = resultSet.getDate("book.publish_date").toLocalDate();
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
        LocalDate publishDate = resultSet.getDate("book.publish_date").toLocalDate();
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

    public static Book buildFrom(BookDto bookDto) {
        int id = bookDto.getId();
        String title = StringEscapeUtils.escapeHtml4(bookDto.getTitle());
        LocalDate publishDate = LocalDate.parse(bookDto.getPublishDate());
        int pageCount = bookDto.getPageCount();
        String isbn = StringEscapeUtils.escapeHtml4(bookDto.getIsbn());
        String description = StringEscapeUtils.escapeHtml4(bookDto.getDescription());
        int totalAmount = bookDto.getTotalAmount();
        String publisherName = StringEscapeUtils.escapeHtml4(bookDto.getPublisher());
        Set<Author> authors = Author.buildFrom(StringEscapeUtils.escapeHtml4(bookDto.getAuthor()));
        Set<Genre> genres = Genre.buildFrom(StringEscapeUtils.escapeHtml4(bookDto.getGenre()));
        return Book.builder()
                .id(id).title(title).publishDate(publishDate).pageCount(pageCount)
                .ISBN(isbn).description(description).totalAmount(totalAmount)
                .publisher(Publisher.builder().publisherName(publisherName).build())
                .author(authors)
                .genres(genres)
                .build();
    }
}
