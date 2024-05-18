package com.example.apicalls.controller;

import com.example.apicalls.model.SampleInput;
import com.example.apicalls.model.SampleOutput;
import org.springframework.web.bind.annotation.*;

@RestController
public class GreetingController {

    @PostMapping("/api-category-l1")
    public SampleOutput callCatL1Api(@RequestBody SampleInput inputModel) {
        // Process the input and create the output
        SampleOutput sampleOutput = new SampleOutput();
        sampleOutput.setEpId(inputModel.getEpId());
        sampleOutput.setCategoryTypecCount(inputModel.getCategoryTypeCount());
        sampleOutput.setEventNumber(inputModel.getEventNumber());
        sampleOutput.setModelOutput("Processed Category L1 API result for: " + inputModel.getModelInput()); // Example process

        return sampleOutput;
    }

    @PostMapping("/api-category-l2")
    public SampleOutput callCatL2Api(@RequestBody SampleInput inputModel) {
        // Process the input and create the output
        SampleOutput sampleOutput = new SampleOutput();
        sampleOutput.setEpId(inputModel.getEpId());
        sampleOutput.setCategoryTypecCount(inputModel.getCategoryTypeCount());
        sampleOutput.setEventNumber(inputModel.getEventNumber());
        sampleOutput.setModelOutput("Processed Category L2 API result for: " + inputModel.getModelInput()); // Example process

        return sampleOutput;
    }

    @PostMapping("/api-category-l3")
    public SampleOutput callCatL3Api(@RequestBody SampleInput inputModel) {
        // Process the input and create the output
        SampleOutput sampleOutput = new SampleOutput();
        sampleOutput.setEpId(inputModel.getEpId());
        sampleOutput.setCategoryTypecCount(inputModel.getCategoryTypeCount());
        sampleOutput.setEventNumber(inputModel.getEventNumber());
        sampleOutput.setModelOutput("Processed Category L3 API result for: " + inputModel.getModelInput()); // Example process

        return sampleOutput;
    }

}
