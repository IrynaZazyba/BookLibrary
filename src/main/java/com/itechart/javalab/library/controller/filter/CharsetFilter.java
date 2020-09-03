package com.itechart.javalab.library.controller.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import java.io.IOException;

/**
 * Sets character encoding for every request.
 */

@WebFilter(urlPatterns = {"/*"},
        initParams = {
        @WebInitParam(name = "encoding", value = "UTF-8", description = "Encoding Param")})
public class CharsetFilter implements Filter {

    private String defaultEncoding = "utf-8";
    private static final String ENCODING = "encoding";

    @Override
    public void init(FilterConfig filterConfig) {
        String encoding = filterConfig.getInitParameter(ENCODING);
        if (encoding != null) {
            defaultEncoding = encoding;
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        servletRequest.setCharacterEncoding(defaultEncoding);
        servletResponse.setCharacterEncoding(defaultEncoding);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
