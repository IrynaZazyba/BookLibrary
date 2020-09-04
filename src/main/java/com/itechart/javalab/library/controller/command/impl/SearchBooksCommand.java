package com.itechart.javalab.library.controller.command.impl;

import com.itechart.javalab.library.controller.command.Command;
import com.itechart.javalab.library.controller.util.JspPageName;
import com.itechart.javalab.library.controller.util.BookSearchValidator;
import com.itechart.javalab.library.model.Book;
import com.itechart.javalab.library.service.BookService;
import com.itechart.javalab.library.service.impl.DefaultBookService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class SearchBooksCommand implements Command {

    private static final String REQUEST_ATTRIBUTE_LIST_BOOKS = "books";
    private static final String REQUEST_IS_AVAILABLE_VALUE = "isAvailableOnly";
    private static final String REQUEST_BOOK_TITLE_SEARCH_PARAMETER = "bookTitle";
    private static final String REQUEST_BOOK_AUTHOR_SEARCH_PARAMETER = "bookAuthor";
    private static final String REQUEST_BOOK_GENRE_SEARCH_PARAMETER = "bookGenre";
    private static final String REQUEST_BOOK_DESCRIPTION_SEARCH_PARAMETER = "bookDescription";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        boolean isAvailableOnly = getFilterValue(request);
        String bookTitleSearchParameter = request.getParameter(REQUEST_BOOK_TITLE_SEARCH_PARAMETER);
        String bookAuthorSearchParameter = request.getParameter(REQUEST_BOOK_AUTHOR_SEARCH_PARAMETER);
        String bookGenreSearchParameter = request.getParameter(REQUEST_BOOK_GENRE_SEARCH_PARAMETER);
        String bookDescriptionSearchParameter = request.getParameter(REQUEST_BOOK_DESCRIPTION_SEARCH_PARAMETER);

        if (BookSearchValidator.isAtLeastOneParameterNull(bookTitleSearchParameter,
                bookAuthorSearchParameter,
                bookGenreSearchParameter,
                bookDescriptionSearchParameter)) {

            request.getServletContext().getRequestDispatcher(JspPageName.SEARCH_PAGE).forward(request, response);

        } else if (BookSearchValidator.isAllParametersEmpty(bookTitleSearchParameter,
                bookAuthorSearchParameter,
                bookGenreSearchParameter,
                bookDescriptionSearchParameter)) {

            response.setStatus(409);

        } else {

            BookService bookService = DefaultBookService.getInstance();
            Optional<List<Book>> allBooks = bookService.
                    findBooksByParameters(isAvailableOnly,
                            bookTitleSearchParameter,
                            bookAuthorSearchParameter,
                            bookGenreSearchParameter,
                            bookDescriptionSearchParameter);

            if (allBooks.isPresent()) {
                request.setAttribute(REQUEST_ATTRIBUTE_LIST_BOOKS, allBooks.get());
                request.setAttribute(REQUEST_IS_AVAILABLE_VALUE, isAvailableOnly);
                request.setAttribute(REQUEST_BOOK_TITLE_SEARCH_PARAMETER, bookTitleSearchParameter);
                request.setAttribute(REQUEST_BOOK_AUTHOR_SEARCH_PARAMETER, bookAuthorSearchParameter);
                request.setAttribute(REQUEST_BOOK_GENRE_SEARCH_PARAMETER, bookGenreSearchParameter);
                request.setAttribute(REQUEST_BOOK_DESCRIPTION_SEARCH_PARAMETER, bookDescriptionSearchParameter);

                request.getServletContext().getRequestDispatcher(JspPageName.SEARCH_PAGE).forward(request, response);
            } else {
                response.setStatus(500);
            }
        }
    }

    private boolean getFilterValue(HttpServletRequest request) {
        String filterParameter = request.getParameter(REQUEST_IS_AVAILABLE_VALUE);
        return Boolean.parseBoolean(filterParameter);
    }

}