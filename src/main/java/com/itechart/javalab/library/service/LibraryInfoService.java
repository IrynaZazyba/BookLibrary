package com.itechart.javalab.library.service;

import com.itechart.javalab.library.dto.LibraryInfoDto;
import com.itechart.javalab.library.model.LibraryEmailInfo;

import java.util.Optional;

public interface LibraryInfoService {

    Optional<LibraryEmailInfo> getLibraryInfo();

    void addLibraryInfo(LibraryInfoDto info);

    void updateLibraryInfo(LibraryInfoDto info);

}
