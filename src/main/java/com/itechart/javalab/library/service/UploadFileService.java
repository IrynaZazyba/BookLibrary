package com.itechart.javalab.library.service;

import javax.servlet.http.Part;

public interface UploadFileService {

    void uploadFile(String savePath, Part part, String nameForDb);

}
