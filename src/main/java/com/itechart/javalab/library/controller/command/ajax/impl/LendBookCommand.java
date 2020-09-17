package com.itechart.javalab.library.controller.command.ajax.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itechart.javalab.library.controller.command.ajax.AjaxCommand;
import com.itechart.javalab.library.controller.util.JsonConverter;
import com.itechart.javalab.library.controller.util.ReaderValidator;
import com.itechart.javalab.library.model.BorrowRecord;
import com.itechart.javalab.library.service.ReaderService;
import com.itechart.javalab.library.service.impl.DefaultReaderService;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
public class LendBookCommand implements AjaxCommand {

    private ReaderService readerService;
    private static final String REQUEST_PARAMETER_EDITED_RECORDS = "addedRecords";
    private static final String RESPONSE_MESSAGE_PARTLY_FAILED = "partlyFailed";

    public LendBookCommand() {
        this.readerService = DefaultReaderService.getInstance();
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String editedRecords = request.getParameter(REQUEST_PARAMETER_EDITED_RECORDS);
        try {
            BorrowRecord[] borrowRecords = JsonConverter.fromJsonBorrowRecordArray(editedRecords);
            if (!validateReaders(borrowRecords)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }

            if (validateReaders(borrowRecords)) {
                boolean result = readerService.lendBook(borrowRecords);
                if (result) {
                    response.setStatus(HttpServletResponse.SC_OK);
                } else {
                    response.setStatus(HttpServletResponse.SC_OK);
                    return JsonConverter.addResponseMessage(RESPONSE_MESSAGE_PARTLY_FAILED);
                }
            }
        } catch (JsonProcessingException e) {
            log.error("Json transformation exception", e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return null;
    }


    private boolean validateReaders(BorrowRecord[] borrowRecords) {
        boolean validationResult = true;
        for (BorrowRecord borrowRecord : borrowRecords) {
            boolean validate = ReaderValidator.validate(borrowRecord.getReader());
            if (!validate) {
                validationResult = false;
            }
        }
        return validationResult;
    }


}
