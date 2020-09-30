package com.itechart.javalab.library.service;

import com.itechart.javalab.library.model.EmailInfo;

import java.util.Optional;

public interface LibraryInfoService {

    Optional<EmailInfo> getLibraryInfo();

}
