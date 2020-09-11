package com.itechart.javalab.library.dto;

import com.itechart.javalab.library.model.Book;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookPageDto {

    private Book book;


}
