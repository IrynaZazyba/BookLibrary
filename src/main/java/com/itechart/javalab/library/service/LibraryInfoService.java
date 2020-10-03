package com.itechart.javalab.library.service;

import com.itechart.javalab.library.domain.entity.LibraryEmailInfo;
import com.itechart.javalab.library.dto.LibraryInfoDto;

import java.util.Optional;

public interface LibraryInfoService {

    /**
     * Get library info used to construct automatic email
     * from the library, calls a method from dao
     */
    Optional<LibraryEmailInfo> getLibraryInfo();

    /**
     * Add library info used to construct automatic email
     * from the library, calls a method from dao
     */
    void addLibraryInfo(LibraryInfoDto info);

    /**
     * Update library info used to construct automatic email
     * from the library, calls a method from dao
     */
    void updateLibraryInfo(LibraryInfoDto info);

}
