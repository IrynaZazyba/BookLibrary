package com.itechart.javalab.library.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookDto {

    private int id;
    private String title;
    private String publishDate;
    private String publisher;
    private String author;
    private String genre;
    private int pageCount;
    private String isbn;
    private String description;
    private String coverPath;
    private int totalAmount;

}
