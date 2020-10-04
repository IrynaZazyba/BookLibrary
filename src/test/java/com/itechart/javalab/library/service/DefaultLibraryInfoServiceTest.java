package com.itechart.javalab.library.service;

import com.itechart.javalab.library.dao.LibraryInfoDao;
import com.itechart.javalab.library.dao.impl.SqlLibraryInfoDao;
import com.itechart.javalab.library.dto.LibraryInfoDto;
import com.itechart.javalab.library.domain.entity.LibraryEmailInfo;
import com.itechart.javalab.library.service.impl.DefaultLibraryInfoService;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import java.util.Optional;

import static org.mockito.Mockito.mock;

public class DefaultLibraryInfoServiceTest {

    @Mock
    private LibraryInfoDao mockLibraryInfoDao = mock(SqlLibraryInfoDao.class);
    private final LibraryInfoService libraryInfoService = DefaultLibraryInfoService.getInstance();

    @Test
    public void getLibraryInfoPositive() {
        Whitebox.setInternalState(libraryInfoService, "libraryInfoDao", mockLibraryInfoDao);
        LibraryEmailInfo info = LibraryEmailInfo.builder()
                .id(5).address("street").name("name").signature("regards").build();
        Mockito.when(mockLibraryInfoDao.getLibraryInfo()).thenReturn(Optional.of(info));
        Assert.assertEquals(Optional.of(info), libraryInfoService.getLibraryInfo());
    }

    @Test
    public void getLibraryInfoNegative() {
        Whitebox.setInternalState(libraryInfoService, "libraryInfoDao", mockLibraryInfoDao);
        LibraryEmailInfo info = null;
        Mockito.when(mockLibraryInfoDao.getLibraryInfo()).thenReturn(Optional.ofNullable(info));
        Assert.assertEquals(Optional.ofNullable(info), libraryInfoService.getLibraryInfo());
    }

    @Test
    public void addLibraryInfo() {
        Whitebox.setInternalState(libraryInfoService, "libraryInfoDao", mockLibraryInfoDao);
        LibraryInfoDto infoDto = Mockito.mock(LibraryInfoDto.class);
        LibraryEmailInfo info = Mockito.mock(LibraryEmailInfo.class);
        Mockito.when(infoDto.toModel()).thenReturn(info);
        Mockito.doNothing().when(mockLibraryInfoDao).addLibraryInfo(info);
        libraryInfoService.addLibraryInfo(infoDto);
        Mockito.verify(mockLibraryInfoDao, Mockito.times(1)).addLibraryInfo(info);
    }

    @Test
    public void updateLibraryInfo() {
        Whitebox.setInternalState(libraryInfoService, "libraryInfoDao", mockLibraryInfoDao);
        LibraryInfoDto infoDto = Mockito.mock(LibraryInfoDto.class);
        LibraryEmailInfo info = Mockito.mock(LibraryEmailInfo.class);
        Mockito.when(infoDto.toModel()).thenReturn(info);
        Mockito.doNothing().when(mockLibraryInfoDao).updateLibraryInfo(info);
        libraryInfoService.updateLibraryInfo(infoDto);
        Mockito.verify(mockLibraryInfoDao, Mockito.times(1)).updateLibraryInfo(info);
    }

}
