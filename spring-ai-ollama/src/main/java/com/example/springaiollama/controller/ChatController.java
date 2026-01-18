package com.example.springaiollama.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/ai")
public class ChatController {

    @Autowired
    private OllamaChatModel ollamaChatModel;

    @GetMapping("/chat")
    public String chat(@RequestParam("message") String message) {
        return ollamaChatModel.call(message);
    }

    @GetMapping("/streamChat")
    public Flux<String> streamChat(@RequestParam("message") String message, HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        Prompt prompt = Prompt.builder()
                .messages(UserMessage.builder().text(message).build())
                .build();
        Flux<ChatResponse> chatResponseFlux = ollamaChatModel.stream(prompt);
        return chatResponseFlux.mapNotNull(chatResponse -> chatResponse.getResult().getOutput().getText());
    }


    @GetMapping("/runtimeOptions")
    public Flux<String> runtimeOptions(@RequestParam("message") String message
            , @RequestParam("temp") Double temp
            , HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        Prompt prompt = Prompt.builder()
                .messages(UserMessage.builder()
                        .text(message)
                        .build())
                .chatOptions(ChatOptions.builder()
                        .temperature(temp)
                        .build())
                .build();
        Flux<ChatResponse> chatResponseFlux = ollamaChatModel.stream(prompt);
        return chatResponseFlux.mapNotNull(chatResponse -> chatResponse.getResult().getOutput().getText());
    }
}
