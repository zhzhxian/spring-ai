package com.example.springaimcpsseclient.config;

import io.modelcontextprotocol.client.McpSyncClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class McpClientConfig {
    /**
     * SyncMcpToolCallbackProvider 自动集成MCP Server 暴露的工具到ChatClient，可以使LLM使用工具
     *
     * @param clients
     * @return
     */
    @Bean
    public SyncMcpToolCallbackProvider syncMcpToolCallbackProvider(List<McpSyncClient> clients) {
        return SyncMcpToolCallbackProvider.builder().mcpClients(clients).build();
    }

    @Bean
    public ChatClient chatClient(ChatModel chatModel, SyncMcpToolCallbackProvider syncMcpToolCallbackProvider) {
        return ChatClient.builder(chatModel)
                .defaultSystem("你是一个优秀的助手，可以调用工具来回答问题")
                .defaultToolCallbacks(syncMcpToolCallbackProvider)
                .build();
    }
}
