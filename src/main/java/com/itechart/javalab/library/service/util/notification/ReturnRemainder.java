package com.itechart.javalab.library.service.util.notification;

import com.itechart.javalab.library.dao.BorrowRecordDao;
import com.itechart.javalab.library.dao.LibraryInfoDao;
import com.itechart.javalab.library.dao.impl.SqlBorrowRecordDao;
import com.itechart.javalab.library.dao.impl.SqlLibraryInfoDao;
import com.itechart.javalab.library.model.BorrowRecord;
import com.itechart.javalab.library.model.LibraryEmailInfo;
import com.itechart.javalab.library.service.util.MailSender;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupDir;

import java.util.List;
import java.util.Optional;


public class ReturnRemainder extends Remainder implements Job {

    private static final String MAIL_TEMPLATE_PATH = "path";
    private static final String STRING_TEMPLATE_RETURN_NAME = "returnNotification";

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Object path = jobExecutionContext.getJobDetail().getJobDataMap().get(MAIL_TEMPLATE_PATH);
        BorrowRecordDao readerDao = SqlBorrowRecordDao.getInstance();
        LibraryInfoDao libraryInfoDao = SqlLibraryInfoDao.getInstance();
        Optional<LibraryEmailInfo> info = libraryInfoDao.getLibraryInfo();
        LibraryEmailInfo libraryInfo = info.orElseGet(LibraryEmailInfo::getDefaultValue);

        List<BorrowRecord> records = readerDao.getReturnNotificationInfo();
        records.forEach(record -> {
            STGroup g = new STGroupDir((String) path);
            ST returnNotification = g.getInstanceOf(STRING_TEMPLATE_RETURN_NAME);
            fillNotification(returnNotification, libraryInfo, record);
            MailSender mailSender = MailSender.getInstance();
            mailSender.send("Return notification", returnNotification.render(), record.getReader().getEmail());
        });
    }
}
