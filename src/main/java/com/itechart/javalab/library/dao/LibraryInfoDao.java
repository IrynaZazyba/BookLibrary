package com.itechart.javalab.library.dao;

import com.itechart.javalab.library.model.LibraryEmailInfo;

import java.util.Optional;

public interface LibraryInfoDao {

    Optional<LibraryEmailInfo> getLibraryInfo();

    void addLibraryInfo(LibraryEmailInfo info);

    void updateLibraryInfo(LibraryEmailInfo info);
}
