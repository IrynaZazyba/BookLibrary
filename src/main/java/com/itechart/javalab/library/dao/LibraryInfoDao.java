package com.itechart.javalab.library.dao;

import com.itechart.javalab.library.model.LibraryEmailInfo;

import java.util.Optional;

public interface LibraryInfoDao {

    public Optional<LibraryEmailInfo> getLibraryInfo();

}
