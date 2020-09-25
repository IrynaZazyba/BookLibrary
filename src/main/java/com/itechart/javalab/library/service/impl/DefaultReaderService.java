package com.itechart.javalab.library.service.impl;

import com.itechart.javalab.library.dao.ReaderDao;
import com.itechart.javalab.library.dao.ReceiveReaderDao;
import com.itechart.javalab.library.dao.impl.SqlReaderDao;
import com.itechart.javalab.library.dao.impl.SqlReceiveReaderDao;
import com.itechart.javalab.library.dto.BorrowRecordDto;
import com.itechart.javalab.library.dto.ReaderDto;
import com.itechart.javalab.library.model.BorrowRecord;
import com.itechart.javalab.library.model.Paginator;
import com.itechart.javalab.library.model.Reader;
import com.itechart.javalab.library.service.BookService;
import com.itechart.javalab.library.service.ReaderService;
import org.apache.commons.text.StringEscapeUtils;

import java.time.LocalDateTime;
import java.util.*;

public class DefaultReaderService implements ReaderService {

    private final ReaderDao readerDao;
    private final ReceiveReaderDao receiveReaderDao;
    private static volatile ReaderService instance;

    private DefaultReaderService(ReaderDao readerDao, ReceiveReaderDao receiveReaderDao) {
        this.readerDao = readerDao;
        this.receiveReaderDao = receiveReaderDao;
    }

    public static ReaderService getInstance() {
        if (instance == null) {
            synchronized (BookService.class) {
                if (instance == null) {
                    instance = new DefaultReaderService(SqlReaderDao.getInstance(), SqlReceiveReaderDao.getInstance());
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

    @Override
    public Optional<Set<Reader>> getReadersByEmail(String email) {
        return readerDao.getReadersByEmail(email);
    }

    @Override
    public Optional<Set<Reader>> getReaders(Paginator paginator) {
        return receiveReaderDao.getReaders(paginator);
    }

    @Override
    public Optional<Integer> getNumberReadersRecords() {
        return receiveReaderDao.getNumberReadersRecords();
    }

    @Override
    public boolean addReader(ReaderDto readerDto) {

        return false;
    }

    @Override
    public void editReader(ReaderDto readerDto) {
    }
}
