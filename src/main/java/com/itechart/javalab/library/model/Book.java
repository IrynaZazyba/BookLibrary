package com.itechart.javalab.library.model;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Book {

    private int id;
    private String title;
    private LocalDateTime publishDate;
    private String publisher;
    private int pageCount;
    private String ISBN;
    private String description;
    private String coverPath;
    private int totalAmount;
    private int inStock;

}
