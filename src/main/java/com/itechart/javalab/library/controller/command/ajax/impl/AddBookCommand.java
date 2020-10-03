package com.itechart.javalab.library.controller.command.ajax.impl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.itechart.javalab.library.controller.command.ajax.AjaxCommand;
import com.itechart.javalab.library.controller.util.RequestBodyHelper;
import com.itechart.javalab.library.controller.util.json.JsonBuilder;
import com.itechart.javalab.library.controller.util.json.impl.JacksonJsonBuilder;
import com.itechart.javalab.library.dto.BookDto;
import com.itechart.javalab.library.service.BookService;
import com.itechart.javalab.library.service.impl.DefaultBookService;
import com.itechart.javalab.library.service.util.FileFormatValidator;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.itechart.javalab.library.controller.util.ResponseParameterName.RESPONSE_PARAMETER_ERROR;
import static com.itechart.javalab.library.controller.util.ResponseParameterName.RESPONSE_PARAMETER_SUCCESS;

@Log4j2
public class AddBookCommand implements AjaxCommand {

    private final BookService bookService;
    private final JsonBuilder jsonBuilder;
    private final FileFormatValidator fileFormatValidator;
    private static final String BOOK_INFO = "bookDto";
    private static final String RESPONSE_MESSAGE_OK = "ok";
    private static final String UPLOAD_FILE = "image_uploads";

    public AddBookCommand() {
        this.bookService = DefaultBookService.getInstance();
        this.jsonBuilder = JacksonJsonBuilder.getInstance();
        this.fileFormatValidator = FileFormatValidator.getInstance();
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String savePath = defineUploadPath(request);
        Part file = RequestBodyHelper.extractPartByName(request, UPLOAD_FILE);
        Part part = RequestBodyHelper.extractPartByName(request, BOOK_INFO);
        String responseBody;
        try {
            fileFormatValidator.validate(file.getSubmittedFileName());
            BookDto book = BookDto.fromJson(RequestBodyHelper.getValue(part));
            int bookId = bookService.createBook(book, file, savePath);
            response.setStatus(HttpServletResponse.SC_OK);
            Map<String, String> resp = new HashMap<>();
            resp.put(RESPONSE_PARAMETER_SUCCESS, RESPONSE_MESSAGE_OK);
            resp.put("id", String.valueOf(bookId));
            responseBody = jsonBuilder.getJsonFromMap(resp);
        } catch (JsonParseException | JsonMappingException | IllegalArgumentException e) {
            log.error("Invalid parameters in AddBookCommand", e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseBody = jsonBuilder.getJsonFromKeyValue(RESPONSE_PARAMETER_ERROR, "Invalid parameters");
        }
        return responseBody;
    }
}
