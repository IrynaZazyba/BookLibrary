package com.itechart.javalab.library.controller.command.impl;

import com.itechart.javalab.library.controller.command.Command;
import com.itechart.javalab.library.model.Book;
import com.itechart.javalab.library.service.BookService;
import com.itechart.javalab.library.service.factory.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class GetBooksCommand implements Command {


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {

        //get parameter from request
        //validate parameter
        //go to service
        //forward or redirect to jsp

    }
}
