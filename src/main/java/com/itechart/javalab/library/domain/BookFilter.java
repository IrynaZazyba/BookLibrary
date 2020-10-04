package com.itechart.javalab.library.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The class {@code BookFilter} allows
 * realizing books filtration
 */
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

