package com.itechart.javalab.library.controller.command.ajax;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
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

    default Part extractFile(HttpServletRequest request) throws IOException, ServletException {
        Part file = null;
        for (Part part : request.getParts()) {
            if (part.getName().equals("image_uploads"))
                file = part;
        }
        return file;
    }

    default String defineUploadPath(HttpServletRequest request) {
        String appPath = request.getServletContext().getRealPath("");
        String uploadDirectory = request.getServletContext().getInitParameter("fileUploadPath");
        return appPath + File.separator + uploadDirectory;
    }
}
