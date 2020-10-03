package com.itechart.javalab.library.service;

import com.itechart.javalab.library.dao.BorrowRecordDao;
import com.itechart.javalab.library.dao.ReaderDao;
import com.itechart.javalab.library.dao.impl.SqlBorrowRecordDao;
import com.itechart.javalab.library.dao.impl.SqlReaderDao;
import com.itechart.javalab.library.dto.BorrowRecordDto;
import com.itechart.javalab.library.dto.ReaderDto;
import com.itechart.javalab.library.domain.entity.BorrowRecord;
import com.itechart.javalab.library.domain.entity.Reader;
import com.itechart.javalab.library.domain.entity.TimePeriod;
import com.itechart.javalab.library.service.impl.DefaultBorrowRecordService;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import java.util.Optional;

import static org.mockito.Mockito.mock;

public class DefaultBorrowRecordServiceTest {

    @Mock
    private BorrowRecordDao mockBorrowRecordDao = mock(SqlBorrowRecordDao.class);
    @Mock
    private ReaderDao mockReaderDao = mock(SqlReaderDao.class);
    private final BorrowRecordService borrowRecordService = DefaultBorrowRecordService.getInstance();


    @Test
    public void addBorrowStatusPositive() {
        BorrowRecordDto[] borrowRecords = new BorrowRecordDto[1];
        BorrowRecordDto borrowRecordDto = BorrowRecordDto.builder()
                .reader(ReaderDto.builder().email("zazybo1.17@gmail.com").name("Ben Ben").build())
                .bookId(5)
                .timePeriod(TimePeriod.ONE)
                .build();
        borrowRecords[0] = borrowRecordDto;
        Whitebox.setInternalState(borrowRecordService, "borrowRecordDao", mockBorrowRecordDao);
        Mockito.when(mockBorrowRecordDao.setBorrowRecordStatus(Mockito.anyListOf(BorrowRecord.class))).thenReturn(true);
        Assert.assertTrue(borrowRecordService.addBorrowStatus(borrowRecords));
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
        Whitebox.setInternalState(borrowRecordService, "borrowRecordDao", mockBorrowRecordDao);
        Mockito.when(mockBorrowRecordDao.setBorrowRecordStatus(Mockito.anyListOf(BorrowRecord.class))).thenReturn(false);
        Assert.assertFalse(borrowRecordService.addBorrowStatus(borrowRecords));
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
        Whitebox.setInternalState(borrowRecordService, "borrowRecordDao", mockBorrowRecordDao);
        Whitebox.setInternalState(borrowRecordService, "readerDao", mockReaderDao);
        Mockito.when(mockBorrowRecordDao.createBorrowRecord(Mockito.anyListOf(BorrowRecord.class))).thenReturn(true);
        Mockito.when(mockReaderDao.getReadersByEmail("zazybo1.17@gmail.com"))
                .thenReturn(Optional.ofNullable(Mockito.anySetOf(Reader.class)));
        Assert.assertTrue(borrowRecordService.addBorrowRecords(borrowRecords));
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
        Whitebox.setInternalState(borrowRecordService, "readerDao", mockReaderDao);
        Whitebox.setInternalState(borrowRecordService, "borrowRecordDao", mockBorrowRecordDao);
        Mockito.when(mockBorrowRecordDao.createBorrowRecord(Mockito.anyListOf(BorrowRecord.class))).thenReturn(false);
        Mockito.when(mockReaderDao.getReadersByEmail("zazybo1.17@gmail.com"))
                .thenReturn(Optional.ofNullable(null));
        Assert.assertFalse(borrowRecordService.addBorrowRecords(borrowRecords));
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
        Whitebox.setInternalState(borrowRecordService, "borrowRecordDao", mockBorrowRecordDao);
        Mockito.when(mockBorrowRecordDao.updateStatusBorrowRecords(Mockito.anyListOf(BorrowRecord.class))).thenReturn(true);
        Assert.assertTrue(borrowRecordService.changeBorrowStatus(borrowRecords));
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
        Whitebox.setInternalState(borrowRecordService, "borrowRecordDao", mockBorrowRecordDao);
        Mockito.when(mockBorrowRecordDao.updateStatusBorrowRecords(Mockito.anyListOf(BorrowRecord.class))).thenReturn(false);
        Assert.assertFalse(borrowRecordService.changeBorrowStatus(borrowRecords));
    }

}
