package com.itechart.javalab.library.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itechart.javalab.library.model.Book;
import com.itechart.javalab.library.model.BorrowRecord;
import com.itechart.javalab.library.model.Status;
import com.itechart.javalab.library.model.TimePeriod;
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
    private Status status;
    private String comment;
    private int bookId;
    private ReaderDto reader;
    private TimePeriod timePeriod;

    public BorrowRecord toAddModel() {
        return BorrowRecord.builder().id(id).status(status).comment(comment)
                .timePeriod(timePeriod)
                .reader(reader.toModel())
                .book(Book.builder().id(bookId).build())
                .build();
    }

    public BorrowRecord toEditModel() {
        return BorrowRecord.builder().id(id).status(status).comment(comment)
                .timePeriod(timePeriod)
                .book(Book.builder().id(bookId).build())
                .build();
    }

    public static BorrowRecordDto[] parseBorrowRecords(String jsonRecords) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonRecords, BorrowRecordDto[].class);
    }
}
