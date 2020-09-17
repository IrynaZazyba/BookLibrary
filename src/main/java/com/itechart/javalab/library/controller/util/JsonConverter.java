package com.itechart.javalab.library.controller.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.itechart.javalab.library.dto.BookDto;
import com.itechart.javalab.library.model.BorrowRecord;

import java.util.HashMap;
import java.util.Map;


public class JsonConverter {

    public static BorrowRecord[] fromJsonBorrowRecordArray(String record) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(record, BorrowRecord[].class);
    }

    public static BookDto fromJsonBook(String record) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.readValue(record, BookDto.class);
    }

    public static String addResponseMessage(String message) throws JsonProcessingException {
        Map<String, String> answer = new HashMap<>();
        answer.put("message", message);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(answer);

    }


}
