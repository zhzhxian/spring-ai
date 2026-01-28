package com.example.springairagwithadvisor.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ai")
public class RagController {
    @Autowired
    private ChatClient chatClient;
    @Autowired
    private QuestionAnswerAdvisor questionAnswerAdvisor;
    @Autowired
    private RetrievalAugmentationAdvisor retrievalAugmentationAdvisor;


    @GetMapping("/chat1")
    public String ask1(@RequestParam String message) {
        return chatClient.prompt().user(message)
                //设置使用 QuestionAnswerAdvisor
                .advisors(List.of(questionAnswerAdvisor))
                .call().content();
    }

    @GetMapping("/chat2")
    public String ask2(@RequestParam String message) {
        return chatClient.prompt().user(message)
                //设置使用 RetrievalAugmentationAdvisor
                .advisors(List.of(retrievalAugmentationAdvisor))
                .call().content();
    }
}
