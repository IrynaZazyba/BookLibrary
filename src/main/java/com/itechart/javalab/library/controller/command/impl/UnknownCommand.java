package com.itechart.javalab.library.controller.command.impl;

import com.itechart.javalab.library.controller.command.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UnknownCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {

    }
}
