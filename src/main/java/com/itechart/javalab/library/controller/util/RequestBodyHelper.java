package com.itechart.javalab.library.controller.util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class RequestBodyHelper {

    private RequestBodyHelper(){}

    public static Part extractPartByName(HttpServletRequest request, String name) throws IOException, ServletException {
        Part file = null;
        for (Part part : request.getParts()) {
            if (part.getName().equals(name)) {
                file = part;
            }
        }
        return file;
    }

    public static String getValue(Part part) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                part.getInputStream(), StandardCharsets.UTF_8))) {
            String s;
            while ((s = reader.readLine()) != null) {
                sb.append(s);
            }
        }
        return sb.toString();
    }
}
