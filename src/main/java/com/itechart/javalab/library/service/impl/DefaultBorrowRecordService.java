package com.itechart.javalab.library.service.impl;

import com.itechart.javalab.library.dao.BorrowRecordDao;
import com.itechart.javalab.library.dao.ReaderDao;
import com.itechart.javalab.library.dao.impl.SqlBorrowRecordDao;
import com.itechart.javalab.library.dao.impl.SqlReaderDao;
import com.itechart.javalab.library.dto.BorrowRecordDto;
import com.itechart.javalab.library.domain.entity.BorrowRecord;
import com.itechart.javalab.library.service.BookService;
import com.itechart.javalab.library.service.BorrowRecordService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DefaultBorrowRecordService implements BorrowRecordService {

    private final BorrowRecordDao borrowRecordDao;
    private final ReaderDao readerDao;
    private static volatile BorrowRecordService instance;

    private DefaultBorrowRecordService(BorrowRecordDao borrowRecordDao, ReaderDao readerDao) {
        this.borrowRecordDao = borrowRecordDao;
        this.readerDao = readerDao;
    }

    public static BorrowRecordService getInstance() {
        if (instance == null) {
            synchronized (BookService.class) {
                if (instance == null) {
                    instance = new DefaultBorrowRecordService(
                            SqlBorrowRecordDao.getInstance(), SqlReaderDao.getInstance());
                }
            }
        }
        return instance;
    }

    public Optional<List<BorrowRecord>> getBorrowRecords(int bookId) {
        return borrowRecordDao.getBorrowRecords(bookId);
    }

    @Override
    public boolean addBorrowStatus(BorrowRecordDto[] records) {
        List<BorrowRecord> borrowRecords = new ArrayList<>();
        Arrays.stream(records).forEach(record ->
                borrowRecords.add(record.toBookEditRecordModel()));
        LocalDateTime current = LocalDateTime.now();
        borrowRecords.forEach(r -> {
            r.setReturnDate(current);
        });
        return borrowRecordDao.setBorrowRecordStatus(borrowRecords);
    }

    @Override
    public boolean addBorrowRecords(BorrowRecordDto[] records) {
        List<BorrowRecord> borrowRecords = new ArrayList<>();
        Arrays.stream(records).forEach(record ->
                borrowRecords.add(record.toBookAddRecordModel()));
        List<BorrowRecord> validRecord = validateExistedEmails(borrowRecords);
        LocalDateTime current = LocalDateTime.now();
        borrowRecords.forEach(r -> {
            r.setBorrowDate(current);
            r.setDueDate(current.plusMonths(r.getTimePeriod().getMonthPeriod()));
        });
        if (borrowRecords.size() != validRecord.size()) {
            return false;
        } else {
            return borrowRecordDao.createBorrowRecord(validRecord);
        }
    }

    private List<BorrowRecord> validateExistedEmails(List<BorrowRecord> borrowRecords) {
        List<BorrowRecord> validRecords = new ArrayList<>();
        borrowRecords.forEach(e -> {
            if (readerDao.getReadersByEmail(e.getReader().getEmail()).isPresent()) {
                validRecords.add(e);
            }
        });
        return validRecords;
    }

    @Override
    public boolean changeBorrowStatus(BorrowRecordDto[] records) {
        List<BorrowRecord> borrowRecords = new ArrayList<>();
        Arrays.stream(records).forEach(record ->
                borrowRecords.add(record.toBookEditRecordModel()));
        return borrowRecordDao.updateStatusBorrowRecords(borrowRecords);
    }

}
