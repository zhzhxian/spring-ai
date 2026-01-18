package com.example.springaiaudio.controller;

import com.example.springaiaudio.service.TtsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
public class TtsController {

    @Autowired
    private TtsService ttsService;

    @GetMapping("/tts")
    public String tts(@RequestParam(value = "text") String text) {
        try {
            String audioPath = ttsService.textToAudio(text);
            System.out.println("audioPath = " + audioPath);
            return "文本转语音完成,文件保存路径：" + audioPath;
        } catch (Exception e) {
            return "文本转语音失败：" + e.getMessage();
        }
    }
}
