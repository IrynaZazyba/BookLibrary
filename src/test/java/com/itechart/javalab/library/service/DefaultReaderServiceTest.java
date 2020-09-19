package com.itechart.javalab.library.service;

import com.itechart.javalab.library.dao.ReaderDao;
import com.itechart.javalab.library.dao.impl.SqlReaderDao;
import com.itechart.javalab.library.dto.BorrowRecordDto;
import com.itechart.javalab.library.dto.ReaderDto;
import com.itechart.javalab.library.model.BorrowRecord;
import com.itechart.javalab.library.model.TimePeriod;
import com.itechart.javalab.library.service.impl.DefaultReaderService;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import static org.mockito.Mockito.mock;

public class DefaultReaderServiceTest {

    @Mock
    private ReaderDao mockReaderDao = mock(SqlReaderDao.class);
    private ReaderService readerService = DefaultReaderService.getInstance();


    @Test
    public void addBorrowStatusPositive() {
        BorrowRecordDto[] borrowRecords = new BorrowRecordDto[1];
        BorrowRecordDto borrowRecordDto = BorrowRecordDto.builder()
                .reader(ReaderDto.builder().email("zazybo1.17@gmail.com").name("Ben Ben").build())
                .bookId(5)
                .timePeriod(TimePeriod.ONE)
                .build();
        borrowRecords[0] = borrowRecordDto;
        Whitebox.setInternalState(readerService, "readerDao", mockReaderDao);
        Mockito.when(mockReaderDao.setBorrowRecordStatus(Mockito.anyListOf(BorrowRecord.class))).thenReturn(true);
        Assert.assertTrue(readerService.addBorrowStatus(borrowRecords));
    }

    @Test
    public void addBorrowStatusNegative() {
        BorrowRecordDto[] borrowRecords = new BorrowRecordDto[1];
        BorrowRecordDto borrowRecordDto = BorrowRecordDto.builder()
                .reader(ReaderDto.builder().email("zazybo1.17@gmail.com").name("Ben Ben").build())
                .bookId(5)
                .timePeriod(TimePeriod.ONE)
                .build();
        borrowRecords[0] = borrowRecordDto;
        Whitebox.setInternalState(readerService, "readerDao", mockReaderDao);
        Mockito.when(mockReaderDao.setBorrowRecordStatus(Mockito.anyListOf(BorrowRecord.class))).thenReturn(false);
        Assert.assertFalse(readerService.addBorrowStatus(borrowRecords));
    }

    @Test
    public void addBorrowRecordsPositive() {
        BorrowRecordDto[] borrowRecords = new BorrowRecordDto[1];
        BorrowRecordDto borrowRecordDto = BorrowRecordDto.builder()
                .reader(ReaderDto.builder().email("zazybo1.17@gmail.com").name("Ross").build())
                .bookId(7)
                .timePeriod(TimePeriod.TWO)
                .build();
        borrowRecords[0] = borrowRecordDto;
        Whitebox.setInternalState(readerService, "readerDao", mockReaderDao);
        Mockito.when(mockReaderDao.createBorrowRecord(Mockito.anyListOf(BorrowRecord.class))).thenReturn(true);
        Assert.assertTrue(readerService.addBorrowRecords(borrowRecords));
    }

    @Test
    public void addBorrowRecordsNegative() {
        BorrowRecordDto[] borrowRecords = new BorrowRecordDto[1];
        BorrowRecordDto borrowRecordDto = BorrowRecordDto.builder()
                .reader(ReaderDto.builder().email("zazybo1.17@gmail.com").name("Ross").build())
                .bookId(7)
                .timePeriod(TimePeriod.TWO)
                .build();
        borrowRecords[0] = borrowRecordDto;
        Whitebox.setInternalState(readerService, "readerDao", mockReaderDao);
        Mockito.when(mockReaderDao.createBorrowRecord(Mockito.anyListOf(BorrowRecord.class))).thenReturn(false);
        Assert.assertFalse(readerService.addBorrowRecords(borrowRecords));
    }

    @Test
    public void changeBorrowStatusPositive() {
        BorrowRecordDto[] borrowRecords = new BorrowRecordDto[1];
        BorrowRecordDto borrowRecordDto = BorrowRecordDto.builder()
                .reader(ReaderDto.builder().email("zazybo1.17@gmail.com").name("Ross").build())
                .bookId(7)
                .timePeriod(TimePeriod.TWO)
                .build();
        borrowRecords[0] = borrowRecordDto;
        Whitebox.setInternalState(readerService, "readerDao", mockReaderDao);
        Mockito.when(mockReaderDao.updateStatusBorrowRecords(Mockito.anyListOf(BorrowRecord.class))).thenReturn(true);
        Assert.assertTrue(readerService.changeBorrowStatus(borrowRecords));
    }

    @Test
    public void changeBorrowStatusNegative() {
        BorrowRecordDto[] borrowRecords = new BorrowRecordDto[1];
        BorrowRecordDto borrowRecordDto = BorrowRecordDto.builder()
                .reader(ReaderDto.builder().email("zazybo1.17@gmail.com").name("Ross").build())
                .bookId(7)
                .timePeriod(TimePeriod.TWO)
                .build();
        borrowRecords[0] = borrowRecordDto;
        Whitebox.setInternalState(readerService, "readerDao", mockReaderDao);
        Mockito.when(mockReaderDao.updateStatusBorrowRecords(Mockito.anyListOf(BorrowRecord.class))).thenReturn(false);
        Assert.assertFalse(readerService.changeBorrowStatus(borrowRecords));
    }


}
