package com.itechart.javalab.library.model;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
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
        if (currentPage != null) {
            int parsedValue = Integer.parseInt(currentPage);
            this.currentPage = parsedValue > 0 ? parsedValue : DEFAULT_CURRENT_PAGE;
        } else {
            this.currentPage = DEFAULT_CURRENT_PAGE;
        }
    }

    private void setRecordsPerPage(String recordsPerPage) {
        if (recordsPerPage != null) {
            int parsedValue = Integer.parseInt(recordsPerPage);
            this.recordsPerPage = parsedValue > 0 ? parsedValue : DEFAULT_RECORDS_PER_PAGE;
        } else {
            this.recordsPerPage = DEFAULT_RECORDS_PER_PAGE;
        }
    }

}

