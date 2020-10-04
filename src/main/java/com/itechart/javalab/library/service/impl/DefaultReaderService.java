package com.itechart.javalab.library.service.impl;

import com.itechart.javalab.library.dao.ReaderDao;
import com.itechart.javalab.library.dao.impl.SqlReaderDao;
import com.itechart.javalab.library.domain.Paginator;
import com.itechart.javalab.library.domain.entity.Reader;
import com.itechart.javalab.library.dto.ReaderDto;
import com.itechart.javalab.library.service.BookService;
import com.itechart.javalab.library.service.ReaderService;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

public class DefaultReaderService implements ReaderService {

    private final ReaderDao readerDao;
    private static volatile DefaultReaderService instance;

    private DefaultReaderService(ReaderDao readerDao) {
        this.readerDao = readerDao;
    }

    public static ReaderService getInstance() {
        if (instance == null) {
            synchronized (BookService.class) {
                if (instance == null) {
                    instance = new DefaultReaderService(SqlReaderDao.getInstance());
                }
            }
        }
        return instance;
    }

    @Override
    public Optional<Set<Reader>> getReadersByEmail(String email) {
        return readerDao.getReadersByEmail(email);
    }

    @Override
    public Optional<Set<Reader>> getReaders(Paginator paginator) {
        return readerDao.getReaders(paginator);
    }

    @Override
    public Optional<Integer> getNumberReadersRecords() {
        return readerDao.getNumberReadersRecords();
    }

    @Override
    public boolean addReader(ReaderDto readerDto) {
        Reader reader = readerDto.toExtendedModel();
        reader.setRegistrationDate(LocalDate.now());
        if (readerDao.getReadersByEmail(reader.getEmail()).isPresent()) {
            return false;
        }
        readerDao.createReader(reader);
        return true;
    }

    @Override
    public boolean editReader(ReaderDto readerDto) {
        Reader reader = readerDto.toExtendedModel();
        if (readerDao.checkExistsEmail(reader.getEmail(), reader.getId()).isPresent()) {
            return false;
        }
        readerDao.updateReader(reader);
        return true;
    }
}
