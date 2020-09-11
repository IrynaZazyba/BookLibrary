package com.itechart.javalab.library.presentation;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateTimeTag extends TagSupport {

    private static final long serialVersionUID = -8894517676471114286L;
    private static final String PATTERN_DATE = "MMMM dd, yyyy HH:mm:ss";
    private String date;

    @Override
    public int doStartTag() throws JspException {

        JspWriter out = pageContext.getOut();
        try {
            LocalDateTime ldt = LocalDateTime.parse(date);
            ZonedDateTime zdt = ZonedDateTime.of(ldt, ZoneId.systemDefault());
            DateTimeFormatter pattern = DateTimeFormatter.ofPattern(PATTERN_DATE, Locale.UK);
            out.write(zdt.format(pattern));
        } catch (IOException e) {
            throw new JspException(e);
        }
        return SKIP_BODY;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
