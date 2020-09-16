package com.itechart.javalab.library.controller.util;

import com.itechart.javalab.library.dto.BorrowRecordDto;
import com.itechart.javalab.library.model.Status;
import com.itechart.javalab.library.model.TimePeriod;

public class BorrowRecordValidator {

    private BorrowRecordValidator() {
    }

    public static boolean validateEditRecord(BorrowRecordDto borrowRecord) {
        return validateStatus(borrowRecord.getStatus());
    }

    public static boolean validateAddRecord(BorrowRecordDto borrowRecord) {
        boolean validate = ReaderValidator.validate(borrowRecord.getReader());
        return validate && validateTimePeriod(borrowRecord.getTimePeriod());
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

    private static boolean validateTimePeriod(String period) {
        try {
            int i = Integer.parseInt(period);
            return TimePeriod.containsMonthPeriod(i);
        } catch (NumberFormatException ex) {
            return false;
        }
    }
}
