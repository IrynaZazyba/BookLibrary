package com.itechart.javalab.library.controller.command.front.impl;

import com.itechart.javalab.library.controller.command.front.Command;
import com.itechart.javalab.library.controller.util.JspPageName;
import com.itechart.javalab.library.dto.ReaderPageDto;
import com.itechart.javalab.library.model.Paginator;
import com.itechart.javalab.library.model.Reader;
import com.itechart.javalab.library.service.ReaderService;
import com.itechart.javalab.library.service.impl.DefaultReaderService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;

public class GetReadersCommand implements Command {

    private final ReaderService readerService;
    private static final String REQUEST_SEARCH_PAGE_DTO = "dto";
    private static final String REQUEST_RECORDS_PER_PAGE = "recordsPerPage";
    private static final String REQUEST_CURRENT_PAGE = "currentPage";

    public GetReadersCommand() {
        this.readerService = DefaultReaderService.getInstance();
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String recordsPerPage = request.getParameter(REQUEST_RECORDS_PER_PAGE);
        String currentPage = request.getParameter(REQUEST_CURRENT_PAGE);
        Paginator paginator = new Paginator(recordsPerPage, currentPage);

        Optional<Set<Reader>> readers = readerService.getReaders(paginator);
        Optional<Integer> numberOfReadersRecords = readerService.getNumberReadersRecords();
        if (readers.isPresent() && numberOfReadersRecords.isPresent()) {
            paginator.setCountPages(numberOfReadersRecords.get());
            ReaderPageDto readerPageDto = ReaderPageDto.builder()
                    .readers(readers.get())
                    .countPages(paginator.getCountPages())
                    .currentPage(paginator.getCurrentPage())
                    .recordsPerPage(paginator.getRecordsPerPage())
                    .build();
            request.setAttribute(REQUEST_SEARCH_PAGE_DTO, readerPageDto);
            forwardToPage(request, response, JspPageName.READERS_PAGE);
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}