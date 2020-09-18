package com.itechart.javalab.library.dto;

import com.itechart.javalab.library.model.Reader;
import lombok.*;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReaderDto {

    private int id;
    @Pattern(regexp = "^[a-zA-Z- ]{2,25}$", message = "Invalid reader name")
    private String name;
    @Email
    private String email;


    public Reader toModel() {
        validate();
        return Reader.builder().name(name).email(email).build();
    }

    private void validate() throws IllegalArgumentException {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        if (validator.validate(this).size() != 0) {
            throw new IllegalArgumentException();
        }
    }

}
