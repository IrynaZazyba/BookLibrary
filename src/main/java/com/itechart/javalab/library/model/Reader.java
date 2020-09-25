package com.itechart.javalab.library.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Reader {

    private int id;
    private String name;
    private String lastName;
    private String email;
    private String phone;
    private Gender gender;
    private LocalDate registrationDate;

    public static Reader buildFrom(ResultSet resultSet) throws SQLException {
        int readerId = resultSet.getInt("reader.id");
        String readerName = resultSet.getString("reader.name");
        String readerEmail = resultSet.getString("reader.email");
        return Reader.builder().id(readerId).name(readerName).email(readerEmail).build();
    }

}
