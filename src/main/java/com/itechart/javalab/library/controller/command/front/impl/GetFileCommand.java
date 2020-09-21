package com.itechart.javalab.library.controller.command.front.impl;

import com.itechart.javalab.library.controller.command.front.Command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class GetFileCommand implements Command {

    private static final String REQUEST_FILE_NAME_PARAMETER = "name";
    private static final String SERVLET_CONTEXT_UPLOAD_PATH_PARAMETER = "fileUploadPath";
    private static final String DEFAULT_UPLOAD_DIRECTORY = "/resources/img";
    private static final String DEFAULT_BOOK_COVER = "book.png";

    public GetFileCommand() {
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fileName = request.getParameter(REQUEST_FILE_NAME_PARAMETER);
        String uploadDirectory = request.getServletContext().getInitParameter(SERVLET_CONTEXT_UPLOAD_PATH_PARAMETER);
        String appPath=request.getServletContext().getRealPath("");
        String uploadPath = appPath  + File.separator + uploadDirectory + File.separator + fileName;

        File file = new File(uploadPath);
        if (!file.exists()) {
            uploadDirectory = DEFAULT_UPLOAD_DIRECTORY;
            fileName = DEFAULT_BOOK_COVER;
        }
        displayFile(request, response, uploadDirectory, fileName);
    }

    private void displayFile(HttpServletRequest request, HttpServletResponse response,
                                String uploadDirectory, String fileName) throws IOException {
        try (InputStream in = request.getServletContext()
                .getResourceAsStream(uploadDirectory + File.separator + fileName);
             OutputStream out = response.getOutputStream()) {
            if (in != null) {
                byte[] buffer = in.readAllBytes();
                response.setContentLength(buffer.length);
                response.setContentType(request.getServletContext().getMimeType(fileName));
                out.write(buffer);
                out.flush();
            }
        }
    }
}
