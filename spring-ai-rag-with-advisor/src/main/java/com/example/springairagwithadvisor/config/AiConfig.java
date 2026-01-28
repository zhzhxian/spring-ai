package com.example.springairagwithadvisor.config;

import io.milvus.client.MilvusServiceClient;
import io.milvus.grpc.GetCollectionStatisticsResponse;
import io.milvus.param.R;
import io.milvus.param.collection.FlushParam;
import io.milvus.param.collection.GetCollectionStatisticsParam;
import io.milvus.response.GetCollStatResponseWrapper;
import jakarta.annotation.PostConstruct;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.milvus.MilvusVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 构建 ChatClient
 * 构建 QuestionAnswerAdvisor
 * 构建 RetrievalAugmentationAdvisor
 * 将向量数据库中写入数据，项目启动写入一次
 */
@Configuration
public class AiConfig {

    @Value("${spring.ai.vectorstore.milvus.collection-name}")
    private String collectionName;

    @Autowired
    private MilvusVectorStore vectorStore;

    @Bean
    public ChatClient chatClient(DeepSeekChatModel deepSeekChatModel) {
        return ChatClient.builder(deepSeekChatModel)
                .defaultSystem("你是一个助手，回答问题时不要提及是从向量数据库中获取，如果你不知道用户提的问题，请直接回复：不知道这个问题法人答案！")
                .build();
    }

    @Bean
    public QuestionAnswerAdvisor questionAnswerAdvisor() {
        return QuestionAnswerAdvisor.builder(vectorStore)
                .searchRequest(
                        SearchRequest.builder()
                                .similarityThreshold(0.5d)
                                .topK(6)
                                .build()
                )
                .build();
    }

    @Bean
    public RetrievalAugmentationAdvisor retrievalAugmentationAdvisor() {
        return RetrievalAugmentationAdvisor.builder()
                .documentRetriever(
                        VectorStoreDocumentRetriever.builder()
                                .vectorStore(vectorStore)
                                .similarityThreshold(0.5d)
                                .topK(6)
                                .build()
                )
                .queryAugmenter(
                        ContextualQueryAugmenter.builder()
                                .allowEmptyContext(true)
                                .build()

                )
                .build();
    }

    //初始化向量数据库
    @PostConstruct
    public void initVectorData() {
        System.out.println("初始化向量数据写入到Milvus");
        //获取Milvus客户端
        MilvusServiceClient milvusServiceClient = (MilvusServiceClient) vectorStore.getNativeClient().get();
        //先获取数据条数，如果大于0就不写入
        R<GetCollectionStatisticsResponse> resp = milvusServiceClient.getCollectionStatistics(GetCollectionStatisticsParam.newBuilder()
                .withCollectionName(collectionName)
                .build());
        GetCollStatResponseWrapper getCollStatResponseWrapper = new GetCollStatResponseWrapper(resp.getData());
        long rowCount = getCollStatResponseWrapper.getRowCount();
        System.out.println("milvus vector store 中的数据条数：" + rowCount);
        if (rowCount == 0) {
            System.out.println("milvus中没有数据，写入数据");
            List<Document> documents = List.of(
                    new Document("spring ai 是一个开源的ai集成项目"),
                    new Document("milvus 是一个向量数据库"),
                    new Document("java 是一门编程语言")
            );
            //vectorStore.add(docs) 调用时，Spring AI 的 MilvusVectorStore 会使用注入的 EmbeddingModel 将每个 Document 中的文本内容转换成向量并写入
            vectorStore.add(documents);
            milvusServiceClient.flush(FlushParam.newBuilder().withCollectionNames(List.of(collectionName)).build());
        }

    }
}
