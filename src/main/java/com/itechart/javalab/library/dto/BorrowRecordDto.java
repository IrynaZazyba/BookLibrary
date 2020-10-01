package com.itechart.javalab.library.dto;

import com.itechart.javalab.library.model.Book;
import com.itechart.javalab.library.model.BorrowRecord;
import com.itechart.javalab.library.model.Status;
import com.itechart.javalab.library.model.TimePeriod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.text.StringEscapeUtils;

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

    public BorrowRecord toBookAddRecordModel() {
        return BorrowRecord.builder().id(id).status(status).comment(StringEscapeUtils.escapeHtml4(comment))
                .timePeriod(timePeriod)
                .reader(reader.toModel())
                .book(Book.builder().id(bookId).build())
                .build();
    }

    public BorrowRecord toBookEditRecordModel() {
        return BorrowRecord.builder().id(id).status(status).comment(StringEscapeUtils.escapeHtml4(comment))
                .timePeriod(timePeriod)
                .book(Book.builder().id(bookId).build())
                .build();
    }
}
