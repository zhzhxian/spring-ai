package com.example.springaimultimodelclient.controller;

import com.example.springaimultimodelclient.pojo.IdCardInfo;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
public class ImgAnalyController {

    @Autowired
    @Qualifier("zhiPuAiChatClient")
    private ChatClient zhiPuAiChatClient;

    @GetMapping("/imgAnalazy")
    public String imgAnalazy() {
        ClassPathResource resource = new ClassPathResource("img.png");
        return zhiPuAiChatClient.prompt("你是一个图像识别的专家，可以对用户关于图片识别问题进行精准回复")
                .user(promptUserSpec -> promptUserSpec.text("请对图片进行描述")
                        .media(MediaType.IMAGE_PNG, resource))
                .call()
                .content();
    }

    @GetMapping("/idCardAnalazy")
    public IdCardInfo idCardAnalazy() {
        ClassPathResource resource = new ClassPathResource("身份证.jpg");
        return zhiPuAiChatClient.prompt("你是一个图像识别的专家，可以对用户关于图片识别问题进行精准回复," +
                        "以json格式输出识别到的身份证中的姓名（name），性别（gender），民族（nation，出生日期（birth），住址（address），身份正号(idNo)")
                .user(promptUserSpec -> promptUserSpec.text("请对图片进行描述")
                        .media(MediaType.IMAGE_JPEG, resource))
                .call()
                .entity(IdCardInfo.class);
    }
}
