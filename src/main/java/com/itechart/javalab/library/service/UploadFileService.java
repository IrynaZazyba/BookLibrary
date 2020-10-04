package com.itechart.javalab.library.service;

import javax.servlet.http.Part;

/**
 * The {@code UploadFileService} interface
 * defines a method for uploading a file
 */
public interface UploadFileService {

    /**
     * Loads user file
     *
     * @param savePath  directory to save file,
     * @param part      file to save,
     * @param nameForDb file name
     */
    void uploadFile(String savePath, Part part, String nameForDb);

}
