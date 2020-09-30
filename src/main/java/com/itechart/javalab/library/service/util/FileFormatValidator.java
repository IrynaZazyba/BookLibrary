package com.itechart.javalab.library.service.util;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * The FileFormatValidator class
 * for checking the format of the downloaded files. Limited
 * to FileFormat enumeration values.
 */
@Log4j2
public class FileFormatValidator {

    private static FileFormatValidator instance;

    private FileFormatValidator() {
    }

    public static FileFormatValidator getInstance() {
        if (instance == null) {
            synchronized (FileFormatValidator.class) {
                if (instance == null) {
                    instance = new FileFormatValidator();
                }
            }
        }
        return instance;
    }

    public void validate(String fileName) {
        if (StringUtils.isNotEmpty(fileName)) {
            String fileFormat = FilenameUtils.getExtension(fileName);
            FileFormat.valueOf(fileFormat.toUpperCase());
        }
    }

}
