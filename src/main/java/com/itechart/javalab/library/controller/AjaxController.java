package com.itechart.javalab.library.controller;

import com.itechart.javalab.library.controller.command.CommandProvider;
import com.itechart.javalab.library.controller.command.ajax.AjaxCommand;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = {"/ajax/*"}, name = "AjaxReaderController")
@MultipartConfig(maxFileSize=1024*1024*2)
public class AjaxController extends HttpServlet {

    private static final long serialVersionUID = -6704867576406749056L;

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

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doProcess(req, resp);
    }


    private void doProcess(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CommandProvider commandProvider = CommandProvider.getInstance();
        AjaxCommand command = commandProvider.getAjaxCommand(req.getMethod(), req.getRequestURI());
        String jsonAnswer = command.execute(req, resp);
        PrintWriter out = resp.getWriter();
        out.print(jsonAnswer);
        out.flush();
    }
}
