package com.itechart.javalab.library.controller.util.json;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Map;

public interface JsonBuilder {

    String getJsonFromMap(Map<String, String> data) throws JsonProcessingException;

    String getJsonFromKeyValue(String key, Object value) throws JsonProcessingException;
}
