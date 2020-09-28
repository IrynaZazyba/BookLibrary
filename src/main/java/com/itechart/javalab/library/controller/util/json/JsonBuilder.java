package com.itechart.javalab.library.controller.util.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public interface JsonBuilder {

    ObjectMapper getObjectMapper();

    String getJsonFromMap(Map<String, String> data) throws JsonProcessingException;

    String getJsonFromKeyValue(String key, Object value) throws JsonProcessingException;
}
