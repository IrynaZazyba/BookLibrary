package com.itechart.javalab.library.dto;

import com.itechart.javalab.library.domain.entity.LibraryEmailInfo;
import lombok.*;
import org.apache.commons.text.StringEscapeUtils;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LibraryInfoDto {

    private int id;
    private String name;
    private String address;
    private String signature;

    public LibraryEmailInfo toModel() {
        return LibraryEmailInfo.builder()
                .id(id)
                .name(StringEscapeUtils.escapeHtml4(name))
                .address(StringEscapeUtils.escapeHtml4(address))
                .signature(StringEscapeUtils.escapeHtml4(signature))
                .build();
    }

}
