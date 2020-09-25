package com.itechart.javalab.library.dao;

import com.itechart.javalab.library.model.BorrowRecord;
import com.itechart.javalab.library.model.Reader;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ReaderDao {

    Optional<List<BorrowRecord>> getBorrowRecords(int bookId);

    boolean setBorrowRecordStatus(List<BorrowRecord> borrowRecord);

    boolean createBorrowRecord(List<BorrowRecord> borrowRecords);

    boolean updateStatusBorrowRecords(List<BorrowRecord> borrowRecords);

    Optional<Set<Reader>> getReadersByEmail(String email);

    List<BorrowRecord> getReturnNotificationInfo();

    List<BorrowRecord> getDelayNotificationInfo();
}
