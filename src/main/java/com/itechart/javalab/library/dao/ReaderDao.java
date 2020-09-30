package com.itechart.javalab.library.dao;

import com.itechart.javalab.library.model.Paginator;
import com.itechart.javalab.library.model.Reader;

import java.util.Optional;
import java.util.Set;

public interface ReaderDao {

    Optional<Set<Reader>> getReaders(Paginator paginator);

    Optional<Integer> getNumberReadersRecords();

    Optional<Set<Reader>> getReadersByEmail(String email);

    void updateReader(Reader reader);

    void createReader(Reader reader);
}
