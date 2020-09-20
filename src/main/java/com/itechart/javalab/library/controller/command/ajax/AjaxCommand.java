package com.itechart.javalab.library.controller.command.ajax;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public interface AjaxCommand {

    String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

    default String addResponseBodyParameter(String key, Object value) throws JsonProcessingException {
        HashMap<String, Object> parameterMap = new HashMap<>();
        parameterMap.put(key, value);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(parameterMap);
    }
}
