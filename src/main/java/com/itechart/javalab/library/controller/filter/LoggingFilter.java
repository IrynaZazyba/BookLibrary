package com.itechart.javalab.library.controller.filter;

import lombok.extern.log4j.Log4j2;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Log4j2
@WebFilter(urlPatterns = {"/*"})
public class LoggingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        log.info("{} {}, {}", httpServletRequest.getMethod(),
                httpServletRequest.getRequestURI(),
                httpServletRequest.getSession().getId());
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
