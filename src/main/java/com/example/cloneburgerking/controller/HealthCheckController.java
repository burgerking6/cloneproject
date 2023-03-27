package com.example.cloneburgerking.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class HealthCheckController {

    @GetMapping("/health")
    public ResponseEntity healthcheck(){
        return ResponseEntity.ok("healthcheck 성공");
    }

}