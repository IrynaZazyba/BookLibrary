package com.itechart.javalab.library.service;

import com.itechart.javalab.library.dto.ReaderDto;
import com.itechart.javalab.library.model.Paginator;
import com.itechart.javalab.library.model.Reader;

import java.util.Optional;
import java.util.Set;

public interface ReaderService {

    Optional<Set<Reader>> getReadersByEmail(String email);

    Optional<Set<Reader>> getReaders(Paginator paginator);

    Optional<Integer> getNumberReadersRecords();

    boolean addReader(ReaderDto readerDto);

    void editReader(ReaderDto readerDto);
}
