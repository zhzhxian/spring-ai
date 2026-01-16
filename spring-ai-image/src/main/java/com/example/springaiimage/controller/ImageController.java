package com.example.springaiimage.controller;

import com.example.springaiimage.service.ImageService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.image.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/ai")
public class ImageController {
    @Autowired
    private ImageService imageService;

    @GetMapping("/generateImage")
    public void generateImage(@RequestParam(value = "prompt", defaultValue = "画一只小狗") String prompt
            , HttpServletResponse response) throws IOException {
        Image image = imageService.generateImage(prompt);
        String url = image.getUrl();
        System.out.println(url);
        response.sendRedirect(url);
    }
}
