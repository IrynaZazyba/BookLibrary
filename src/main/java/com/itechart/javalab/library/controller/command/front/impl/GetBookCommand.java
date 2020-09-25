package com.itechart.javalab.library.controller.command.front.impl;

import com.itechart.javalab.library.controller.command.front.Command;
import com.itechart.javalab.library.controller.util.JspPageName;
import com.itechart.javalab.library.dto.BookPageDto;
import com.itechart.javalab.library.model.Book;
import com.itechart.javalab.library.model.BorrowRecord;
import com.itechart.javalab.library.service.BookService;
import com.itechart.javalab.library.service.BorrowRecordService;
import com.itechart.javalab.library.service.impl.DefaultBookService;
import com.itechart.javalab.library.service.impl.DefaultBorrowRecordService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class GetBookCommand implements Command {

    private final BookService bookService;
    private final BorrowRecordService readerService;

    private static final String BOOK_PAGE_DTO = "bookPageDto";

    public GetBookCommand() {
        this.bookService = DefaultBookService.getInstance();
        this.readerService = DefaultBorrowRecordService.getInstance();
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int bookId = getBookId(request);
        Optional<Book> bookById = bookService.getBookById(bookId);
        Optional<List<BorrowRecord>> borrowRecords = readerService.getBorrowRecords(bookId);
        if (bookById.isPresent() && borrowRecords.isPresent()) {
            BookPageDto bookPageDto = new BookPageDto(bookById.get(),borrowRecords.get());
            request.setAttribute(BOOK_PAGE_DTO, bookPageDto);
            forwardToPage(request, response, JspPageName.BOOK_PAGE);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private int getBookId(HttpServletRequest request) {
        String[] partsUri = request.getRequestURI().split("/");
        return Integer.parseInt(partsUri[partsUri.length - 1]);
    }
}
