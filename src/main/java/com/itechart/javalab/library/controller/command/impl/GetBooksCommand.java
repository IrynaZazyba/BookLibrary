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
    private static final String REQUEST_FILTER_VALUE = "isFiltered";


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        boolean isFiltered=getFilterValue(request);

        BookService bookService = DefaultBookService.getInstance();
        Optional<List<Book>> allBooks = bookService.getAllBooks(isFiltered);

        if (allBooks.isPresent()) {
            request.setAttribute(REQUEST_ATTRIBUTE_LIST_BOOKS, allBooks.get());
            request.setAttribute(REQUEST_FILTER_VALUE, isFiltered);
            forwardToPage(request, response, JspPageName.MAIN_PAGE);
        } else {
            response.setStatus(500);
        }

    }

    private boolean getFilterValue(HttpServletRequest request) {
        String filterParameter = request.getParameter(REQUEST_FILTER_VALUE);
        return filterParameter != null ? Boolean.valueOf(filterParameter) : false;
    }


}