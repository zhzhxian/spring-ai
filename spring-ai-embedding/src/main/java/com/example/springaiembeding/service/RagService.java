package com.example.springaiembeding.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class RagService {

    private final EmbeddingModel embeddingModel;//嵌入模型
    private final ChatClient chatClient;//聊天客户端
    private final EmbeddingService embeddingService;

    private List<String> docs = new ArrayList<>();//存储本地文档切分内容
    private List<float[]> vectors = new ArrayList<>();//存储本地文档对应的向量

    public RagService(EmbeddingModel embeddingModel, ChatClient.Builder builder, EmbeddingService embeddingService) throws IOException {
        this.embeddingModel = embeddingModel;
        this.chatClient = builder.build();
        this.embeddingService = embeddingService;
        //加载本地文档
        ClassPathResource resource = new ClassPathResource("古代诗歌常用意象.txt");
        String content = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        //分割文档内容，通过embeding向量化
        String[] split = content.split("----");
        for (String part : split) {
            System.out.println("part = " + part);
            docs.add(part);//存储切分的内容
            vectors.add(this.embeddingModel.embed(part));//将文档片段进行向量化
        }

    }

    /**
     * 对用户输入的问题进行回答
     *
     * @param question
     * @return
     */
    public String answer(String question) {
        //对用户提问的问题向量化
        float[] qv = embeddingModel.embed(question);
        //定义相似度最大的top2 的值
        double v1 = -1;
        double v2 = -1;

        //定义相似度最大的两个向量对应的下标
        int index1 = -1;
        int index2 = -1;

        //将向量化内容与知识库中各个向量进行相似度对比，获取最相似的top2
        for (int i = 0; i < vectors.size(); i++) {
            //两个向量的相似度
            double sim = embeddingService.consineSimilarity(qv, vectors.get(i));
            if (sim > v1) {
                //相似度赋值
                v2 = v1;
                v1 = sim;
                //下标赋值
                index2 = index1;
                index1 = i;
            } else if (sim > v2) {
                v2 = sim;
                index2 = i;
            }
        }

        //获取top2最相似文本内容，拼接在一起作为上下文/prompt提供给LLM
        String ctx = "";
        if (index1 >= 0) {
            ctx += docs.get(index1);
        }
        if (index2 >= 0) {
            ctx += "\n----\n" + docs.get(index2);
        }
        //构建回复
        //准别提示词
        //将获取到的top2文档以提示词内容交给chat大模型 回复
        String prompt = "以下是知识库的内容：\n" + ctx + "\n" + "请基于上述知识库的内容回答用户问题：" + question;
        System.out.println("prompt = " + prompt);
        ChatClient.CallResponseSpec responseSpec = chatClient.prompt()
                .system("你是知识助手结合上下文回答问题")
                .user(prompt)
                .call();
        //获取模型返回的内容
        return responseSpec.content();
    }
}
