package com.itechart.javalab.library.dto;

import com.itechart.javalab.library.model.Book;
import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MainPageDto {

    private Boolean isAvailableOnly;
    private List<Book> books;
    private int recordsPerPage;
    private int currentPage;
    private int countPages;

}
