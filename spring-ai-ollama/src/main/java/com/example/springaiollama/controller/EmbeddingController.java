package com.example.springaiollama.controller;

import com.example.springaiollama.service.EmbeddingService;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/ai")
public class EmbeddingController {

    @Autowired
    private EmbeddingModel embeddingModel;

    @Autowired
    private EmbeddingService embeddingService;

    @GetMapping("/embedding")
    public Map<String, Object> embed(@RequestParam(value = "message", defaultValue = "给我讲个笑话") String message) {
        float[] vector = embeddingModel.embed(message);
        System.out.println("向量维度：" + vector.length);
        return Map.of("message", message, "vector", vector);
    }
    @GetMapping("/similarity")
    public String similarity(@RequestParam(value = "query") String query) {
        //找出相似的文本
        return embeddingService.queryBastMatch(query);
    }
}
