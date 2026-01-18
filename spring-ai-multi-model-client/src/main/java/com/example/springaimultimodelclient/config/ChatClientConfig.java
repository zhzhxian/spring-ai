package com.example.springaimultimodelclient.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.zhipuai.ZhiPuAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    //创建DeepSeekChatClient
    @Bean
    public ChatClient deepSeekChatClient(DeepSeekChatModel deepSeekChatModel) {
        return ChatClient.builder(deepSeekChatModel)
                .defaultSystem("你是一个人工智能助手，你的名字叫做小智")
                .build();
    }

    //创建ZhiPuAiChatClient
    @Bean
    public ChatClient zhiPuAiChatClient(ZhiPuAiChatModel zhiPuAiChatModel) {
        return ChatClient.builder(zhiPuAiChatModel)
                .defaultSystem("你是一个人工智能助手，你的名字叫做小智")
                .build();
    }
}
