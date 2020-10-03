package com.itechart.javalab.library.controller.command.ajax.impl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.itechart.javalab.library.controller.command.ajax.AjaxCommand;
import com.itechart.javalab.library.controller.util.json.JsonBuilder;
import com.itechart.javalab.library.controller.util.json.impl.JacksonJsonBuilder;
import com.itechart.javalab.library.dto.LibraryInfoDto;
import com.itechart.javalab.library.service.LibraryInfoService;
import com.itechart.javalab.library.service.impl.DefaultLibraryInfoService;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.itechart.javalab.library.controller.util.ResponseParameterName.RESPONSE_PARAMETER_ERROR;
import static com.itechart.javalab.library.controller.util.ResponseParameterName.RESPONSE_PARAMETER_SUCCESS;

@Log4j2
public class AddLibraryInfoCommand implements AjaxCommand {

    private final LibraryInfoService libraryInfoService;
    private final JsonBuilder jsonBuilder;
    private static final String LIBRARY_INFO = "info";
    private static final String RESPONSE_MESSAGE_OK = "ok";


    public AddLibraryInfoCommand() {
        this.libraryInfoService = DefaultLibraryInfoService.getInstance();
        this.jsonBuilder = JacksonJsonBuilder.getInstance();
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String info = request.getParameter(LIBRARY_INFO);
        String responseBody;
        try {
            LibraryInfoDto libraryInfo = jsonBuilder.getObjectMapper().readValue(info, LibraryInfoDto.class);
            libraryInfoService.addLibraryInfo(libraryInfo);
            response.setStatus(HttpServletResponse.SC_OK);
            responseBody = jsonBuilder.getJsonFromKeyValue(RESPONSE_PARAMETER_SUCCESS, RESPONSE_MESSAGE_OK);
        } catch (JsonParseException | JsonMappingException e) {
            log.error("Invalid parameters in AddLibraryInfoCommand", e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseBody = jsonBuilder.getJsonFromKeyValue(RESPONSE_PARAMETER_ERROR, "Invalid parameters");
        }
        return responseBody;
    }
}
