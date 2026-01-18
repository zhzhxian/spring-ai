package com.example.springaiaudio.controller;

import com.example.springaiaudio.service.TranscribeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping
public class TranscribeController {

    @Autowired
    private TranscribeService transcribeService;

    @GetMapping("/transcribe")
    public Map<String, String> transcribe() {
        return transcribeService.transcribe();
    }
}
