package com.borjamoll.scrappingMarshalling.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Search {
    private String key;
    private int total;
    private boolean isPrime;
    private boolean save;
    private boolean read;

}
