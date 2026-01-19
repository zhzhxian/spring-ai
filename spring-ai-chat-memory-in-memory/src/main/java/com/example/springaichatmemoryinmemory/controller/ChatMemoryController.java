package com.example.springaichatmemoryinmemory.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
public class ChatMemoryController {

    @Autowired
    private ChatClient chatClient;

    @GetMapping("/chatMemory")
    public String chatMemory(@RequestParam("conversationId") String conversationId
            , @RequestParam("message") String message) {
        return chatClient.prompt()
                .user(message)
                //加入聊天的会话
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();
    }
}
