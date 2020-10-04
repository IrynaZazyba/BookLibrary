package com.itechart.javalab.library.dao;

import com.itechart.javalab.library.domain.Paginator;
import com.itechart.javalab.library.domain.entity.Reader;

import java.util.Optional;
import java.util.Set;

public interface ReaderDao {

    /**
     * Retrieves readers according to params
     *
     * @param paginator see {@link Paginator},
     */
    Optional<Set<Reader>> getReaders(Paginator paginator);

    /**
     * Retrieves number readers` records,
     * used to define count pages in {@link Paginator}
     */
    Optional<Integer> getNumberReadersRecords();

    /**
     * Retrieves readers according to param
     *
     * @param email reader email
     */
    Optional<Set<Reader>> getReadersByEmail(String email);

    /**
     * Update reader info
     *
     * @param reader see{@link Reader}
     */
    void updateReader(Reader reader);

    /**
     * Create reader
     *
     * @param reader see{@link Reader}
     */
    void createReader(Reader reader);
}
