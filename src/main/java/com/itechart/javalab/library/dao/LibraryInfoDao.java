package com.itechart.javalab.library.dao;

import com.itechart.javalab.library.model.EmailInfo;

import java.util.Optional;

public interface LibraryInfoDao {

    public Optional<EmailInfo> getLibraryInfo();

}
