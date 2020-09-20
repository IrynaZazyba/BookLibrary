package com.itechart.javalab.library.service.util.notification;

import com.itechart.javalab.library.dao.ReaderDao;
import com.itechart.javalab.library.dao.impl.SqlReaderDao;
import com.itechart.javalab.library.model.BorrowRecord;
import com.itechart.javalab.library.service.util.MailSender;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupDir;

import java.time.format.DateTimeFormatter;
import java.util.List;


public class ReturnRemainder implements Job {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String MAIL_TEMPLATE_PATH = "path";
    private static final String STRING_TEMPLATE_RETURN_NAME = "returnNotification";
    private static final String USER_NAME = "name";
    private static final String BOOK_TITLE = "title";
    private static final String DUE_DATE = "dueDate";
    private static final String ISBN = "isbn";

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Object path = jobExecutionContext.getJobDetail().getJobDataMap().get(MAIL_TEMPLATE_PATH);
        ReaderDao readerDao = SqlReaderDao.getInstance();
        List<BorrowRecord> records = readerDao.getReturnNotificationInfo();
        records.forEach(record -> {
            STGroup g = new STGroupDir((String) path);
            ST returnNotification = g.getInstanceOf(STRING_TEMPLATE_RETURN_NAME);
            returnNotification.add(USER_NAME, record.getReader().getName());
            returnNotification.add(BOOK_TITLE, record.getBook().getTitle());
            returnNotification.add(DUE_DATE, record.getDueDate().format(formatter));
            returnNotification.add(ISBN, record.getBook().getISBN());
            MailSender mailSender = MailSender.getInstance();
            mailSender.send("Return notification", returnNotification.render(), record.getReader().getEmail());
        });
    }
}
