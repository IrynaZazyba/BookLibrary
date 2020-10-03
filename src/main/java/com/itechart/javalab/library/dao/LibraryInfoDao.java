package com.itechart.javalab.library.dao;

import com.itechart.javalab.library.domain.entity.LibraryEmailInfo;

import java.util.Optional;

public interface LibraryInfoDao {

    /**
     * Retrieves info used to build automatic emails
     * from library
     */
    Optional<LibraryEmailInfo> getLibraryInfo();

    /**
     * Create info used to build automatic emails
     * from library
     *
     * @param info see{@link LibraryEmailInfo}
     */
    void addLibraryInfo(LibraryEmailInfo info);

    /**
     * Update info used to build automatic emails
     * from library
     *
     * @param info see{@link LibraryEmailInfo}
     */
    void updateLibraryInfo(LibraryEmailInfo info);
}
