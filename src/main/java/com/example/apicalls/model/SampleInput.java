package com.example.apicalls.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SampleInput {
    private String epId;
    private Integer categoryTypeCount;
    private Integer eventNumber;
    private String modelInput;
}
