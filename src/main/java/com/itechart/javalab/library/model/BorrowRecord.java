package com.itechart.javalab.library.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BorrowRecord {

    private int id;
    private LocalDateTime borrowDate;
    private LocalDateTime dueDate;
    private LocalDateTime returnDate;
    private String comment;
    private Book book;
    private Reader reader;
    private Status status;
    private TimePeriod timePeriod;

    public static BorrowRecord extractForBookPage(ResultSet resultSet, int bookId) throws SQLException {
        int borrowId = resultSet.getInt("borrow_list.id");
        String comment = resultSet.getString("borrow_list.comment");
        LocalDateTime borrowDate = resultSet.getTimestamp("borrow_list.borrow_date").toLocalDateTime();
        LocalDateTime dueDate = resultSet.getTimestamp("borrow_list.due_date").toLocalDateTime();
        Timestamp timestampReturnDate = resultSet.getTimestamp("borrow_list.return_date");
        LocalDateTime returnDate = null;
        Status status = null;
        if (timestampReturnDate != null) {
            returnDate = timestampReturnDate.toLocalDateTime();
            status = Status.valueOf(resultSet.getString("status.status"));
        }
        Reader reader = Reader.buildFrom(resultSet);
        Book book = Book.builder().id(bookId).build();
        return BorrowRecord.builder().id(borrowId).borrowDate(borrowDate).dueDate(dueDate)
                .returnDate(returnDate).book(book).reader(reader).status(status).comment(comment).build();
    }

    public static BorrowRecord extractForNotification(ResultSet resultSet) throws SQLException {
        int borrowId = resultSet.getInt("borrow_list.id");
        LocalDateTime dueDate = resultSet.getTimestamp("borrow_list.due_date").toLocalDateTime();
        Reader reader = Reader.buildFrom(resultSet);
        Book book = Book.extractForNotification(resultSet);
        return BorrowRecord.builder().id(borrowId).dueDate(dueDate).book(book).reader(reader).build();
    }

}
