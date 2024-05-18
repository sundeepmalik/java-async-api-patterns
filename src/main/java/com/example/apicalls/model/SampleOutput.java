package com.example.apicalls.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SampleOutput {
    private String epId;
    private Integer categoryTypecCount;
    private Integer eventNumber;
    private String modelOutput;
}

