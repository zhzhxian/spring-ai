package com.example.springaiembeding.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class RagService2 {

    private final EmbeddingModel embeddingModel;//嵌入模型
    private final ChatClient chatClient;//聊天客户端
    private final EmbeddingService embeddingService;

    private List<String> docs = new ArrayList<>();//存储本地文档切分内容
    private List<float[]> vectors = new ArrayList<>();//存储本地文档对应的向量

    public RagService2(EmbeddingModel embeddingModel, ChatClient.Builder builder, EmbeddingService embeddingService) throws IOException {
        this.embeddingModel = embeddingModel;
        this.chatClient = builder.build();
        this.embeddingService = embeddingService;
        //加载本地文档
        ClassPathResource resource = new ClassPathResource("古代诗歌常用意象.txt");
        //创建textReader来读取文档内容
        TextReader textReader = new TextReader(resource);
        List<Document> documents = textReader.read();
        TokenTextSplitter tokenTextSplitter = TokenTextSplitter.builder()
                .withChunkSize(800)//拆分chunk每个最多800个token
                .withMinChunkSizeChars(400)//每个chunk最小允许的字符
                .withKeepSeparator(true)//保留分隔符，提高上下文的连贯性
                .build();
        //按照设置的参数切分的chunk
        List<Document> chunks = tokenTextSplitter.apply(documents);

        //将chunk向量化并存储在本地
        for (Document chunk : chunks) {
            String text = chunk.getText();
            docs.add(text);//存储chunk对应的内容
            vectors.add(embeddingModel.embed(text));//存储chunk对应的向量
        }
    }

    public static class IndexSim {
        int index;//文档索引
        double sim;//相似度

        public IndexSim(int index, double sim) {
            this.index = index;
            this.sim = sim;
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
        //定义topK和阈值
        int topK = 5;
        double threshold = 0.05;//相似度阈值

        List<IndexSim> indexSimList = new ArrayList<>();
        for (int i = 0; i < vectors.size(); i++) {
            //两个向量的相似度
            double sim = embeddingService.consineSimilarity(qv, vectors.get(i));
            if (sim >= threshold) {
                indexSimList.add(new IndexSim(i, sim));
            }
        }

        //按照相似度由大到小排序
        indexSimList.sort((indexSim1, indexSim2) -> Double.compare(indexSim2.sim, indexSim1.sim));
        //获取topK
        List<IndexSim> topKList = indexSimList.stream().limit(topK).collect(Collectors.toList());
        //获取每个topKList每个片段前后临近的chunk
        TreeSet<Integer> idxSet = new TreeSet<>();
        for (IndexSim indexSim : topKList) {
            idxSet.add(indexSim.index);
            if (indexSim.index - 1 >= 0) {
                idxSet.add(indexSim.index - 1);//加chunk前的chunk
            }
            if (indexSim.index + 1 < docs.size()) {
                idxSet.add(indexSim.index + 1);//加chunk后的chunk
            }
        }
        //将片段获取并拼接，作为上下文传给LLM
        String context = idxSet.stream()
                .map(index -> docs.get(index))
                .collect(Collectors.joining("\n----\n"));
        //准备prompt
        String prompt = "以下是知识库的内容：\n" + context + "\n" + "请基于上述知识库的内容回答用户问题：" + question;

        System.out.println("prompt = " + prompt);
        ChatClient.CallResponseSpec responseSpec = chatClient.prompt()
                .system("你是知识助手结合上下文回答问题")
                .user(prompt)
                .call();
        //获取模型返回的内容
        return responseSpec.content();
    }
}
