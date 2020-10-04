package com.itechart.javalab.library.dto;

import com.itechart.javalab.library.domain.entity.Reader;
import lombok.*;

import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReaderPageDto {

    private Set<Reader> readers;
    private int recordsPerPage;
    private int currentPage;
    private int countPages;

}
