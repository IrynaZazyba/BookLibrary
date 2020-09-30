package com.itechart.javalab.library.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailInfo {

    private int id;
    private String name;
    private String signature;
    private String address;
}
