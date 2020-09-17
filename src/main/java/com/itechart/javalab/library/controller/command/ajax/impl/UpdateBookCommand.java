package com.itechart.javalab.library.controller.command.ajax.impl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.itechart.javalab.library.controller.command.ajax.AjaxCommand;
import com.itechart.javalab.library.controller.util.JsonConverter;
import com.itechart.javalab.library.dto.BookDto;
import com.itechart.javalab.library.service.BookService;
import com.itechart.javalab.library.service.impl.DefaultBookService;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
public class UpdateBookCommand implements AjaxCommand {

    private BookService bookService;
    private static final String REQUEST_BOOK_PARAMETER = "bookDto";

    public UpdateBookCommand() {
        this.bookService = DefaultBookService.getInstance();
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            BookDto book = JsonConverter.fromJsonBook(request.getParameter(REQUEST_BOOK_PARAMETER));

            if (bookService.updateBookInfo(book).isPresent()) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
            }
        } catch (JsonParseException | JsonMappingException e) {
            log.error("Json transformation exception", e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return null;
    }


}
