package com.itechart.javalab.library.service.impl;

import com.itechart.javalab.library.dao.LibraryInfoDao;
import com.itechart.javalab.library.dao.impl.SqlLibraryInfoDao;
import com.itechart.javalab.library.dto.LibraryInfoDto;
import com.itechart.javalab.library.domain.entity.LibraryEmailInfo;
import com.itechart.javalab.library.service.LibraryInfoService;

import java.util.Optional;

public class DefaultLibraryInfoService implements LibraryInfoService {

    private final LibraryInfoDao libraryInfoDao;
    private static volatile DefaultLibraryInfoService instance;

    private DefaultLibraryInfoService(LibraryInfoDao libraryInfoDao) {
        this.libraryInfoDao = libraryInfoDao;
    }

    public static LibraryInfoService getInstance() {
        if (instance == null) {
            synchronized (DefaultLibraryInfoService.class) {
                if (instance == null) {
                    instance = new DefaultLibraryInfoService(SqlLibraryInfoDao.getInstance());
                }
            }
        }
        return instance;
    }

    @Override
    public Optional<LibraryEmailInfo> getLibraryInfo() {
        return libraryInfoDao.getLibraryInfo();
    }

    @Override
    public void addLibraryInfo(LibraryInfoDto info) {
        libraryInfoDao.addLibraryInfo(info.toModel());
    }

    @Override
    public void updateLibraryInfo(LibraryInfoDto info) {
        libraryInfoDao.updateLibraryInfo(info.toModel());
    }
}
