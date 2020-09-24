package com.itechart.javalab.library.controller.command.front.impl;

import com.itechart.javalab.library.controller.command.front.Command;
import com.itechart.javalab.library.controller.util.JspPageName;
import com.itechart.javalab.library.dto.MainPageDto;
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

    private static final String REQUEST_SEARCH_PAGE_DTO = "dto";
    private static final String REQUEST_IS_AVAILABLE_VALUE = "isAvailableOnly";
    private static final String REQUEST_RECORDS_PER_PAGE = "recordsPerPage";
    private static final String REQUEST_CURRENT_PAGE = "currentPage";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        boolean isAvailableOnly = Boolean.parseBoolean(request.getParameter(REQUEST_IS_AVAILABLE_VALUE));
        String recordsPerPage = request.getParameter(REQUEST_RECORDS_PER_PAGE);
        String currentPage = request.getParameter(REQUEST_CURRENT_PAGE);

        Paginator paginator = new Paginator(recordsPerPage, currentPage);
        BookService bookService = DefaultBookService.getInstance();
        Optional<List<Book>> allBooks = bookService.getBooks(paginator, isAvailableOnly);
        Optional<Integer> numberOfBooksRecords = bookService.getNumberBooksRecords(isAvailableOnly);
        if (allBooks.isPresent() && numberOfBooksRecords.isPresent()) {
            paginator.setCountPages(numberOfBooksRecords.get());
            MainPageDto mainPageDto = MainPageDto.builder()
                    .isAvailableOnly(isAvailableOnly)
                    .books(allBooks.get())
                    .countPages(paginator.getCountPages())
                    .currentPage(paginator.getCurrentPage())
                    .recordsPerPage(paginator.getRecordsPerPage())
                    .build();
            request.setAttribute(REQUEST_SEARCH_PAGE_DTO, mainPageDto);
            forwardToPage(request, response, JspPageName.MAIN_PAGE);
        } else {
            response.setStatus(500);
        }
    }
}