package com.example.springaitoolcalling.controller;

import com.example.springaitoolcalling.tools.MyTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
public class ToolUseController {

    private final ChatClient chatClient;

    public ToolUseController(ChatModel chatModel, MyTools myTools) {
        this.chatClient = ChatClient.builder(chatModel)
                .defaultSystem("你是一个非常有帮助的助手，可以使用工具回复用户的问题")
                .defaultTools(myTools)
                .build();
    }

    @GetMapping("/chat")
    public String chat(@RequestParam("message") String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }
}
