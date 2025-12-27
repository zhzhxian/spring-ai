package com.example.springaimcpstdioserver;

import com.example.springaimcpstdioserver.service.WeatherService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringAiMcpStdioServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAiMcpStdioServerApplication.class, args);
    }

    /**
     * ToolCallbackProvider 接口：Spring AI 提供的接口，负责将带有@Tool注解的放方法注册为AI LLM 可以调用的工具
     *
     * @param weatherService mcp 服务
     * @return
     */
    @Bean
    public ToolCallbackProvider toolCallbackProvider(WeatherService weatherService) {
        return MethodToolCallbackProvider.builder().toolObjects(weatherService).build();
    }
}
