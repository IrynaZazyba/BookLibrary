package com.itechart.javalab.library.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookDto {

    private int id;
    private String title;
    private String publishDate;
    private String publisher;
    private String author;
    private String genre;
    private int pageCount;
    private String isbn;
    private String description;
    private String coverPath;
    private int totalAmount;

    public static BookDto fromJson(String record) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.readValue(record, BookDto.class);
    }

}
