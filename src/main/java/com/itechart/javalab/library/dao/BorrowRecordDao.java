package com.itechart.javalab.library.dao;

import com.itechart.javalab.library.domain.entity.BorrowRecord;

import java.util.List;
import java.util.Optional;

public interface BorrowRecordDao {

    /**
     * Retrieves borrow records list according to
     *
     * @param bookId book id,
     */
    Optional<List<BorrowRecord>> getBorrowRecords(int bookId);

    /**
     * Sets the status to borrow records,
     * automatically reduces the number of books if needed
     * (statuses lost or returned and damaged see
     * {@link com.itechart.javalab.library.domain.entity.Status})
     * performed transactionally
     *
     * @param borrowRecord list of borrow records,
     */
    boolean setBorrowRecordStatus(List<BorrowRecord> borrowRecord);

    /**
     * Creating borrow records, changing books amount,
     * automatically changing reader name if needed
     * performed transactionally
     *
     * @param borrowRecords list of borrow records,
     */
    boolean createBorrowRecord(List<BorrowRecord> borrowRecords);

    /**
     * Updating the status to borrow records,
     * automatically changing the books amount if needed
     * (status changed from RETURNED to LOST or RETURNED AND DAMAGED
     * and vise a versa
     * {@link com.itechart.javalab.library.domain.entity.Status})
     * performed transactionally
     *
     * @param borrowRecords list of borrow records,
     */
    boolean updateStatusBorrowRecords(List<BorrowRecord> borrowRecords);

    /**
     * Retrieves borrow records info to construct return notification
     * email, select records where the refund is due after 8 days
     */
    List<BorrowRecord> getReturnNotificationInfo();

    /**
     * Retrieves borrow records info to construct delay notification
     * email, select records where the refund is overdue 1 day ago
     */
    List<BorrowRecord> getDelayNotificationInfo();
}
