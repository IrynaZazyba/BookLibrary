package com.itechart.javalab.library.controller.command.ajax.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itechart.javalab.library.controller.command.ajax.AjaxCommand;
import com.itechart.javalab.library.service.BookService;
import com.itechart.javalab.library.service.impl.DefaultBookService;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.itechart.javalab.library.controller.util.ResponseParameterName.*;

@Log4j2
public class DeleteBookCommand implements AjaxCommand {

    private final BookService bookService;
    private static final String REQUEST_PARAMETER_DELETED_BOOKS_ID = "deletedBooks";
    private static final String RESPONSE_MESSAGE_OK = "ok";
    private static final String RESPONSE_MESSAGE_PARTLY_FAILED = "partlyFailed";

    public DeleteBookCommand() {
        this.bookService = DefaultBookService.getInstance();
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String responseBody;
        try {
            ObjectMapper mapper = new ObjectMapper();
            int[] deletedBooks = mapper.readValue(request.getParameter(REQUEST_PARAMETER_DELETED_BOOKS_ID), int[].class);
            if (bookService.deleteBooks(deletedBooks)) {
                response.setStatus(HttpServletResponse.SC_OK);
                responseBody = addResponseBodyParameter(RESPONSE_PARAMETER_SUCCESS, RESPONSE_MESSAGE_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                responseBody = addResponseBodyParameter(RESPONSE_PARAMETER_MESSAGE, RESPONSE_MESSAGE_PARTLY_FAILED);
            }
        } catch (JsonProcessingException e) {
            log.error("Json transformation exception", e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseBody = addResponseBodyParameter(RESPONSE_PARAMETER_ERROR, "Invalid parameters");
        }
        return responseBody;
    }
}
