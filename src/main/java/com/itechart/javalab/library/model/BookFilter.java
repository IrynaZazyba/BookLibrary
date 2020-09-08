package com.itechart.javalab.library.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookFilter {

    private boolean isAvailableOnly;
    private String bookTitle;
    private String bookAuthor;
    private String bookGenre;
    private String bookDescription;

}

