package com.itechart.javalab.library.controller.util;

import com.itechart.javalab.library.model.Reader;

public class ReaderValidator {

    private static final String NAME_PATTERN = "^[a-zA-Z- ]{2,25}$";
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                    "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private ReaderValidator() {
    }

    public static boolean validate(Reader reader) {
        return validateEmail(reader.getEmail()) && validateName(reader.getName());
    }

    private static boolean validateEmail(String email) {
        return email.matches(EMAIL_PATTERN);
    }

    private static boolean validateName(String name) {
        return name.matches(NAME_PATTERN);
    }


}
