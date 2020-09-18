package com.itechart.javalab.library.controller.command.ajax.impl;

import com.itechart.javalab.library.controller.command.ajax.AjaxCommand;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.itechart.javalab.library.controller.util.ResponseParameterName.RESPONSE_PARAMETER_ERROR;

public class AjaxUnknownCommand implements AjaxCommand {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        return addResponseBodyParameter(RESPONSE_PARAMETER_ERROR, "Invalid parameters");
    }
}
