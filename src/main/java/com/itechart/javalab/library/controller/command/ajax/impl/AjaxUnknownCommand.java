package com.itechart.javalab.library.controller.command.ajax.impl;

import com.itechart.javalab.library.controller.command.ajax.AjaxCommand;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AjaxUnknownCommand implements AjaxCommand {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        return null;
    }
}
