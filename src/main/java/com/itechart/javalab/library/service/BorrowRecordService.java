package com.itechart.javalab.library.service;

import com.itechart.javalab.library.dto.BorrowRecordDto;
import com.itechart.javalab.library.model.BorrowRecord;

import java.util.List;
import java.util.Optional;

public interface BorrowRecordService {

    Optional<List<BorrowRecord>> getBorrowRecords(int bookId);

    boolean addBorrowStatus(BorrowRecordDto[] records);

    boolean addBorrowRecords(BorrowRecordDto[] records);

    boolean changeBorrowStatus(BorrowRecordDto[] records);

}
