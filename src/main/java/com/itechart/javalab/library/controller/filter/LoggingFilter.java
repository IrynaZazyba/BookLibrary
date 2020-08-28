package com.itechart.javalab.library.controller.filter;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(urlPatterns = {"/*"})
public class LoggingFilter implements Filter {

    private final static Logger logger = LogManager.getLogger(LoggingFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        StringBuilder infoMessage = new StringBuilder();
        infoMessage.append(httpServletRequest.getMethod()).append(" ");
        infoMessage.append(httpServletRequest.getRequestURI()).append(", ");
        infoMessage.append(httpServletRequest.getSession().getId());

        logger.log(Level.INFO, infoMessage);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
