package com.itechart.javalab.library.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LibraryEmailInfo {

    private static final String DEFAULT_NAME = "Book Library";
    private static final String DEFAULT_ADDRESS = "Default address";

    private int id;
    private String name;
    private String signature;
    private String address;

    public static LibraryEmailInfo getDefaultValue() {
        return LibraryEmailInfo.builder().name(DEFAULT_NAME).address(DEFAULT_ADDRESS).build();
    }
}
