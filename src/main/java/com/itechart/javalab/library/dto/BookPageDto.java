package com.itechart.javalab.library.dto;

import com.itechart.javalab.library.model.Book;
import com.itechart.javalab.library.model.BorrowRecord;
import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookPageDto {

    private Book book;
    private List<BorrowRecord> borrowRecord;

}
