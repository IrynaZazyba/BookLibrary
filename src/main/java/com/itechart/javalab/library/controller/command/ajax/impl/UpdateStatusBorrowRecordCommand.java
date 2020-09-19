package com.itechart.javalab.library.controller.command.ajax.impl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.itechart.javalab.library.controller.command.ajax.AjaxCommand;
import com.itechart.javalab.library.dto.BorrowRecordDto;
import com.itechart.javalab.library.service.ReaderService;
import com.itechart.javalab.library.service.impl.DefaultReaderService;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.itechart.javalab.library.controller.util.ResponseParameterName.*;

@Log4j2
public class UpdateStatusBorrowRecordCommand implements AjaxCommand {

    private final ReaderService readerService;
    private static final String REQUEST_PARAMETER_UPDATED_STATUS = "updatedStatus";
    private static final String RESPONSE_MESSAGE_PARTLY_FAILED = "partlyFailed";
    private static final String RESPONSE_MESSAGE_OK = "ok";

    public UpdateStatusBorrowRecordCommand() {
        this.readerService = DefaultReaderService.getInstance();
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String editedRecords = request.getParameter(REQUEST_PARAMETER_UPDATED_STATUS);
        String responseBody;
        try {
            BorrowRecordDto[] records = BorrowRecordDto.parseBorrowRecords(editedRecords);
            boolean result = readerService.changeBorrowStatus(records);
            if (result) {
                response.setStatus(HttpServletResponse.SC_OK);
                responseBody = addResponseBodyParameter(RESPONSE_PARAMETER_SUCCESS, RESPONSE_MESSAGE_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_OK);
                responseBody = addResponseBodyParameter(RESPONSE_PARAMETER_MESSAGE, RESPONSE_MESSAGE_PARTLY_FAILED);
            }
        } catch (JsonParseException | JsonMappingException | IllegalArgumentException e) {
            log.error("Json transformation exception", e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseBody = addResponseBodyParameter(RESPONSE_PARAMETER_ERROR, "Invalid parameters");
        }
        return responseBody;
    }

}
