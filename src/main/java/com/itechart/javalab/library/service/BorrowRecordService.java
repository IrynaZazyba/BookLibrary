package com.itechart.javalab.library.service;

import com.itechart.javalab.library.domain.entity.BorrowRecord;
import com.itechart.javalab.library.dto.BorrowRecordDto;

import java.util.List;
import java.util.Optional;

public interface BorrowRecordService {

    /**
     * Get borrow records, calls a method from dao
     *
     * @param bookId book id
     */
    Optional<List<BorrowRecord>> getBorrowRecords(int bookId);

    /**
     * Transforming dto object to entity, setting return date like
     * current DateTime, calls a method from dao
     *
     * @param records array of borrow records dto
     */
    boolean addBorrowStatus(BorrowRecordDto[] records);

    /**
     * Transforming dto object to entity, setting borrow date like
     * current DateTime, setting due date like borrow date plus borrow
     * record time period, prevents the possibility of creating a
     * borrowed record for a non-existent user, calls a method from dao
     *
     * @param records array of borrow records dto
     */
    boolean addBorrowRecords(BorrowRecordDto[] records);

    /**
     * Transforming dto object to entity, calls a method from dao
     *
     * @param records array of borrow records dto
     */
    boolean changeBorrowStatus(BorrowRecordDto[] records);

}
