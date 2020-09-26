package com.itechart.javalab.library.service.impl;

import com.itechart.javalab.library.dao.BorrowRecordDao;
import com.itechart.javalab.library.dao.impl.SqlBorrowRecordDao;
import com.itechart.javalab.library.dto.BorrowRecordDto;
import com.itechart.javalab.library.model.BorrowRecord;
import com.itechart.javalab.library.service.BookService;
import com.itechart.javalab.library.service.BorrowRecordService;
import org.apache.commons.text.StringEscapeUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DefaultBorrowRecordService implements BorrowRecordService {

    private final BorrowRecordDao readerDao;
    private static volatile BorrowRecordService instance;

    private DefaultBorrowRecordService(BorrowRecordDao readerDao) {
        this.readerDao = readerDao;
    }

    public static BorrowRecordService getInstance() {
        if (instance == null) {
            synchronized (BookService.class) {
                if (instance == null) {
                    instance = new DefaultBorrowRecordService(SqlBorrowRecordDao.getInstance());
                }
            }
        }
        return instance;
    }

    public Optional<List<BorrowRecord>> getBorrowRecords(int bookId) {
        return readerDao.getBorrowRecords(bookId);
    }

    @Override
    public boolean addBorrowStatus(BorrowRecordDto[] records) {
        List<BorrowRecord> borrowRecords = new ArrayList<>();
        Arrays.stream(records).forEach(record ->
                borrowRecords.add(record.toBookEditRecordModel()));
        LocalDateTime current = LocalDateTime.now();
        borrowRecords.forEach(r -> {
            r.setReturnDate(current);
            r.setComment(StringEscapeUtils.escapeHtml4(r.getComment()));
        });
        return readerDao.setBorrowRecordStatus(borrowRecords);
    }

    @Override
    public boolean addBorrowRecords(BorrowRecordDto[] records) {
        List<BorrowRecord> borrowRecords = new ArrayList<>();
        Arrays.stream(records).forEach(record ->
                borrowRecords.add(record.toBookAddRecordModel()));
        LocalDateTime current = LocalDateTime.now();
        borrowRecords.forEach(r -> {
            r.setBorrowDate(current);
            r.setDueDate(current.plusMonths(r.getTimePeriod().getMonthPeriod()));
            r.setComment(StringEscapeUtils.escapeHtml4(r.getComment()));
        });
        return readerDao.createBorrowRecord(borrowRecords);
    }

    @Override
    public boolean changeBorrowStatus(BorrowRecordDto[] records) {
        List<BorrowRecord> borrowRecords = new ArrayList<>();
        Arrays.stream(records).forEach(record ->
                borrowRecords.add(record.toBookEditRecordModel()));
        return readerDao.updateStatusBorrowRecords(borrowRecords);
    }

}
