package com.itechart.javalab.library.controller.listener;

import com.itechart.javalab.library.service.util.MailSender;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * The class {@code MailConfigListener} initializing mail config
 */
@Log4j2
public class MailConfigListener implements ServletContextListener {

    private static final String MAIL_CONTEXT_PARAMETER = "mailConfig";

    public MailConfigListener() {
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            String mailInitParameter = sce.getServletContext().getInitParameter(MAIL_CONTEXT_PARAMETER);
            InputStream mailConfig = getClass().getClassLoader().getResourceAsStream(mailInitParameter);
            Properties properties = new Properties();
            if (mailConfig != null) {
                properties.load(mailConfig);
                MailSender instance = MailSender.getInstance();
                instance.initializeSalSender(properties);
            }
        } catch (IOException e) {
            log.error("Exception in MailConfigListener in attempt to load properties", e);
            throw new RuntimeException("MailConfigListener didn't initialize.", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

}
