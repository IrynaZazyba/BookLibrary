package com.itechart.javalab.library.controller.util.json.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itechart.javalab.library.controller.util.json.JsonBuilder;

import java.util.HashMap;
import java.util.Map;

public class JacksonJsonBuilder implements JsonBuilder {

    private final ObjectMapper objectMapper;
    private final Map<String, Object> parameterMap = new HashMap<>();
    private static volatile JacksonJsonBuilder instance;

    private JacksonJsonBuilder() {
        this.objectMapper = new ObjectMapper();
    }

    public static JsonBuilder getInstance() {
        if (instance == null) {
            synchronized (JacksonJsonBuilder.class) {
                if (instance == null) {
                    instance = new JacksonJsonBuilder();
                }
            }
        }
        return instance;
    }

    @Override
    public String getJsonFromMap(Map<String, String> data) throws JsonProcessingException {
        return objectMapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(data);
    }

    @Override
    public String getJsonFromKeyValue(String key, Object value) throws JsonProcessingException {
        parameterMap.put(key, value);
        String json = objectMapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(parameterMap);
        parameterMap.clear();
        return json;
    }


}
