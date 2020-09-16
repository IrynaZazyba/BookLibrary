package com.itechart.javalab.library.controller.util;

import com.itechart.javalab.library.dto.BorrowRecordDto;
import com.itechart.javalab.library.model.Status;

public class BorrowRecordValidator {

    private BorrowRecordValidator() {
    }

    public static boolean validateEditRecord(BorrowRecordDto borrowRecord) {
        return validateStatus(borrowRecord.getStatus());
    }

    private static boolean validateStatus(String status) {
        status = status.replace(" ", "_").toUpperCase();
        try {
            Status.valueOf(status);
        } catch (IllegalArgumentException ex) {
            return false;
        }
        return true;
    }

}
