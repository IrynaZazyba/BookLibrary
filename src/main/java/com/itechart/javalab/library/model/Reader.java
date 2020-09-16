package com.itechart.javalab.library.model;

import com.itechart.javalab.library.dto.BorrowRecordDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Reader {

    private int id;
    private String name;
    private String email;


    public static Reader buildFrom(ResultSet resultSet) throws SQLException {
        int readerId = resultSet.getInt("reader.id");
        String readerName = resultSet.getString("reader.name");
        String readerEmail = resultSet.getString("reader.email");
        return Reader.builder().id(readerId).name(readerName).email(readerEmail).build();
    }

    public static Reader buildFrom(BorrowRecordDto borrowRecordDto) {
        return Reader.builder()
                .email(borrowRecordDto.getReader().getEmail())
                .name(borrowRecordDto.getReader().getName())
                .build();
    }
}
