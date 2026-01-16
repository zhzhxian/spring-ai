package com.example.springaiimage.service;

import org.springframework.ai.image.*;
import org.springframework.ai.zhipuai.ZhiPuAiImageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageService {

    @Autowired
    private ZhiPuAiImageModel zhiPuAiImageModel;

    public Image generateImage(String prompt) {
        ImageOptions imageOptions = ImageOptionsBuilder.builder()
                .N(1)//图片数量
                .width(512)//图片宽度
                .height(512)//图片高度
                .build();
        ImagePrompt imagePrompt = new ImagePrompt(List.of(new ImageMessage(prompt)), imageOptions);
        ImageResponse imageResponse = zhiPuAiImageModel.call(imagePrompt);
        return imageResponse.getResult().getOutput();
    }
}
