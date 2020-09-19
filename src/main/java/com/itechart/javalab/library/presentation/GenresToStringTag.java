package com.itechart.javalab.library.presentation;

import com.itechart.javalab.library.model.Genre;
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
public class GenresToStringTag extends TagSupport {


    private static final long serialVersionUID = 1069760003032182702L;
    private Set<Genre> genres;

    @Override
    public int doStartTag() throws JspException {
        JspWriter out = pageContext.getOut();
        List<String> genreNames = new ArrayList<>();
        genres.forEach(genre -> genreNames.add(genre.getGenre()));
        String genresToString = StringUtils.join(genreNames, ", ");

        try {
            out.write(genresToString);
        } catch (IOException e) {
            log.error("IOException in doStartTag() method GenresToStringTag class", e);
            throw new JspException("Exception in attempt to write genres with help by tag", e);
        }
        return SKIP_BODY;
    }


    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }
}
