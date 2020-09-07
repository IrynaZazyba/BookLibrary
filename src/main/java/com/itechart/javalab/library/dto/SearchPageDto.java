package com.itechart.javalab.library.dto;

import com.itechart.javalab.library.model.Book;
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

}
