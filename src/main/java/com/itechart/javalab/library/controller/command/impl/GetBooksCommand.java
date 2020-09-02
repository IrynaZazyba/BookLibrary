package com.itechart.javalab.library.controller.command.impl;

import com.itechart.javalab.library.controller.command.Command;
import com.itechart.javalab.library.controller.util.JspPageName;
import com.itechart.javalab.library.model.Book;
import com.itechart.javalab.library.model.Paginator;
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
    private static final String REQUEST_RECORDS_PER_PAGE = "recordsPerPage";
    private static final String REQUEST_CURRENT_PAGE = "currentPage";
    private static final String REQUEST_COUNT_PAGES = "countPages";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        boolean isFiltered = getFilterValue(request);
        String recordsPerPage = request.getParameter(REQUEST_RECORDS_PER_PAGE);
        String currentPage = request.getParameter(REQUEST_CURRENT_PAGE);

        Paginator paginator = new Paginator(recordsPerPage, currentPage);

        BookService bookService = DefaultBookService.getInstance();
        Optional<List<Book>> allBooks = bookService.getAllBooks(paginator, isFiltered);

        Optional<Integer> numberOfBooksRecords = bookService.getNumberOfBooksRecords(isFiltered);

        if (allBooks.isPresent() && numberOfBooksRecords.isPresent()) {
            paginator.setCountPages(numberOfBooksRecords.get());

            request.setAttribute(REQUEST_ATTRIBUTE_LIST_BOOKS, allBooks.get());
            request.setAttribute(REQUEST_FILTER_VALUE, isFiltered);
            request.setAttribute(REQUEST_CURRENT_PAGE, paginator.getCurrentPage());
            request.setAttribute(REQUEST_RECORDS_PER_PAGE, paginator.getRecordsPerPage());
            request.setAttribute(REQUEST_COUNT_PAGES, paginator.getCountPages());

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