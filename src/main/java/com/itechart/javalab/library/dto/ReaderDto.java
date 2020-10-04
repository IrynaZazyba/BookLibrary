package com.itechart.javalab.library.dto;

import com.itechart.javalab.library.domain.entity.Gender;
import com.itechart.javalab.library.domain.entity.Reader;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

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
    private static final String PHONE_PATTERN = "(^[+][3][7][5][0-9]{9}$)|(^[8][0-9]{10}$)";
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

    public Reader toExtendedModel() {
        validateExtendedModel();
        return Reader.builder().id(id).name(name.trim()).lastName(lastName.trim()).email(email.trim())
                .phone(phone).gender(gender).build();
    }

    private void validate() throws IllegalArgumentException {
        if (!this.email.matches(EMAIL_PATTERN) || !this.name.matches(NAME_PATTERN)) {
            throw new IllegalArgumentException("Invalid reader data");
        }
    }

    private void validateExtendedModel() throws IllegalArgumentException {
        validate();
        if (!this.lastName.matches(NAME_PATTERN)) {
            throw new IllegalArgumentException("Invalid reader data");
        }

        if (StringUtils.isNotEmpty(this.phone)) {
            if (!this.phone.matches(PHONE_PATTERN)) {
                throw new IllegalArgumentException("Invalid reader data");
            }
        }
    }

}
