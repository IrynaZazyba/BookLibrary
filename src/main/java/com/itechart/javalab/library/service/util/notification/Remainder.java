package com.itechart.javalab.library.service.util.notification;

import com.itechart.javalab.library.model.BorrowRecord;
import com.itechart.javalab.library.model.LibraryEmailInfo;
import org.stringtemplate.v4.ST;

import java.time.format.DateTimeFormatter;

abstract class Remainder {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String USER_NAME = "name";
    private static final String BOOK_TITLE = "title";
    private static final String DUE_DATE = "dueDate";
    private static final String ISBN = "isbn";
    private static final String LIBRARY_CONTACT_NAME = "libName";
    private static final String LIBRARY_ADDRESS = "address";
    private static final String LIBRARY_SIGNATURE = "signature";

    void fillNotification(ST notification, LibraryEmailInfo libraryInfo, BorrowRecord record) {
        notification.add(USER_NAME, record.getReader().getName());
        notification.add(BOOK_TITLE, record.getBook().getTitle());
        notification.add(DUE_DATE, record.getDueDate().format(formatter));
        notification.add(ISBN, record.getBook().getISBN());
        notification.add(LIBRARY_CONTACT_NAME, libraryInfo.getName());
        notification.add(LIBRARY_ADDRESS, libraryInfo.getAddress());
        notification.add(LIBRARY_SIGNATURE, libraryInfo.getSignature());
    }
}
