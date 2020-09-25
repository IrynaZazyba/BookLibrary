package com.itechart.javalab.library.service;

import com.itechart.javalab.library.dto.BorrowRecordDto;
import com.itechart.javalab.library.model.BorrowRecord;
import com.itechart.javalab.library.model.Paginator;
import com.itechart.javalab.library.model.Reader;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ReaderService {

    Optional<List<BorrowRecord>> getBorrowRecords(int bookId);

    boolean addBorrowStatus(BorrowRecordDto[] records);

    boolean addBorrowRecords(BorrowRecordDto[] records);

    boolean changeBorrowStatus(BorrowRecordDto[] records);

    Optional<Set<Reader>> getReadersByEmail(String email);

    Optional<Set<Reader>> getReaders(Paginator paginator);

    Optional<Integer> getNumberReadersRecords();
}
