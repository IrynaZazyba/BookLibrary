package com.itechart.javalab.library.controller.command.ajax.impl;

import com.google.gson.Gson;
import com.itechart.javalab.library.controller.command.ajax.AjaxCommand;
import com.itechart.javalab.library.dto.BookDto;
import com.itechart.javalab.library.service.BookService;
import com.itechart.javalab.library.service.impl.DefaultBookService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UpdateBookCommand implements AjaxCommand {

    private BookService bookService;
    private static final String REQUEST_BOOK_PARAMETER = "book";

    public UpdateBookCommand() {
        this.bookService = DefaultBookService.getInstance();
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        BookDto book = gson.fromJson(request.getParameter(REQUEST_BOOK_PARAMETER), BookDto.class);

        if (bookService.updateBookInfo(book).isPresent()) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        }
        return null;
    }


}
