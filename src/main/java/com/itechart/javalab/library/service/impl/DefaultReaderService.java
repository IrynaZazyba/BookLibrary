package com.itechart.javalab.library.service.impl;

import com.itechart.javalab.library.dao.ReaderDao;
import com.itechart.javalab.library.dao.impl.SqlReaderDao;
import com.itechart.javalab.library.model.BorrowRecord;
import com.itechart.javalab.library.service.BookService;
import com.itechart.javalab.library.service.ReaderService;
import org.apache.commons.text.StringEscapeUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DefaultReaderService implements ReaderService {

    private final ReaderDao readerDao;
    private static volatile ReaderService instance;

    private DefaultReaderService(ReaderDao readerDao) {
        this.readerDao = readerDao;
    }

    public static ReaderService getInstance() {
        if (instance == null) {
            synchronized (BookService.class) {
                if (instance == null) {
                    instance = new DefaultReaderService(SqlReaderDao.getInstance());
                }
            }
        }
        return instance;
    }

    public Optional<List<BorrowRecord>> getBorrowRecords(int bookId) {
        return readerDao.getBorrowRecords(bookId);
    }

    @Override
    public boolean returnBook(BorrowRecord[] records) {
        LocalDateTime current = LocalDateTime.now();
        for (BorrowRecord record : records) {
            record.setReturnDate(current);
            record.setComment(StringEscapeUtils.escapeHtml4(record.getComment()));
        }
        return readerDao.setBorrowRecordStatus(Arrays.asList(records));
    }

    @Override
    public boolean lendBook(BorrowRecord[] records) {
        LocalDateTime current = LocalDateTime.now();
        for (BorrowRecord record : records) {
            record.setBorrowDate(current);
            record.setDueDate(current.plusMonths(record.getTimePeriod().getMonthPeriod()));
            record.setComment(StringEscapeUtils.escapeHtml4(record.getComment()));
        }
        return readerDao.createBorrowRecord(Arrays.asList(records));
    }


    @Override
    public boolean changeBorrowStatus(BorrowRecord[] records) {
        return readerDao.updateStatusBorrowRecords(Arrays.asList(records));
    }
}
