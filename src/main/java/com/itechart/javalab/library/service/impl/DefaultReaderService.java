package com.itechart.javalab.library.service.impl;

import com.itechart.javalab.library.dao.ReaderDao;
import com.itechart.javalab.library.dao.impl.SqlReaderDao;
import com.itechart.javalab.library.dto.BorrowRecordDto;
import com.itechart.javalab.library.model.BorrowRecord;
import com.itechart.javalab.library.service.BookService;
import com.itechart.javalab.library.service.ReaderService;

import java.util.ArrayList;
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
    public boolean returnBook(BorrowRecordDto[] records) {
        List<BorrowRecord> borrowRecordList = new ArrayList<>();
        for (BorrowRecordDto borrowRecordDto : records) {
            borrowRecordList.add(BorrowRecord.extractForEditRecord(borrowRecordDto));
        }
        return readerDao.setBorrowRecordStatus(borrowRecordList);
    }


}
