package com.example.springaiquickstart.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.deepseek.DeepSeekChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.function.Function;

@RestController
@RequestMapping("/ai")
public class ChatController {

    @Autowired
    private DeepSeekChatModel chatModel;//与DeepSeek聊天的对象

    @GetMapping("/generate")
    public String generate(@RequestParam(value = "message", defaultValue = "你是谁？") String message) {
        System.out.println("收到消息：" + message);
        //与模型直接对话，调用chatModel.call方法
        String resp = chatModel.call(message);
        System.out.println("返回的内容：" + resp);
        return resp;
    }

    @GetMapping("/generateStream1")
    public Flux<ChatResponse> generateStream1(@RequestParam(value = "message", defaultValue = "你是谁？") String message) {
        System.out.println("收到消息：" + message);
        //与模型直接对话，流式返回内容
        UserMessage userMessage = new UserMessage(message);
        Prompt prompt = new Prompt(userMessage);
        Flux<ChatResponse> stream = chatModel.stream(prompt);
        System.out.println("返回的内容：" + stream);
        return stream;
    }

    @GetMapping("/generateStream2")
    public Flux<String> generateStream2(@RequestParam(value = "message", defaultValue = "你是谁？") String message
            , HttpServletResponse response) {
        //设置响应字符为UTF-8，避免乱码
        response.setCharacterEncoding("UTF-8");
        System.out.println("收到消息：" + message);
        //与模型直接对话，流式返回内容
        UserMessage userMessage = new UserMessage(message);
        Prompt prompt = new Prompt(userMessage);
        Flux<ChatResponse> stream = chatModel.stream(prompt);
        Flux<String> resp = stream.mapNotNull(chatResponse -> chatResponse.getResult().getOutput().getText());
        System.out.println("返回的内容：" + resp);
        return resp;
    }

    @GetMapping("/runtimeOptions")
    public String runtimeOptions(@RequestParam("message") String message
            , @RequestParam(value = "temp", required = false) Double temp) {
        System.out.println("收到消息，message = " + message);

        Prompt prompt;
        if (temp != null) {
            //构建代temperature的DeepSeekChatOptions，覆盖默认的temperature
            DeepSeekChatOptions options = DeepSeekChatOptions.builder()
                    .temperature(temp)
                    .build();
            prompt = new Prompt(message, options);
            System.out.println("运行时覆盖temperature，temp= " + temp);
        } else {
            //无temperature传入的时，使用默认配置
            prompt = new Prompt(message);
            System.out.println("使用默认的temperature");
        }
        ChatResponse chatResponse = chatModel.call(prompt);
        String text = chatResponse.getResult().getOutput().getText();
        System.out.println("模型返回结果:" + text);
        return text;
    }
}
