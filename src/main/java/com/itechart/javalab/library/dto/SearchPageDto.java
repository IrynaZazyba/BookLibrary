package com.itechart.javalab.library.dto;

import com.itechart.javalab.library.domain.entity.Book;
import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SearchPageDto {

    private Boolean isAvailableOnly;
    private String bookTitle;
    private String bookAuthor;
    private String bookGenre;
    private String bookDescription;
    private List<Book> books;
    private int recordsPerPage;
    private int currentPage;
    private int countPages;

}
