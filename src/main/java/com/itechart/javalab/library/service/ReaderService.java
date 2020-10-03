package com.itechart.javalab.library.service;

import com.itechart.javalab.library.dto.ReaderDto;
import com.itechart.javalab.library.domain.Paginator;
import com.itechart.javalab.library.domain.entity.Reader;

import java.util.Optional;
import java.util.Set;

public interface ReaderService {

    /**
     * Get readers according to params
     *
     * @param email reader email
     */
    Optional<Set<Reader>> getReadersByEmail(String email);

    /**
     * Get readers according to params
     *
     * @param paginator see{@link Paginator}
     */
    Optional<Set<Reader>> getReaders(Paginator paginator);

    /**
     * Get number books` records, used to define count
     * pages in {@link Paginator}
     */
    Optional<Integer> getNumberReadersRecords();

    /**
     * Adding readers to the system, transform dto object to
     * entity and setting registration date like current Date,
     * calls a method from dao
     *
     * @param readerDto {@link ReaderDto}
     */
    boolean addReader(ReaderDto readerDto);

    /**
     * Adding readers to the system, transform dto object to
     * entity, calls a method from dao
     *
     * @param readerDto {@link ReaderDto}
     */
    void editReader(ReaderDto readerDto);
}
