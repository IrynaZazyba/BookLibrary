package com.itechart.javalab.library.controller.command.ajax.impl;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.itechart.javalab.library.controller.command.ajax.AjaxCommand;
import com.itechart.javalab.library.controller.util.BorrowRecordValidator;
import com.itechart.javalab.library.dto.BorrowRecordDto;
import com.itechart.javalab.library.service.ReaderService;
import com.itechart.javalab.library.service.impl.DefaultReaderService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ReturnBookCommand implements AjaxCommand {

    private ReaderService readerService;
    private static final String REQUEST_PARAMETER_EDITED_RECORDS = "editedRecords";
    private static final String RESPONSE_PARAMETER_MESSAGE = "message";
    private static final String RESPONSE_MESSAGE_PARTLY_FAILED = "partlyFailed";

    public ReturnBookCommand() {
        this.readerService = DefaultReaderService.getInstance();
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        BorrowRecordDto[] records = parseBorrowRecords(request);
        boolean result = true;

        if (validateBorrowRecordsDto(records)) {
            result = readerService.returnBook(records);
        }

        if (result) {
            response.setStatus(HttpServletResponse.SC_OK);
            return null;
        } else {
            Gson gson = new Gson();
            response.setStatus(HttpServletResponse.SC_OK);
            JsonObject responseMessage = new JsonObject();
            JsonElement jsonElement = gson.toJsonTree(RESPONSE_MESSAGE_PARTLY_FAILED);
            responseMessage.add(RESPONSE_PARAMETER_MESSAGE, jsonElement);
            return responseMessage.toString();
        }
    }


    private BorrowRecordDto[] parseBorrowRecords(HttpServletRequest request) {
        String editedRecords = request.getParameter(REQUEST_PARAMETER_EDITED_RECORDS);
        Gson gson = new Gson();
        return gson.fromJson(editedRecords, BorrowRecordDto[].class);
    }

    private boolean validateBorrowRecordsDto(BorrowRecordDto[] records) {
        boolean validationResult = true;

        for (BorrowRecordDto borrowRecordDto : records) {
            boolean validate = BorrowRecordValidator.validateEditRecord(borrowRecordDto);
            if (!validate) {
                validationResult = false;
            }
        }
        return validationResult;
    }
}
