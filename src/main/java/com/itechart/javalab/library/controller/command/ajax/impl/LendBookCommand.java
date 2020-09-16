package com.itechart.javalab.library.controller.command.ajax.impl;

import com.google.gson.Gson;
import com.itechart.javalab.library.controller.command.ajax.AjaxCommand;
import com.itechart.javalab.library.controller.util.BorrowRecordValidator;
import com.itechart.javalab.library.dto.BorrowRecordDto;
import com.itechart.javalab.library.service.ReaderService;
import com.itechart.javalab.library.service.impl.DefaultReaderService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LendBookCommand implements AjaxCommand {

    private ReaderService readerService;
    private static final String REQUEST_PARAMETER_EDITED_RECORDS = "addedRecords";
    private static final String RESPONSE_MESSAGE_PARTLY_FAILED = "partlyFailed";

    public LendBookCommand() {
        this.readerService = DefaultReaderService.getInstance();
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        BorrowRecordDto[] records = parseBorrowRecords(request, gson);
        boolean result = true;

        if (validateBorrowRecordsDto(records)) {
            result = readerService.lendBook(records);
        }

        if (result) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            return addResponseMessage(response, RESPONSE_MESSAGE_PARTLY_FAILED, gson);
        }
        return null;
    }


    private BorrowRecordDto[] parseBorrowRecords(HttpServletRequest request, Gson gson) {
        String editedRecords = request.getParameter(REQUEST_PARAMETER_EDITED_RECORDS);
        return gson.fromJson(editedRecords, BorrowRecordDto[].class);
    }

    private boolean validateBorrowRecordsDto(BorrowRecordDto[] borrowRecordDtos) {
        boolean validationResult = true;

        for (BorrowRecordDto borrowRecordDto : borrowRecordDtos) {
            boolean validate = BorrowRecordValidator.validateAddRecord(borrowRecordDto);
            if (!validate) {
                validationResult = false;
            }
        }
        return validationResult;
    }
}
