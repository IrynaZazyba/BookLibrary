package com.itechart.javalab.library.controller.command.impl;

import com.itechart.javalab.library.controller.command.Command;
import com.itechart.javalab.library.controller.util.JspPageName;
import com.itechart.javalab.library.model.Book;
import com.itechart.javalab.library.service.BookService;
import com.itechart.javalab.library.service.impl.DefaultBookService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class GetBooksCommand implements Command {

    private static final String REQUEST_ATTRIBUTE_LIST_BOOKS = "books";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        BookService bookService = DefaultBookService.getInstance();
        Optional<List<Book>> allBooks = bookService.getAllBooks();

        if (allBooks.isPresent()) {
            request.setAttribute(REQUEST_ATTRIBUTE_LIST_BOOKS, allBooks.get());
            forwardToPage(request, response, JspPageName.MAIN_PAGE);
        }

        if (allBooks.isEmpty()) {
            response.setStatus(500);
        }
    }
}