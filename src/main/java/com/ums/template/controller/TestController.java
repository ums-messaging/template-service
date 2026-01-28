package com.ums.template.controller;

import com.ums.template.config.TestProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final TestProperties testProperties;

    @GetMapping("/hello")
    public String test() {
        System.out.println("helloworld");
        return "hello-world";
    }

    @GetMapping("/config/bus")
    public String index() {
        System.out.println("config/bus");
        return testProperties.toString();
    }
}
