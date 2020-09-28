package com.itechart.javalab.library.service;

import com.itechart.javalab.library.dao.ReaderDao;
import com.itechart.javalab.library.dao.impl.SqlReaderDao;
import com.itechart.javalab.library.dto.ReaderDto;
import com.itechart.javalab.library.model.Paginator;
import com.itechart.javalab.library.model.Reader;
import com.itechart.javalab.library.service.impl.DefaultReaderService;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

public class DefaultReaderServiceTest {

    @Mock
    private ReaderDao mockReaderDao = mock(SqlReaderDao.class);
    private final ReaderService readerService = DefaultReaderService.getInstance();

    @Test
    public void getReadersByEmailPositive() {
        Set<Reader> readers = Mockito.anySetOf(Reader.class);
        String email = "zazybo1.17@gmail.com";
        Whitebox.setInternalState(readerService, "readerDao", mockReaderDao);
        Mockito.when(mockReaderDao.getReadersByEmail(email)).thenReturn(Optional.of(readers));
        Assert.assertEquals(Optional.of(readers), readerService.getReadersByEmail(email));
    }

    @Test
    public void getReadersByEmailNegative() {
        Set<Reader> readers = null;
        String email = "zazybo1.17@gmail.com";
        Whitebox.setInternalState(readerService, "readerDao", mockReaderDao);
        Mockito.when(mockReaderDao.getReadersByEmail(email)).thenReturn(Optional.ofNullable(readers));
        Assert.assertEquals(Optional.ofNullable(readers), readerService.getReadersByEmail(email));
    }

    @Test
    public void getReaders() {
        Set<Reader> readers = Mockito.anySetOf(Reader.class);
        Paginator paginator = new Paginator("2", "1");
        Whitebox.setInternalState(readerService, "readerDao", mockReaderDao);
        Mockito.when(mockReaderDao.getReaders(paginator)).thenReturn(Optional.of(readers));
        Assert.assertEquals(Optional.of(readers), readerService.getReaders(paginator));
    }

    @Test
    public void getNumberReadersRecords() {
        Whitebox.setInternalState(readerService, "readerDao", mockReaderDao);
        Mockito.when(mockReaderDao.getNumberReadersRecords()).thenReturn(Optional.of(2));
        Assert.assertEquals(Optional.of(2), readerService.getNumberReadersRecords());
    }

    @Test
    public void addReaderNegative() {
        Whitebox.setInternalState(readerService, "readerDao", mockReaderDao);
        ReaderDto readerDto = Mockito.mock(ReaderDto.class);
        Reader reader = Mockito.mock(Reader.class);
        Mockito.when(readerDto.toExtendedModel()).thenReturn(reader);
        Set<Reader> readers = new HashSet<>();
        readers.add(Reader.builder().id(5).build());
        Mockito.when(mockReaderDao.getReadersByEmail(reader.getEmail())).thenReturn(Optional.of(readers));
        doNothing().when(mockReaderDao).createReader(reader);
        Assert.assertFalse(readerService.addReader(readerDto));
    }

    @Test
    public void addReaderPositive() {
        Whitebox.setInternalState(readerService, "readerDao", mockReaderDao);
        ReaderDto readerDto = Mockito.mock(ReaderDto.class);
        Reader reader = Mockito.mock(Reader.class);
        Mockito.when(readerDto.toExtendedModel()).thenReturn(reader);
        Set<Reader> readers = null;
        Mockito.when(mockReaderDao.getReadersByEmail(reader.getEmail())).thenReturn(Optional.ofNullable(readers));
        doNothing().when(mockReaderDao).createReader(reader);
        Assert.assertTrue(readerService.addReader(readerDto));
    }

    @Test
    public void editReader() {
        Whitebox.setInternalState(readerService, "readerDao", mockReaderDao);
        ReaderDto readerDto = Mockito.mock(ReaderDto.class);
        Reader reader = Mockito.mock(Reader.class);
        Mockito.when(readerDto.toExtendedModel()).thenReturn(reader);
        doNothing().when(mockReaderDao).updateReader(reader);
        readerService.editReader(readerDto);
        Mockito.verify(mockReaderDao, Mockito.times(1)).updateReader(reader);
    }
}
