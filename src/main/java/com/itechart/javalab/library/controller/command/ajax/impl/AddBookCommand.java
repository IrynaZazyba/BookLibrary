package com.itechart.javalab.library.controller.command.ajax.impl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itechart.javalab.library.controller.command.ajax.AjaxCommand;
import com.itechart.javalab.library.dto.BookDto;
import com.itechart.javalab.library.service.BookService;
import com.itechart.javalab.library.service.impl.DefaultBookService;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.itechart.javalab.library.controller.util.ResponseParameterName.RESPONSE_PARAMETER_ERROR;
import static com.itechart.javalab.library.controller.util.ResponseParameterName.RESPONSE_PARAMETER_SUCCESS;

@Log4j2
public class AddBookCommand implements AjaxCommand {

    private final BookService bookService;
    private static final String REQUEST_BOOK_PARAMETER = "bookDto";
    private static final String RESPONSE_MESSAGE_OK = "ok";

    public AddBookCommand() {
        this.bookService = DefaultBookService.getInstance();
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String savePath = defineUploadPath(request);
        Part file = extractFile(request);
        String responseBody;
        try {
            BookDto book = BookDto.fromJson(request.getParameter(REQUEST_BOOK_PARAMETER));
            int bookId = bookService.createBook(book, file, savePath);
            response.setStatus(HttpServletResponse.SC_OK);
            Map<String, String> resp = new HashMap<>();
            resp.put(RESPONSE_PARAMETER_SUCCESS, RESPONSE_MESSAGE_OK);
            resp.put("id", String.valueOf(bookId));
            ObjectMapper objectMapper=new ObjectMapper();
            responseBody = objectMapper.writeValueAsString(resp);
        } catch (JsonParseException | JsonMappingException | IllegalArgumentException e) {
            log.error("Json transformation exception", e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseBody = addResponseBodyParameter(RESPONSE_PARAMETER_ERROR, "Invalid parameters");
        }
        return responseBody;
    }
}
