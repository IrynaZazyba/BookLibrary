package com.itechart.javalab.library.controller.command.ajax.impl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.itechart.javalab.library.controller.command.ajax.AjaxCommand;
import com.itechart.javalab.library.controller.util.json.JsonBuilder;
import com.itechart.javalab.library.controller.util.json.impl.JacksonJsonBuilder;
import com.itechart.javalab.library.dto.ReaderDto;
import com.itechart.javalab.library.service.ReaderService;
import com.itechart.javalab.library.service.impl.DefaultReaderService;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.itechart.javalab.library.controller.util.ResponseParameterName.RESPONSE_PARAMETER_ERROR;
import static com.itechart.javalab.library.controller.util.ResponseParameterName.RESPONSE_PARAMETER_SUCCESS;

@Log4j2
public class EditReaderCommand implements AjaxCommand {

    private final ReaderService readerService;
    private final JsonBuilder jsonBuilder;
    private static final String READER_INFO = "dto";
    private static final String RESPONSE_MESSAGE_OK = "ok";

    public EditReaderCommand() {
        this.readerService = DefaultReaderService.getInstance();
        this.jsonBuilder = JacksonJsonBuilder.getInstance();
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String readers = request.getParameter(READER_INFO);
        String responseBody;
        try {
            ReaderDto readerDto = jsonBuilder.getObjectMapper().readValue(readers, ReaderDto.class);
            readerService.editReader(readerDto);
            response.setStatus(HttpServletResponse.SC_OK);
            responseBody = jsonBuilder.getJsonFromKeyValue(RESPONSE_PARAMETER_SUCCESS, RESPONSE_MESSAGE_OK);
        } catch (JsonParseException | JsonMappingException | IllegalArgumentException e) {
            log.error("Json transformation exception", e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseBody = jsonBuilder.getJsonFromKeyValue(RESPONSE_PARAMETER_ERROR, "Invalid parameters");
        }
        return responseBody;
    }
}
