package com.itechart.javalab.library.controller.command.impl;

import com.itechart.javalab.library.controller.command.Command;
import com.itechart.javalab.library.controller.util.JspPageName;
import com.itechart.javalab.library.dto.SearchPageDto;
import com.itechart.javalab.library.model.Book;
import com.itechart.javalab.library.model.BookFilter;
import com.itechart.javalab.library.service.BookService;
import com.itechart.javalab.library.service.impl.DefaultBookService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class SearchBooksCommand implements Command {

    private BookService bookService;

    private static final String REQUEST_SEARCH_PAGE_DTO = "searchPageDto";
    private static final String REQUEST_IS_AVAILABLE_VALUE = "isAvailableOnly";
    private static final String REQUEST_BOOK_TITLE_SEARCH_PARAMETER = "bookTitle";
    private static final String REQUEST_BOOK_AUTHOR_SEARCH_PARAMETER = "bookAuthor";
    private static final String REQUEST_BOOK_GENRE_SEARCH_PARAMETER = "bookGenre";
    private static final String REQUEST_BOOK_DESCRIPTION_SEARCH_PARAMETER = "bookDescription";

    public SearchBooksCommand() {
        this.bookService = DefaultBookService.getInstance();
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        boolean isAvailableOnly = Boolean.parseBoolean(request.getParameter(REQUEST_IS_AVAILABLE_VALUE));
        String titleSearch = request.getParameter(REQUEST_BOOK_TITLE_SEARCH_PARAMETER);
        String authorSearch = request.getParameter(REQUEST_BOOK_AUTHOR_SEARCH_PARAMETER);
        String genreSearch = request.getParameter(REQUEST_BOOK_GENRE_SEARCH_PARAMETER);
        String descriptionSearch = request.getParameter(REQUEST_BOOK_DESCRIPTION_SEARCH_PARAMETER);


        if (ObjectUtils.allNull(titleSearch, authorSearch, genreSearch, descriptionSearch)) {
            request.getServletContext().getRequestDispatcher(JspPageName.SEARCH_PAGE).forward(request, response);

        } else if (StringUtils.isAllEmpty(titleSearch, authorSearch, genreSearch, descriptionSearch)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        } else {

            BookFilter bookFilter = BookFilter.builder()
                    .bookTitle(titleSearch)
                    .bookAuthor(authorSearch)
                    .bookDescription(descriptionSearch)
                    .bookGenre(genreSearch)
                    .isAvailableOnly(isAvailableOnly)
                    .build();

            Optional<List<Book>> allBooks = bookService.findBooksByParameters(bookFilter);

            if (allBooks.isPresent()) {

                SearchPageDto searchPageDto = SearchPageDto.builder()
                        .bookAuthor(authorSearch)
                        .bookDescription(descriptionSearch)
                        .bookGenre(genreSearch)
                        .bookTitle(titleSearch)
                        .isAvailableOnly(isAvailableOnly)
                        .books(allBooks.get())
                        .build();

                request.setAttribute(REQUEST_SEARCH_PAGE_DTO, searchPageDto);
                request.getServletContext().getRequestDispatcher(JspPageName.SEARCH_PAGE).forward(request, response);
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
    }
}