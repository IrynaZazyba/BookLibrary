package com.itechart.javalab.library.dto;

import com.itechart.javalab.library.model.Reader;
import lombok.*;

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
    private String email;


    public Reader toModel() {
        validate();
        return Reader.builder().name(name).email(email).build();
    }

    private void validate() throws IllegalArgumentException {
        if (!this.email.matches(EMAIL_PATTERN) || !this.name.matches(NAME_PATTERN)) {
            throw new IllegalArgumentException();
        }
    }

}
