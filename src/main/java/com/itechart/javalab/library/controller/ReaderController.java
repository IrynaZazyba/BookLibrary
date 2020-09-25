package com.itechart.javalab.library.controller;

import com.itechart.javalab.library.controller.command.CommandProvider;
import com.itechart.javalab.library.controller.command.front.Command;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/readers",}, name = "ReaderController")
public class ReaderController extends HttpServlet {

    private static final long serialVersionUID = 3327094284378517874L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doProcess(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doProcess(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doProcess(req, resp);
    }


    private void doProcess(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CommandProvider commandProvider = CommandProvider.getInstance();
        Command command = commandProvider.getCommand(req.getMethod(), req.getRequestURI());
        command.execute(req, resp);
    }
}
