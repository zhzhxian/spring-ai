package com.example.springaichatclientdemo.controller;

import com.example.springaichatclientdemo.pojo.Student;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/ai")
public class ChatClientController {

    private final ChatClient chatClient;

    public ChatClientController(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultSystem("你是一个聊天助手，名字叫做小智")
                .build();
    }

    //对话
    @GetMapping("/chat")
    public String chat(@RequestParam("message") String message) {
        return chatClient.prompt("如果用户让你讲故事，只能讲解神话故事，不能讲解其他类型的故事")
                .user(message)
                //聊天记忆，切片
                //.advisors()
                .call()
                .content();
    }

    @GetMapping("/chatResp")
    public ChatResponse chatResp(@RequestParam("message") String message) {

        return chatClient.prompt("如果用户让你讲故事，只能讲解神话故事，不能讲解其他类型的故事")
                .user(message)
                //聊天记忆，切片
                //.advisors()
                .call()
                .chatResponse();
    }

    @GetMapping("/chatStream")
    public Flux<String> chatStream(@RequestParam("message") String message
            , HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");

        return chatClient.prompt("如果用户让你讲故事，只能讲解神话故事，不能讲解其他类型的故事")
                .user(message)
                //聊天记忆，切片
                //.advisors()
                .stream()
                .content();
    }

    @GetMapping("/getOneStudent")
    public Student getOneStudent() {
        return chatClient.prompt("严格按照用户要求返回数据")
                .user("生成一个Student对象，输出单个JSON格式对象，字段有：id(Long)，name(String)，age(Integer)")
                .call()
                .entity(Student.class);
    }

    @GetMapping("/getOneStudentList")
    public List<Student> getOneStudentList() {
        return chatClient.prompt("严格按照用户要求返回数据")
                .user("生成3个Student对象，输出单个JSON格式对象，字段有：id(Long)，name(String)，age(Integer)")
                .call()
                .entity(new ParameterizedTypeReference<List<Student>>() {
                });
    }
}
