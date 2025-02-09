package com.siukatech.poc.spring.security.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/general")
public class GeneralController {

    @GetMapping(value = "/welcome")
    public ResponseEntity<String> getGeneralWelcome() {
        return ResponseEntity.ok("welcome");
    }
}
