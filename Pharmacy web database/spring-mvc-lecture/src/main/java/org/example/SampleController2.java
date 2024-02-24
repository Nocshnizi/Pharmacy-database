package org.example;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SampleController2 {

    @GetMapping(value = "/sample2", produces = MediaType.APPLICATION_JSON_VALUE)
    public SampleResponse getSample() {
        return new SampleResponse("Get succeeded");
    }

    @PostMapping(value = "/sample2", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SampleResponse> postSample(@RequestBody SampleRequest request) {
        return List.of(new SampleResponse(
                "Post succeeded, name: " + request.getFk()));
    }
}
