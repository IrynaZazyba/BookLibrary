package com.itechart.javalab.library.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * The class {@code Paginator} allows to realize pagination,
 * introduce default values to currentPage and recordsPerPages
 * parameters
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paginator {

    private static final int DEFAULT_RECORDS_PER_PAGE = 10;
    private static final int DEFAULT_CURRENT_PAGE = 1;

    private int currentPage;
    private int recordsPerPage;
    private int start;
    private int countPages;

    public Paginator(String recordsPerPage, String currentPage) {
        setCurrentPage(currentPage);
        setRecordsPerPage(recordsPerPage);
        setStart();
    }

    public void setCountPages(int countBooksRecords) {
        int nOfPages = countBooksRecords / recordsPerPage;
        if (countBooksRecords % recordsPerPage > 0) {
            nOfPages++;
        }
        this.countPages = nOfPages;
    }

    private void setStart() {
        this.start = this.currentPage * this.recordsPerPage - this.recordsPerPage;
    }

    private void setCurrentPage(String currentPage) {
        if (currentPage != null && !StringUtils.isEmpty(currentPage)) {
            int parsedValue = Integer.parseInt(currentPage);
            this.currentPage = parsedValue > 0 ? parsedValue : DEFAULT_CURRENT_PAGE;
        } else {
            this.currentPage = DEFAULT_CURRENT_PAGE;
        }
    }

    private void setRecordsPerPage(String recordsPerPage) {
        if (recordsPerPage != null && !StringUtils.isEmpty(recordsPerPage)) {
            int parsedValue = Integer.parseInt(recordsPerPage);
            this.recordsPerPage = parsedValue > 0 ? parsedValue : DEFAULT_RECORDS_PER_PAGE;
        } else {
            this.recordsPerPage = DEFAULT_RECORDS_PER_PAGE;
        }
    }

}

