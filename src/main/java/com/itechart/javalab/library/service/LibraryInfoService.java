package com.itechart.javalab.library.service;

import com.itechart.javalab.library.model.LibraryEmailInfo;

import java.util.Optional;

public interface LibraryInfoService {

    Optional<LibraryEmailInfo> getLibraryInfo();

}
