package com.itechart.javalab.library.service.impl;

import com.itechart.javalab.library.service.UploadFileService;
import com.itechart.javalab.library.service.exception.UploadFileRuntimeException;
import lombok.extern.log4j.Log4j2;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;

@Log4j2
public class DefaultUploadFileService implements UploadFileService {

    private static DefaultUploadFileService instance;

    private DefaultUploadFileService() {
    }

    public static DefaultUploadFileService getInstance() {
        if (instance == null) {
            synchronized (DefaultUploadFileService.class) {
                if (instance == null) {
                    instance = new DefaultUploadFileService();
                }
            }
        }
        return instance;
    }

    public void uploadFile(String savePath, Part part, String nameForDb) {
        try {
            File dir = new File(savePath);
            if (!dir.exists()) {
                dir.mkdir();
            }
            part.write(savePath + File.separator + nameForDb);
        } catch (IOException e) {
            log.error("Error with write file to the directory", e);
            throw new UploadFileRuntimeException("Impossible to write file");
        }
    }
}
