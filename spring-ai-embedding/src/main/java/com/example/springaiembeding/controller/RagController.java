package com.example.springaiembeding.controller;

import com.example.springaiembeding.service.RagService;
import com.example.springaiembeding.service.RagService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/rag")
public class RagController {

    @Autowired
    private RagService ragService;
    @Autowired
    private RagService2 ragService2;

    @GetMapping("/ask")
    public Map<String, String> ask(@RequestParam("question") String question) {
        String answer = ragService.answer(question);
        return Map.of("question", question, "answer", answer);
    }

    @GetMapping("/ask2")
    public Map<String, String> ask2(@RequestParam("question") String question) {
        String answer = ragService2.answer(question);
        return Map.of("question", question, "answer", answer);
    }
}
