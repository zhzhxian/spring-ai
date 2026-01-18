package com.example.springaimultimodelclient.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
public class MultiModelController {

    private ChatClient deepSeekChatClient;
    private ChatClient zhiPuAiChatClient;

    public MultiModelController(@Qualifier("deepSeekChatClient") ChatClient deepSeekChatClient
            , @Qualifier("zhiPuAiChatClient") ChatClient zhiPuAiChatClient) {
        this.deepSeekChatClient = deepSeekChatClient;
        this.zhiPuAiChatClient = zhiPuAiChatClient;
    }

    @GetMapping("/deepseek")
    public String chatWithDeepSeek(@RequestParam("message") String message) {
        return deepSeekChatClient.prompt()
                .user(message)
                .call()
                .content();
    }

    @GetMapping("/zhipu")
    public String chatWithZhiPuAi(@RequestParam("message") String message) {
        return zhiPuAiChatClient.prompt()
                .user(message)
                .call()
                .content();
    }
}
