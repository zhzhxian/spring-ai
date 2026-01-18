package com.example.springaiollama.service;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmbeddingService {
    private final EmbeddingModel embeddingModel;

    private final List<float[]> docVectors;

    // 1. 准备知识库文本内容
    private final List<String> docs = List.of("美食非常美味，服务员也很友好。", "这部电影既刺激又令人兴奋。", "阅读书籍是扩展知识的好方法。");

    public EmbeddingService(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
        //对知识库文本进行向量化
        this.docVectors = this.embeddingModel.embed(docs);
    }

    public String queryBastMatch(String query) {
        //对用户传入的query进行向量化
        float[] queryVector = embeddingModel.embed(query);
        double similarity = -1;
        int bestMatchIndex = -1;
        //遍历 docVectors 与用户传入的文本计算相似度，找出一个最相似的返回
        for (int i = 0; i < docVectors.size(); i++) {
            float[] docVector = docVectors.get(i);
            //计算余玄相似度
            double temp = consineSimilarity(queryVector, docVector);
            if (temp > similarity) {
                similarity = temp;
                bestMatchIndex = i;
            }
        }
        return docs.get(bestMatchIndex);
    }

    /**
     * 余玄相似度计算
     *
     * @param a
     * @param b
     * @return
     */
    public double consineSimilarity(float[] a, float[] b) {
        double dot = 0;
        double na = 0;
        double nb = 0;
        for (int i = 0; i < a.length; i++) {
            dot += a[i] * b[i];
            na += a[i] * a[i];
            nb += b[i] * b[i];
        }
        return dot / (Math.sqrt(na) / Math.sqrt(nb));
    }
}
