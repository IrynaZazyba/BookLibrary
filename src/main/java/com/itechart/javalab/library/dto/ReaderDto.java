package com.itechart.javalab.library.dto;

import com.itechart.javalab.library.model.Gender;
import com.itechart.javalab.library.model.Reader;
import lombok.*;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReaderDto {

    private static final String NAME_PATTERN = "^[a-zA-Z- ]{2,25}$";
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                    "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private int id;
    private String name;
    private String lastName;
    private String email;
    private String phone;
    private Gender gender;
    private LocalDate registrationDate;

    Reader toModel() {
        validate();
        return Reader.builder().name(name).email(email).build();
    }

    Reader toExtendedModel() {
        validate();
        return Reader.builder().name(name).lastName(lastName).email(email)
                .phone(phone).gender(gender).registrationDate(registrationDate).build();
    }

    private void validate() throws IllegalArgumentException {
        if (!this.email.matches(EMAIL_PATTERN) || !this.name.matches(NAME_PATTERN)) {
            throw new IllegalArgumentException();
        }
    }

}
