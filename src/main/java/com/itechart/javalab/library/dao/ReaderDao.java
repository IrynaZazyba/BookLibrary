package com.itechart.javalab.library.dao;

import com.itechart.javalab.library.model.BorrowRecord;

import java.util.List;
import java.util.Optional;

public interface ReaderDao {

    Optional<List<BorrowRecord>> getBorrowRecords(int bookId);

    boolean setBorrowRecordStatus(List<BorrowRecord> borrowRecord);

    boolean createBorrowRecord(List<BorrowRecord> borrowRecords);
}
