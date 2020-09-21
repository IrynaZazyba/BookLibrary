package com.itechart.javalab.library.presentation;

import com.itechart.javalab.library.model.Author;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Log4j2
public class AuthorsToStringTag extends TagSupport {

    private static final long serialVersionUID = 1790934664492620922L;
    private Set<Author> authors;

    @Override
    public int doStartTag() throws JspException {
        JspWriter out = pageContext.getOut();
        List<String> authorNames = new ArrayList<>();
        if (authors != null) {
            authors.forEach(author -> authorNames.add(author.getName()));
            String authors = StringUtils.join(authorNames, ", ");
            try {
                out.write(authors);
            } catch (IOException e) {
                log.error("IOException in doStartTag() method AuthorsToStringTag class", e);
                throw new JspException("Exception in attempt to write authors with help by tag", e);
            }
        }
        return SKIP_BODY;
    }


    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }
}
