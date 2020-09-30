package com.itechart.javalab.library.controller.command.front.impl;

import com.itechart.javalab.library.controller.command.front.Command;
import com.itechart.javalab.library.controller.util.JspPageName;
import com.itechart.javalab.library.model.EmailInfo;
import com.itechart.javalab.library.service.LibraryInfoService;
import com.itechart.javalab.library.service.impl.DefaultLibraryInfoService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class GetLibraryInfoPageCommand implements Command {

    private final LibraryInfoService libraryInfoService;
    private static final String REQUEST_LIBRARY_TEMPLATE_INFO = "emailInfo";

    public GetLibraryInfoPageCommand() {
        this.libraryInfoService = DefaultLibraryInfoService.getInstance();
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Optional<EmailInfo> libraryInfo = libraryInfoService.getLibraryInfo();
        libraryInfo.ifPresent(emailInfo -> request.setAttribute(REQUEST_LIBRARY_TEMPLATE_INFO, emailInfo));
        forwardToPage(request, response, JspPageName.LIBRARY_INFO_PAGE);
    }
}
