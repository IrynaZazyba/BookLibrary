package com.itechart.javalab.library.service;

import com.itechart.javalab.library.dto.BorrowRecordDto;
import com.itechart.javalab.library.model.BorrowRecord;

import java.util.List;
import java.util.Optional;

public interface ReaderService {

    Optional<List<BorrowRecord>> getBorrowRecords(int bookId);


    boolean returnBook(BorrowRecordDto[] records);

    boolean lendBook(BorrowRecordDto[] records);

    boolean changeBorrowStatus(BorrowRecordDto[] records);
}
