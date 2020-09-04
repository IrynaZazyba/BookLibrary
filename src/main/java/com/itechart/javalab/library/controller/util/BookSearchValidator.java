package com.itechart.javalab.library.controller.util;

public class BookSearchValidator {

    private BookSearchValidator() {
    }

    public static boolean isAtLeastOneParameterNull(String title, String author, String genre, String description) {
        return title == null || author == null || genre == null || description == null;
    }

    public static boolean isAllParametersEmpty(String title, String author, String genre, String description) {
        return title.equals("") && author.equals("") && genre.equals("") && description.equals("");
    }

}
