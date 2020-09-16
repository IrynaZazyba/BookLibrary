package com.itechart.javalab.library.dto;

import com.itechart.javalab.library.model.Book;
import com.itechart.javalab.library.model.Reader;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BorrowRecordDto {

    private int id;
    private String borrowDate;
    private String dueDate;
    private String status;
    private String comment;
    private Book book;
    private Reader reader;
    private String timePeriod;


}
