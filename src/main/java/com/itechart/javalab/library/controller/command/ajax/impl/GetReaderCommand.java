package com.itechart.javalab.library.controller.command.ajax.impl;

import com.itechart.javalab.library.controller.command.ajax.AjaxCommand;
import com.itechart.javalab.library.controller.util.json.impl.JacksonJsonBuilder;
import com.itechart.javalab.library.controller.util.json.JsonBuilder;
import com.itechart.javalab.library.model.Reader;
import com.itechart.javalab.library.service.ReaderService;
import com.itechart.javalab.library.service.impl.DefaultReaderService;
import org.apache.commons.text.StringEscapeUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;

import static com.itechart.javalab.library.controller.util.ResponseParameterName.RESPONSE_PARAMETER_ERROR;

public class GetReaderCommand implements AjaxCommand {

    private final ReaderService readerService;
    private final JsonBuilder jsonBuilder;
    private static final String READERS = "readers";
    private static final String REQUEST_PARAMETER_EMAIL = "email";

    public GetReaderCommand() {
        this.readerService = DefaultReaderService.getInstance();
        this.jsonBuilder = JacksonJsonBuilder.getInstance();
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String readerEmail = request.getParameter(REQUEST_PARAMETER_EMAIL);
        String responseBody;

        Optional<Set<Reader>> readersByEmail = readerService.getReadersByEmail(StringEscapeUtils.escapeHtml4(readerEmail));
        if (readersByEmail.isPresent()) {
            response.setStatus(HttpServletResponse.SC_OK);
            responseBody = jsonBuilder.getJsonFromKeyValue(READERS, readersByEmail.get());
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            responseBody = jsonBuilder.getJsonFromKeyValue(RESPONSE_PARAMETER_ERROR, "No such email");
        }
        return responseBody;
    }
}
