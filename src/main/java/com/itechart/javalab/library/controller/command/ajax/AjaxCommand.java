package com.itechart.javalab.library.controller.command.ajax;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public interface AjaxCommand {

    String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

    default String defineUploadPath(HttpServletRequest request) {
        String appPath = request.getServletContext().getRealPath("");
        String uploadDirectory = request.getServletContext().getInitParameter("fileUploadPath");
        return appPath + File.separator + uploadDirectory;
    }
}
