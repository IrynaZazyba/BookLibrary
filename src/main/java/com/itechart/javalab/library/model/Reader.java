package com.itechart.javalab.library.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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

    public static Set<Reader> extractForReadersPage(ResultSet resultSet) throws SQLException {
        Set<Reader> readers = new HashSet<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("reader.id");
            String firstName = resultSet.getString("reader.name");
            String lastName = resultSet.getString("reader.lastName");
            String email = resultSet.getString("reader.email");
            String phone = resultSet.getString("reader.phone");
            LocalDate regDate = resultSet.getDate("reader.registrationDate").toLocalDate();
            Gender gender = Gender.valueOf(resultSet.getString("gender.gender"));
            Reader reader = Reader.builder().id(id).name(firstName).lastName(lastName)
                    .email(email).phone(phone).gender(gender).registrationDate(regDate).build();
            readers.add(reader);
        }
        return readers;
    }


}
