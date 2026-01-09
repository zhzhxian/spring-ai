# spring-ai

# 1. **Spring AI介绍**

## 1.1. **什么是Spring AI**

**Spring AI是面向 Java 和 Spring 生态的原生生成式人工智能框架**。它不是简单地将 Python 中的 LangChain 或 LlamaIndex 移植到 Java，而是依据 Spring 的设计理念——如依赖注入、POJO、模块化和可配置——重构生成式 AI 的全流程。**通过 Spring Boot 的自动装配机制，开发者可以像调用数据库或 Web API 一样轻松地接入聊天、嵌入、图像生成、语音处理等 AI 能力**，并且能够毫不费力地将企业内部数据与 AI 模型关联起来（如同 RAG 检索增强生成中常用的数据注入方式）。

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/8b697cdc95914482a80d529bfa582de2.jpg)

Spring AI 倡导“一套接口，多种实现”，开发者无须为不同 AI 提供商逐一适配，而是可以通过统一抽象实现轻松切换，比如 OpenAI、Anthropic、Bedrock、Hugging Face、Vertex AI、Ollama 等服务。

* **Spring AI 官网地址**

https://spring.io/projects/spring-ai

* **Spring AI 文档地址**

[https://docs.spring.io/spring-ai/reference/index.html](https://docs.spring.io/spring-ai/reference/index.html)

* **Spring AI 中文文档地址**

https://spring-ai.spring-doc.cn/docs/1.0.0/index.html

## 1.2. **Spring AI 特点**

Spring AI 功能模块丰富，涵盖AI应用开发的各个环节，具备如下特点：

**1. 多供应商模型支持**

支持主流的AI模型提供商，例如：Anthropic、OpenAI、Microsoft、Amazon、Google、Ollama 等模型服务。通过这些模型可以实现聊天、文本嵌入、文生图、音频转录、文本转语音、内容审核等多种能力。

**2. 统一抽象API**

提供如 ChatClient, EmbeddingModel, ImageModel 等统一接口，无论切换到哪家 AI 平台，调用方式一致，同时支持同步与流式调用，也能够访问模型特定功能。

**3. Spring Boot集成**

以 Starter 和自动装配方式支持 AI 模型、向量数据库、ETL 工具等，开发者可通过 Initializr 快速上手。

**4. 结构化输出与类型安全**

模型的响应可解析并映射到 Java POJO，保证后续处理的类型安全与可维护性。

**5. 向量存储与RAG**

集成了主流向量数据库（PostgreSQL/pgvector、Pinecone、Qdrant、Redis、Weaviate 等）及其元数据过滤，通过内置的 ETL 文档处理流程，结合检索增强生成（RAG）实现文档问答和聊天检索。

**6. Tool/Function Calling**

支持模型发起函数调用（类似 OpenAI Function Calling），可以注册 Spring Bean 作为可调用工具，从而访问实时业务系统或执行外部操作。

**7. 可观测性与评估**

内建对于 AI 调用的监控指标与日志、模型评估工具，可用于检测响应准确性、防止“幻觉”。

## 1.3. **Spring AI 快速上手**

### **1.3.1. 环境要求**

Spring AI构建在Spring Boot 3.x之上，Spring Boot 3.x系列最低Java要求版本是JDK17，不支持Java8/11/16等低于17的版本，推荐使用Maven3.6及以上版本。

我们后续使用Spring AI 时，对应环境版本如下：

* SpringBoot 3.5.0版本
* JDK17版本
* Maven3.9.9版本

这里在Windows中下载并安装JDK17。使用如下链接下载JDK 17后进行安装，这里安装在D盘“D:\\Program Files\\Java\\jdk17\\jdk”中，不需要配置环境变量，只需要在相应的SpringBoot项目中设置使用的JDK17版本即可。

JDK17下载地址：[https://www.oracle.com/cn/java/technologies/downloads/#java17](https://www.oracle.com/cn/java/technologies/downloads/#java17)

### **1.3.2. Deepseek对话案例**

下面以Spring AI中通过与Deepseek模型对话为例，演示Spring AI相关配置。

**1) 创建SpringBoot项目，命名为“SpringAIQuickStart”**

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/90abdbf0743b4bb1830bc8d1b3981081.jpg)

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/df7ec41d1dfe4678a2d07e54d54ba6e0.jpg)

**2) 配置项目pom.xml**

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.5.0</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.example</groupId>
    <artifactId>SpringAIQuickStart</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>SpringAIQuickStart</name>
    <description>SpringAIQuickStart</description>

    <properties>
        <java.version>17</java.version>
    </properties>

    <!-- 导入 Spring AI BOM，用于统一管理 Spring AI 依赖的版本，
    引用每个 Spring AI 模块时不用再写 <version>，只要依赖什么模块 Mavens 自动使用 BOM 推荐的版本 -->
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.ai</groupId>
                <artifactId>spring-ai-bom</artifactId>
                <version>1.0.0-SNAPSHOT</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.ai</groupId>
            <artifactId>spring-ai-starter-model-deepseek</artifactId>
        </dependency>
    </dependencies>


    <!-- 声明仓库， 用于获取 Spring AI 以及相关预发布版本-->
    <repositories>
        <repository>
            <id>spring-snapshots</id>
            <name>Spring Snapshots</name>
            <url>https://repo.spring.io/snapshot</url>
            <releases>
                <enabled>false</enabled>
            </releases>
        </repository>
        <repository>
            <name>Central Portal Snapshots</name>
            <id>central-portal-snapshots</id>
            <url>https://central.sonatype.com/repository/maven-snapshots/</url>
            <releases>
                <enabled>false</enabled>
            </releases>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
    </repositories>

</project>
```

**3) 配置resources/application.properties**

```
spring.application.name=SpringAIQuickStart

server.port=8080

#配置 Deepseek的基础URL、密钥和使用模型
spring.ai.deepseek.base-url=https://api.deepseek.com
spring.ai.deepseek.api-key=sk-81bxxx62c6a821
spring.ai.deepseek.chat.options.model=deepseek-chat

# 介于0和2之间，0表示随机性最小，2表示随机性最大。
spring.ai.deepseek.chat.options.temperature=0.8
```

**4) 创建controller包，并创建ChatController.java文件**

```
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
public class ChatController {
    @Autowired
    private DeepSeekChatModel chatModel;

    @GetMapping("/generate")
    public String generate(@RequestParam(value = "message", defaultValue = "给我讲个笑话") String message) {
        System.out.println("收到消息："+message);
        String result = chatModel.call(message);
        //模型返回的内容
        System.out.println(result);
        return result;
    }

}
```

**5) 启动项目并测试**

启动项目后，浏览器输入“http://localhost:8080/ai/generate?message=你是谁”，可以看到输出内容如下：

```
我是DeepSeek Chat，由深度求索公司（DeepSeek）开发的智能AI助手！✨ 我可以帮你解答问题、提供建议、陪你聊天，还能处理各种文本、文档等内容。无论是学习、工作，还是日常生活中的疑问，都可以来问我哦！😊 有什么我可以帮你的吗？
```

为了更好的可视化与Deepseek模型聊天，我们还可以在项目的“resources/static”目录下创建“index.html”实现聊天可视化，index.html内容如下：

```
<!-- src/main/resources/static/index.html -->
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AI 聊天</title>
    <script src="https://cdn.jsdelivr.net/npm/marked/marked.min.js"></script>
    <style>
        body {
            font-family: Arial, sans-serif;
            padding: 20px;
            max-width: 800px;
            margin: 0 auto;
        }
        h1 {
            text-align: center;
            color: #333;
        }
        textarea {
            width: 100%;
            padding: 10px;
            font-size: 16px;
            border: 1px solid #ccc;
            border-radius: 4px;
            margin-bottom: 10px;
            box-sizing: border-box;
        }
        button {
            width: 100%;
            padding: 10px;
            font-size: 16px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        button:hover {
            background-color: #0056b3;
        }
        #response {
            margin-top: 20px;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
            background-color: #f9f9f9;
        }
        /* Loading Spinner */
        #loading {
            display: none;
            margin: 20px auto;
            border: 4px solid #f3f3f3;
            border-top: 4px solid #3498db;
            border-radius: 50%;
            width: 50px;
            height: 50px;
            animation: spin 2s linear infinite;
        }
        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }
    </style>
</head>
<body>
<h1>与 AI 聊天</h1>
<textarea id="userMessage" rows="4" placeholder="请输入您的问题..."></textarea><br><br>
<button onclick="sendMessage()">发送</button>

<div id="loading"></div>
<div id="response"></div>

<script>
    function sendMessage() {
        const message = document.getElementById('userMessage').value;
        if (!message) {
            alert('请输入消息');
            return;
        }

        // 显示加载动画
        document.getElementById('loading').style.display = 'block';

        fetch(`/ai/generate?message=${encodeURIComponent(message)}`)
            .then(response => response.text())
            .then(data => {
                // 隐藏加载动画
                document.getElementById('loading').style.display = 'none';
                document.getElementById('response').innerHTML = marked.parse(data);
            })
            .catch(error => {
                console.error('请求失败:', error);
                alert('请求失败，请稍后再试');
                // 隐藏加载动画
                document.getElementById('loading').style.display = 'none';
            });
    }
</script>
</body>
</html>
```

重启Springboot项目后，直接访问“http://localhost:8080”可以直接访问到resource/static/index.html进行可视化与模型对话：

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/c6e52536db324c8e806c52caeda3ff77.jpg)

# 2. **Models 模型**

## 2.1. **Chat Models**

Spring AI 的 Chat Model API 提供一套统一接口，方便开发者在应用中集成 AI 聊天完成功能。它支持切换不同厂商的聊天模型，如 OpenAI、Anthropic、Google Gemini、Amazon Bedrock、Ollama 等，多数实现支持流式输出和函数调用等功能。

如下以Deepseek模型为例，演示ChatModel相关功能，更多模型的使用方式参考：[https://docs.spring.io/spring-ai/reference/api/chatmodel.html](https://docs.spring.io/spring-ai/reference/api/chatmodel.html)

### **2.1.1. 基本聊天**

参考Spring AI快速上手案例部分。核心代码如下：

```
@GetMapping("/generate")
public String generate(@RequestParam(value = "message", defaultValue = "给我讲个笑话") String message) {
    System.out.println("收到消息："+message);
    String result = chatModel.call(message);
    //模型返回的内容
    System.out.println(result);
    return result;
}
```

### **2.1.2. Stream流式聊天**

Spring AI中使用对话模型时，也支持流式聊天。以Deepseek为例，在“SpringAIQuickStart”项目的“/controller/ChatController.java”代码中准备如下方法：

```
//流式获取模型返回的内容，获取返回的 ChatResponse 对象内容
@GetMapping("/generateStream1")
public Flux<ChatResponse> generateStream1(@RequestParam(value = "message", defaultValue = "给我讲个笑话") String message) {
    System.out.println("收到消息："+message);
    var prompt = new Prompt(new UserMessage(message));
    return chatModel.stream(prompt);
}

//流式获取模型返回的内容，获取返回的 Text 内容
@GetMapping("/generateStream2")
public Flux<String> generateStream2(
        @RequestParam(value = "message", defaultValue = "给我讲个笑话") String message,
        HttpServletResponse response) {
    // 避免返回乱码
    response.setCharacterEncoding("UTF-8");

    System.out.println("收到消息："+message);
    var prompt = new Prompt(new UserMessage(message));
    Flux<String> result = chatModel.stream(prompt)
            .map(chatResponse -> chatResponse.getResult().getOutput().getText());

    return result;
}
```

以上代码中注意如下几点：

* “generateStream1”方法返回Flux&#x3c;ChatResponse>对象，Flux&#x3c;T>表示“0到N个元素”的异步数据流，用于模型返回异步数据的输出。
* ChatResponse对象内容如下：

```
[
{
	"result":{
		"metadata":{"finishReason":"","contentFilters":[],"empty":true},
		"output":{"messageType":"ASSISTANT","metadata":{"finishReason":"","id":"e392c615-9d42-4a8f-a819-1640cd3a7d68","role":"ASSISTANT","messageType":"ASSISTANT"},"toolCalls":[],"media":[],"prefix":null,"reasoningContent":null,"text":""}},
	"metadata":{"id":"e392c615-9d42-4a8f-a819-1640cd3a7d68","model":"deepseek-chat","rateLimit":{"requestsRemaining":0,"requestsReset":"PT0S","requestsLimit":0,"tokensReset":"PT0S","tokensRemaining":0,"tokensLimit":0},"usage":{"completionTokens":0,"promptTokens":0,"nativeUsage":{},"totalTokens":0},"promptMetadata":[],"empty":false},
	"results":[{"metadata":{"finishReason":"","contentFilters":[],"empty":true},"output":{"messageType":"ASSISTANT","metadata":{"finishReason":"","id":"e392c615-9d42-4a8f-a819-1640cd3a7d68","role":"ASSISTANT","messageType":"ASSISTANT"},"toolCalls":[],"media":[],"prefix":null,"reasoningContent":null,"text":""}}]
},
{
	"result":{"metadata":{"finishReason":"","contentFilters":[],"empty":true},"output":{"messageType":"ASSISTANT","metadata":{"finishReason":"","id":"e392c615-9d42-4a8f-a819-1640cd3a7d68","role":"ASSISTANT","messageType":"ASSISTANT"},"toolCalls":[],"media":[],"prefix":null,"reasoningContent":null,"text":"好的"}},
	"metadata":{"id":"e392c615-9d42-4a8f-a819-1640cd3a7d68","model":"deepseek-chat","rateLimit":{"requestsRemaining":0,"requestsReset":"PT0S","requestsLimit":0,"tokensReset":"PT0S","tokensRemaining":0,"tokensLimit":0},"usage":{"completionTokens":0,"promptTokens":0,"nativeUsage":{},"totalTokens":0},"promptMetadata":[],"empty":false},
	"results":[{"metadata":{"finishReason":"","contentFilters":[],"empty":true},"output":{"messageType":"ASSISTANT","metadata":{"finishReason":"","id":"e392c615-9d42-4a8f-a819-1640cd3a7d68","role":"ASSISTANT","messageType":"ASSISTANT"},"toolCalls":[],"media":[],"prefix":null,"reasoningContent":null,"text":"好的"}}]
},
....
]
```

* generateStream2方法返回Flux&#x3c;String>对象，这里获取ChatResponse对象中模型返回的text内容。
* 启动项目后，可以在浏览器输入“http://localhost:8080/ai/generateStream2?message=你是谁”查看模型Stream流式返回结果。

### **2.1.3. 运行时参数设置**

Spring AI 使用模型时，可以通过项目中application.properties配置文件来设置关于模型的全局默认参数，示例如下：

```
spring.ai.openai.base-url: https://api.deepseek.com
spring.ai.openai.api-key: ${DEEPSEEK_API_KEY}
spring.ai.openai.chat.options.model: deepseek-chat
spring.ai.openai.chat.options.temperature: 0.7
... ...
```

对于一些请求可能需要不同的行为参数，例如：模型回复更高的随机性，这时候就需要在项目运行时进行参数覆盖，这就是Spring AI的运行时参数设置。

在运行时设置参数时，可以在调用模型时，创建一个prompt，并嵌入一个新的DeepSeekChatOptions 来覆盖启动配置。

在“SpringAIQuickStart”项目的“/controller/ChatController.java”代码中准备如下方法：

```
@GetMapping("/runtimeOptions")
public String runtimeOptions(
        @RequestParam(value = "message") String message,
        @RequestParam(value = "temp", required = false) Double temp
) {
    System.out.println("收到消息："+message);

    Prompt prompt;
    if (temp != null) {
        // 构建带 temperature 的 DeepSeekChatOptions，覆盖默认 temperature
        var opts = DeepSeekChatOptions.builder()
                .temperature(temp)
                .build();
        prompt = new Prompt(message, opts);
        System.out.println("使用运行时覆盖 temperature=" + temp);
    } else {
        // 无 temperature 传入时，使用默认配置
        prompt = new Prompt(message);
        System.out.println("使用默认 temperature");
    }

    ChatResponse resp = chatModel.call(prompt);
    String result = resp.getResult().getOutput().getText();
    System.out.println("模型返回："+ result);
    return result;
}

```

以上代码编写好后，重启项目，可以传入不同的参数：

“http://localhost:8080/ai/runtimeOptions?message=1加1等于几&temp=0.1”，模型返回如下：

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/d2f9b1c8ba3247269dac8300abff5410.jpg)

“http://localhost:8080/ai/runtimeOptions?message=1加1等于几&temp=2”，模型返回如下：

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/152cd492562042889a0c688618563d9c.jpg)

## 2.2. **Embedding Models**

### **2.2.1. Embedding 介绍**

Embedding 是一种将文本（也可扩展至图像、视频）转换成数字向量的技术，这些向量表示了输入内容在语义空间中的位置，能够反映它们之间的相似度——向量距离越近，内容越相似。

Spring AI 通过其 EmbeddingModel 接口提供一套统一、简单、可替换的访问方式，支持多种底层模型（如 OpenAI、Titan、Azure、Ollama、智谱等），这样可以统一接口，切换模型仅需要改配置，不改调用逻辑。

Spring AI 中的Embedding 使用场景如下：

* 相似度计算/语义搜索：将查询和文档全部转换为向量，构建向量数据库进行近邻检索；
* 聚类与分类：将文本转换为向量后，使用传统算法进行聚类或分类；
* 检索增强生成（RAG）：先用向量搜索获取相关知识，再结合生成模型回答；
* 推荐系统：如问答推荐、内容推荐等；
* 异常检测：语义异常内容检测。

### **2.2.2. 智普AI Embedding 使用示例**

智普AI（北京智谱华章科技有限公司）是清华大学知识工程实验室成果转化成立的大模型研发公司，专注于构建“认知智能”体系。其产品线包括文字对话模型（如 ChatGLM、GLM-4）、图像乃至视频理解模型（如 GLM-4V-Plus）以及文本视频生成模型（如 CogVideoX/“清影”）等，覆盖文字、图像、音视频等多种模态。

使用智普AI需要在以下网站中进行注册并充值。智普AI相关网站如下：

* 智普AI官网地址：https://open.bigmodel.cn/
* 智普AI Key地址：[https://open.bigmodel.cn/usercenter/proj-mgmt/apikeys](https://open.bigmodel.cn/usercenter/proj-mgmt/apikeys)
* 智普AI 充值地址:https://open.bigmodel.cn/finance/pay
* 智普AI相关模型费用地址：[https://www.bigmodel.cn/pricing](https://www.bigmodel.cn/pricing)，可以看到相关种类模型的费用，也包含一些免费模型。
* 智普AI 文档地址：https://open.bigmodel.cn/dev/howuse/introduction

在Spring AI 中使用Embedding时，需要对应的AI模型支持Embedding，deepseek中目前没有Embedding模型，这里使用智普AI的“embedding-2”模型来演示Embedding使用。以下示例中演示如何在SpringBoot中配置智普AI作为Embedding模型，并对文本数据向量化操作。

**1) 创建SpringBoot项目，命名为“SpringAIEmbedding”**

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/1d6e4d5db6974578b7fa388154760720.jpg)

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/0bf6040610654509a2711c07eee744b0.jpg)

**2) 配置项目pom.xml**

pom.xml内容如下：

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.5.0</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.example</groupId>
    <artifactId>SpringAIEmbedding</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>SpringAIEmbedding</name>
    <description>SpringAIEmbedding</description>

    <properties>
        <java.version>17</java.version>
    </properties>

    <!-- 导入 Spring AI BOM，用于统一管理 Spring AI 依赖的版本，
    引用每个 Spring AI 模块时不用再写 <version>，只要依赖什么模块 Mavens 自动使用 BOM 推荐的版本 -->
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.ai</groupId>
                <artifactId>spring-ai-bom</artifactId>
                <version>1.0.0-SNAPSHOT</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.ai</groupId>
            <artifactId>spring-ai-starter-model-zhipuai</artifactId>
        </dependency>

        <!-- spring-ai-client-chat 中包括 TokenTextSplitter、TextReader、Document 等工具 -->
        <dependency>
            <groupId>org.springframework.ai</groupId>
            <artifactId>spring-ai-client-chat</artifactId>
            <version>1.0.0</version>
        </dependency>

    </dependencies>

    <!-- 声明仓库， 用于获取 Spring AI 以及相关预发布版本-->
    <repositories>
        <repository>
            <id>spring-snapshots</id>
            <name>Spring Snapshots</name>
            <url>https://repo.spring.io/snapshot</url>
            <releases>
                <enabled>false</enabled>
            </releases>
        </repository>
        <repository>
            <name>Central Portal Snapshots</name>
            <id>central-portal-snapshots</id>
            <url>https://central.sonatype.com/repository/maven-snapshots/</url>
            <releases>
                <enabled>false</enabled>
            </releases>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
    </repositories>

</project>
```

特别注意：需要在“SpringAIEmbedding”项目的pom.xml中导入“spring-ai-starter-model-zhipuai”依赖。“spring-ai-client-chat”依赖包需要对文档进行切分处理时所使用的依赖包。

**3) 配置resources/application.properties**

```
spring.application.name=SpringAIEmbedding

server.port=8080

#使用 智普AI Embedding 模型，需要在pom.xml中引入对应依赖
spring.ai.zhipuai.api-key=d...GA4
spring.ai.zhipuai.base-url=https://open.bigmodel.cn/api/paas
spring.ai.zhipuai.embedding.options.model=embedding-2

#使用 智普AI Chat 模型，需要在pom.xml中引入对应依赖
spring.ai.zhipuai.chat.options.model=GLM-4-Flash
```

**4) 创建Controller包并创建EmbeddingController类**

在“SpringAIEmbedding”项目中创建“controller”包，在该包下编写“EmbeddingController.java”类，该类中实现embed方法来将用户输入的文本内容通过智普AI Embedding进行向量化。

```
import com.example.springaiembedding.service.EmbeddingService;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
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

    //测试 embedding
    @GetMapping("/embedding")
    public Map embed(@RequestParam(value = "message", defaultValue = "给我讲个笑话") String message) {
        // 调用 embed 方法，将文本转换成向量，默认向量维度为 1024
        float[] vector = embeddingModel.embed(message);

        return Map.of(
                "message", message,
                "vector", vector
        );
    }
}
```

以上代码中对用户输入的文本内容向量化，只需要调用“embeddingModel.embed(文本)”即可，如果对多条文本进行向量化转换，可以调用“embeddingModel.embed(List\&#x3c;T\>)”操作。

**5) 启动项目并测试**

启动项目后，浏览器输入“http://localhost:8080/ai/embedding?message=今天天气很好”，可以看到输出内容如下：

```
{"message":"今天天气很好","vector":[-0.07260562,0.005600141,-0.0378431,-0.034070812,-0.018191425,-0.0034634115,-0.029991228,0.038196057,0.025889568,0.001633238,-0.016376244,-0.040188957,-0.017353531,0.047503855,-0.018386343,0.014544255,-0.021725116,0.002996758,-0.011191875,0.011162163,-0.019570969,0.017660921,0.03319585,-0.065318294,-2.5312623E-4,-0.067810364,0.013731177,0.008502983,0.02763537,-0.009782783,0.011306662,0.005104383,0.00480137,0.04124188,-0.012842868,0.039708782,-0.02221145...]}
```

可以看到通过智普AI的“embedding-2”模型将用户输入的文本进行进行了向量化。‌‌‌‌

### **2.2.3. Embedding案例1：查找相似文本**

本案例中实现通过智普AI 的Embedding模型实现对用户输入文本的向量化，然后通过余弦相似度与已有的本地知识内容进行匹配，查找并输出与用户输入相似的文本内容。

该案例实现思路如下：

**1) SpringBoot中创建Service，当服务启动时，将所有本地知识批量通过embed(List\&#x3c;T\>)转换为向量。在Service中定义queryBastMatch方法，该方法对传入的文本进行向量化，然后与本地知识转换的向量通过余弦相似度计算，找出最相似的本地知识内容。**

**2) SpringBoot中创建Controller，用户通过浏览器URL访问到Controller中similarity方法，该方法调用到Service中queryBestMatch方法，返回给用户最相似的本地知识内容。**

#### **2.2.3.1. 余弦相似度**

余弦相似度是一种衡量两个向量方向相似程度的度量方法，通过计算它们夹角的余弦值来评估相似性，广泛应用于文本分析、数据挖掘等领域‌。

余弦相似度的数学本质是向量空间模型中夹角的余弦值，计算公式为两个向量的点积除以它们的模长乘积。对于n维向量A和B，公式可表示为：

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/9b83011f6cc046719545e3737b1a471e.jpg)

其值范围在-1到1之间，1表示完全相同，-1表示完全相反，0表示无关。

余弦相似度典型应用场景如下：

* 文本相似度计算‌：将文档转化为词频向量后，通过余弦相似度比较内容相似性。‌‌
* 聚类分析‌：衡量数据点在高维空间中的分布方向是否相近，常用于推荐系统或异常检测。

#### **2.2.3.2. 案例实现**

**1) 准备Service及对应方法**

在“SpringAIEmbedding”项目中创建“service”包，并在该包中创建“EmbeddingService.java”类，写入如下内容：

```
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmbeddingService {

    private EmbeddingModel embeddingModel;

    private final List<float[]> docVectors;

    // 1. 准备知识库文本内容
    private final List<String> docs = List.of(
            "美食非常美味，服务员也很友好。",
            "这部电影既刺激又令人兴奋。",
            "阅读书籍是扩展知识的好方法。"
    );

    public EmbeddingService(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
        // 2. 启动时，将所有文档向量化
        this.docVectors = this.embeddingModel.embed(docs);
    }

    /**
     * 输入用户查询，返回最相似文档的索引 （使用余弦相似度计算）
     * @param query 用户输入的查询文本
     * @return 最相似的知识库文本
     */
    public String queryBestMatch(String query) {
        //将用户的输入通过EmbeddingModel转换成向量
        float[] queryVec = embeddingModel.embed(query);

        int bestIdx = -1; //记录目前最匹配文档在列表中的索引位置
        double bestSim = -1; //记录目前的最高相似度

        // 遍历所有文档对应的向量，计算与查询向量的相似度
        for (int i = 0; i < docVectors.size(); i++) {
            //计算余弦相似度
            double sim = cosineSimilarity(queryVec, docVectors.get(i));
            if (sim > bestSim) {
                bestSim = sim;
                bestIdx = i;
            }
        }
        return docs.get(bestIdx);
    }

    // 余弦相似度实现
    // 计算两个向量之间的余弦相似度，数值范围在 [-1, 1] 之间
    // 相似度越高，越接近1，越接近0，越不相似
    private double cosineSimilarity(float[] a, float[] b) {
        double dot = 0, na = 0, nb = 0;
        for (int i = 0; i < a.length; i++) {
            dot += a[i] * b[i];
            na += a[i] * a[i];
            nb += b[i] * b[i];
        }
        return dot / (Math.sqrt(na) * Math.sqrt(nb));
    }

}
```

**2) 准备Controller及对应方法**

在“SpringAIEmbedding”项目中的“controller/EmbeddingController.java”类中注入如下依赖和增加如下方法：

```
@Autowired
private EmbeddingService service;

//文本相似检测
@GetMapping("/similarity")
public Map<String, String> similarity(@RequestParam("query") String query) {
    String ans = service.queryBestMatch(query);
    return Map.of("query", query, "answer", ans);
}
```

**3) 启动项目并测试**

启动Spring Boot项目，浏览器中输入如下内容进行相似文本查找：

```
#输入http://localhost:8080/ai/similarity?query=美食
{
  "query": "美食",
  "answer": "美食非常美味，服务员也很友好。"
}

#输入http://localhost:8080/ai/similarity?query=电影
{
  "query": "电影",
  "answer": "这部电影既刺激又令人兴奋。"
}

#输入http://localhost:8080/ai/similarity?query=书籍
{
  "query": "书籍",
  "answer": "阅读书籍是扩展知识的好方法。"
}

```

测试结果：

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/cee3809c973144968e8ee54f092a964d.jpg)

### **2.2.4. Embedding案例2：RAG本地知识库检索**

#### **2.2.4.1. 什么是RAG**

检索增强生成（Retrieval-Augmented Generation，简称 RAG）是一种将大型语言模型（LLM）与外部知识源相结合的人工智能技术。通过在生成响应前检索相关信息，RAG 能够为模型提供最新且特定领域的知识，从而提高回答的准确性和相关性。

RAG的工作原理如下：

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/f34fd0dfb4744d449ed04a12f72a2cf0.jpg)

我们有自己的文档（url、pdf、txt、数据库等），这些文档就是用户本地的知识库，经过解析处理为chunks（小块）文档，然后这些文档通过嵌入模型（Embedding Model）将这些文本信息转换成向量（Vector）,即转换成数字表示，这就是嵌入的含义，这些向量保存在向量数据库中，这个过程我们称为indexing(索引)。

当用户提出问题时，这些问题也会通过Embedding Model转换成向量，然后去向量数据库中去检索（Retrieve），检索到与问题相关的文档，然后把这些内容打包，结合提示词（Prompt）一起传递给大模型，这样大模型就有了如何回答该问题的上下文概念，结合大模型的推理能力，形成连贯答案返回给用户，这就是RAG工作流程。

#### **2.2.4.2. 案例实现**

如下是一个完整的RAG(Retrieval-Augmented Generation)案例，该案例中使用Spring AI 、智普AI Embedding、智普 Chat Model来完成。

该案例实现思路如下:

1) 本地知识库文件放入到项目resources资源目录中，在项目中通过ClassPathResource加载。

2) 将文档按照指定的符号进行拆分，分成多个片段，然后使用智普AI Embedding 进行向量化处理，向量化内容保存在内存中。

3) 当用户输入问题时，通过智普AI Embedding进行向量化，然后找出与本地知识向量化内容最相似的Top2内容。

4) 将查找到与用户问题最相关的Top2本地内容和用户问题作为prompt交由智普AI Chat模型进行统一回复。

下面按照以上步骤在“SpringAIEmbedding”项目中实现RAG本地知识库信息检索。

**1) 准备智普AI Chat模型配置**

在“SpringAIEmbedding”项目的resources/application.properties中加入如下chat模型配置，该chat模式将用于整合知识片段回复。

```
#使用 智普AI Chat 模型，需要在pom.xml中引入对应依赖
spring.ai.zhipuai.chat.options.model=GLM-4-Flash
```

**2) 准备本地知识**

在“SpringAIEmbedding”项目的resources资源目录下放入“古代诗歌常用意向.txt”文件，该文件内容如下：

```
古代诗歌中常用的意象
----
1．植物类
草：生命力强、生生不息、希望、荒凉、离恨、卑微。
黄叶：凋零、别离、美人迟暮、新陈代谢。
绿叶：生命力、长久、活力、希望。
梧桐：悲秋、凄苦、感伤，还可表达高贵。
芭蕉：孤独、离别。
柳：送别、思亲、挽留、操守(介之推)。
花开：青春、希望、人生的美好。
花落：惜春、凋零、失意、事业的挫折、对美好事物的留恋。
禾黍：黍离之悲(国家衰败)。
杨花：漂泊、流散、无情。
牡丹：富贵、美好、憧憬。
莲：怜、爱、纯洁。
梅子：少女怀春。
丁香：愁思、情结。
虞美人：罂粟科一年生草本花卉，亦称丽春、赛牡丹。相传此花系西楚霸王项羽爱妾虞姬自刎垓下碧血所化，故有闻虞兮歌而起舞之说。历代文人雅士歌咏此花，多涉及这一悲壮传说。
红豆：《南州记》称为海红豆，《本草》称其为“相思子”。常用以象征爱情或相思。
豆蔻：豆蔻是一种多年生草本植物。后来称女子十三四岁的年纪为豆蔻年华。
岁寒三友：指古诗文中经常提到的松、竹、梅。松，是耐寒树木，终冬不凋，常被看做刚正节操的象征。竹，也经冬不凋，且自成美景，它刚直、谦逊、不卑不亢，潇洒处世，常被看做不同流俗的高雅之士的象征。梅，迎寒而开，美丽绝俗，是坚忍不拔的人格的象征。
花中四君子：古诗人中常提到的梅、兰、竹、菊。兰，一则花朵色淡香清，二则多生于幽僻之处，故常被看做是谦谦君子的象征。菊，它不仅清丽淡雅、芳香袭人，而且具有傲霜斗雪的特征；它艳于百花凋后，不与群芳争艳，故历来被用来象征恬然自处、傲然不屈的高尚品格。(“梅、竹”见上条。)
----
2．动物类
鹧鸪：行不得也哥哥、离愁、物是人非。
燕：家园、物是人非。
鹰：自由、强劲、人生搏击、事业成功。
乌鸦：小人、奸臣、俗客庸夫、哀伤。
沙鸥：飘零、无依、伤感。
鱼：自由、惬意。
鲤鱼：尺素书、信。
寒蝉：悲秋、高洁之士。
鸡狗：田园、世俗生活。
猿猴：凄清、哀伤、荒远。
马：追求、仕途、漂泊(瘦马)。
鸿雁：《汉书·苏武传》载有大雁传书事，后因以之喻书信。
鸿鹄：鸿鹄飞得很高，常用来比喻志气高远的人。
杜鹃：杜鹃鸟俗称布谷，也叫子归、子鹃。春夏季节，杜鹃彻夜不停啼鸣，啼声清脆而短促，唤起人们多种情思。杜鹃口腔上皮和舌部都为红色，古人误以为它们啼得满嘴流血，凑巧杜鹃高歌之时，正是杜鹃花开之际，人们见杜鹃花开得那样鲜红，便把这种颜色说成是杜鹃啼的血。
比翼鸟：传说中的一种鸟，雌雄老在一起飞，古典诗歌里用作恩爱夫妻的比喻。
----
3．景象类
草原：辽阔、人生境界、人的胸襟。
海：辽阔、深邃、力量、胸襟、人生的起伏。
江水：时光流逝、岁月短暂、愁苦绵长。
烟雾：情感朦胧、命运惨淡、前途迷惘、理想幻灭。
小雨：春景、生机活力、希望、伤感、潜移默化式的教化。
暴雨：热情、动荡、巨变、政治斗争、恶势力、荡涤污秽的力量。
狂风：作乱、恶势力、摧毁旧世界的力量。
东风：春风、欢愉、希望、美好、时光。
西风：悲秋、落寞、惆怅、衰败、游子思归。
霜：打击、考验、变易、挫折、社会环境恶劣，恶势力猖狂、人生路途坎坷。
雪：纯洁、美好、寒冷、环境恶劣、恶势力。
露：人生短促、生命易逝。
阴：压抑、愁苦、寂寞。
晴：欢景、光明、希望。
云：游子、飘泊、归隐、轻浮。
夕阳：迟暮、失落、消沉、美好而短暂的事物。
月亮：圆满、缺憾、思乡、思亲、变易、离别。
破晓：清静、迷茫、希望。
暮夜：愁思、怀旧、孤独、清冷。
天地：人的渺小、人生短暂、心胸广阔、情感孤独。
秋水：秋水，喻指眼睛，形容盼望的迫切。
----
4．人文类
英雄：历史风云人物、功业有成者、令人追慕者、让人自愧不如者。 
小人：奸人、被鄙夷的、被鞭挞的、使人反思者。
古迹：古营垒、旧楼台，衰败、萧条、怀旧明志、昔盛今衰。
乡村：思归、世俗、田园风光、生活气息、纯朴宁静的生活。
南浦：送别、感伤。
亭：长亭短亭，送别、挽留、依恋。
都市：市井繁荣、富贵奢华、世俗名利。
仙境：飘逸、忘尘厌俗、幻想、虚幻。
酒：长久、送别、欢悦、得意、失意、愁苦。
云帆：抱负、离别、思乡。
琴瑟：(1)比喻夫妇感情和谐，亦作“瑟琴”。(2)比喻兄弟朋友的情谊。
梨园：梨园原是皇帝禁苑中的果木园圃，唐玄宗开元年间，将其作为教习歌舞的地方，且在这里培养出了大批优秀的音乐舞蹈表演人才，在历史上产生了深远的影响。因此，后世的戏曲班社常以“梨园”为其代称，戏曲艺人称“梨园弟子”。
神器：指地位、政权。
月老：传说唐朝韦固月夜里经过宋城，遇见一个老人坐着翻检书本。韦固前往窥视，一个字也不认得。向老人询问后，才知道老人是专管人间婚姻的神仙，翻检的书是婚姻簿。(见《续幽怪录·定婚店》)后来因此称媒人为月下老人，或月老。
陶朱：春秋时越国大夫范蠡的别号。相传他帮助勾践灭吴后，离开越国到陶，善于经营生计，积累了很多财富，后世因此以“陶朱”或“陶朱公”来称富商。
祝融：传说中楚国唐诉祖先，为高辛氏帝喾的火正(掌火之官)，以光明四海而成为祝融，后世祀为火神。由此，火灾称为祝融之灾。
青梅竹马：出自李白的《长干行》：“郎骑竹马来，绕床弄青梅。同居长干里，两小无嫌猜。”后来用“青梅竹马”形容男女小的时候天真无邪，也指幼小时就相识的伴侣。
问鼎：《左传·宣公三年》：“楚子伐陆浑之戎，隧至于雒观兵于周疆。定王使王孙满劳楚子，楚子问鼎之大小轻重焉。”三代以九鼎为传国宝，楚子问鼎，有觊觎周室之意。后遂以问鼎比喻图谋帝王权位。
逐鹿：《汉书·蒯通传》：“且秦失其鹿，天下共逐之。”颜师古注引张晏曰：“以鹿喻帝位。”后来用逐鹿比喻群雄并起，争夺天下。
三尺：三尺，也叫“三尺法”，是法律的代名词。古代把法律写在三尺长的竹简上，所以称“三尺法”。
杜康：《说文解字·巾部》：“古者少康初作箕帚，少康，杜康也。”后即以杜康作为酒的代称。
秦晋：春秋时，秦晋两国世为婚姻，后因称两姓联姻为“秦晋之好”。
彭祖：传说故事人物，生于夏代，至殷末时已八百余岁，旧时把彭祖作为长寿的象征，以“寿如彭祖”来祝人长寿。
谢家：唐诗宋词不达意之处常用“谢家”之典，其意有：
(1)用谢安、谢玄家事，意指人有风度。
(2)专指谢安侄女谢道韫之事，表示有才有貌之美女。
(3)指山水诗人谢灵运事。
婵娟：婵娟，姿态美好，多用来形容女子；因人们常喻月为美女，故月亮称为婵娟。
献芹：《列子·杨朱》有一个故事说，从前有一个人在乡里的豪绅面前大肆吹嘘芹菜如何好吃，豪绅尝了之后，竟“蜇于口，惨于腹”。后来就用“献芹”谦称赠人的礼品菲薄，或所提的建议浅陋。也说“芹献”。
执牛耳：古代诸侯订立盟约，要每人尝一点牲血，主盟的人亲自割牛耳取血，故用“执牛耳”指盟主。后来指在某一方面居领导地位。
```

**3) 准备Service及对应方法**

在“SpringAIEmbedding”项目中创建service/RagService.java文件，写入如下内容：

```
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class RagService {
    private final EmbeddingModel em; //嵌入模型，用于生成文本的向量
    private final ChatClient chatClient; // 聊天客户端，用于与 AI 进行交互
    private final List<String> docs = new ArrayList<>();//存储本地文档内容
    private final List<float[]> vectors = new ArrayList<>();//存储本地文档向量

    public RagService(EmbeddingModel embeddingModel, ChatClient.Builder chatBuilder) throws IOException {
        this.em = embeddingModel;
        this.chatClient = chatBuilder.build(); // 创建 智普AI Chat 聊天客户端

        // 加载本地文档
        Resource res = new ClassPathResource("古代诗歌常用意象.txt");
        String content = new String(res.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        // 分割文档内容并生成向量
        for (String part : content.split("----")) {
            System.out.println("part: " + part);
            if (part.isBlank()) continue;
            docs.add(part);// 存储切分的文档内容
            vectors.add(em.embed(part)); // 将文档生成 embedding 并存储
        }
    }

    // 对用户输入的问题进行回答
    public String answer(String q) {
        // 生成用户问题的向量
        float[] qv = em.embed(q);

        // 最相似两个文档的索引
        int index1 = -1, index2 = -1;
        // 最相似两个文档的相似度
        double v1 = -1, v2 = -1;
        // 找到最相似的两个文档
        for (int i = 0; i < vectors.size(); i++) {
            // 计算用户问题向量与切分的每个文档向量的余弦相似度
            double sim = cosineSimilarity(qv, vectors.get(i));
            if (sim > v1) {
                index2 = index1;
                v2 = v1;
                index1 = i;
                v1 = sim;
            } else if (sim > v2) {
                index2 = i;
                v2 = sim;
            }
        }

        //获取两个最相似文档的内容，拼接在一起作为上下文
        String ctx = docs.get(index1) + (index2 >= 0 ? "\n---\n" + docs.get(index2) : "");

        //构建 AI 模型的提示信息，包含上下文和用户问题
        String prompt = "以下是知识内容：\n" + ctx + "\n请基于上述知识回答用户问题：“" + q + "”";

        // 使用 ChatClient 流式构建
        var response = chatClient
                .prompt()
                .system("你是知识助手，结合上下文回答问题")  // 添加系统提示
                .user(prompt)                                // 用户 + 上下文
                .call();                                     // 发起同步调用

        // 获取内容文本
        return response.content();
    }

    // 余弦相似度实现
    // 计算两个向量之间的余弦相似度，数值范围在 [-1, 1] 之间
    // 相似度越高，越接近1，越接近0，越不相似
    private double cosineSimilarity(float[] a, float[] b) {
        double dot = 0, na = 0, nb = 0;
        for (int i = 0; i < a.length; i++) {
            dot += a[i] * b[i];
            na += a[i] * a[i];
            nb += b[i] * b[i];
        }
        return dot / (Math.sqrt(na) * Math.sqrt(nb));
    }
}
```

特别注意：以上代码中用户自己将本地知识库文件切分并通过Embedding 模型转换成向量，然后获取与用户输入问题向量最相似的Top2文档交给大模型处理。

**4) 准备Controller及对应方法**

在“SpringAIEmbedding”项目中创建controller/RagController.java文件，写入如下内容：

```
import com.example.springaiembedding.service.RagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/rag")
public class RagController {
    @Autowired
    private RagService ragService;

    @GetMapping("/ask")
    public Map<String, String> ask(@RequestParam("question") String question) {
        String answer = ragService.answer(question);
        return Map.of("question", question, "answer", answer);
    }
}

```

**5) 启动项目并测试**

重新启动“SpringAIEmbedding”项目，然后浏览器输入如下内容进行RAG本地知识库检索测试:

```
http://localhost:8080/rag/ask?question=古代诗歌常用意象有哪些？
```

可以看到答案如下，我们发现关于用户提问的回复并不是太精准，只是返回的部分内容，主要原因详见下个小节。

![](//:0)![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/8353e38575bd49f580561ddc67e67bb1.jpg)

### **2.2.5. Embedding案例3：RAG本地知识库检索升级**

#### **2.2.5.1. 手动切分文档存在问题**

上个案例中，对于用户的提问，RAG本地知识库检索的结果并不理想，原因是：用户提问向量化后在与本地知识库内容匹配时匹配到的片段只有top2前两个，当本地知识库较大且用户的问题可能覆盖多个知识片段时，仅仅返回“最相关的Top2”文档内容导致最终模型回复效果很差，并且仅仅返回的两个知识片段还有可能由于切分规则导致语义不完整，这样也导致模型回复效果很差。

对于以上这个问题，可以通过以下方式改进效果：

**1. 调整Chunk分段策略**

如果分片过大（例如：长文本全部），里面可能包含多个主题内容，但用户查询embedding更聚焦于某一小主题，这样导致检索不够精准；分片过小（几行文字）又无法捕获完整含义。建议分片中等大小，可以几句话到一小段，长度建议200~500字符，根据文档结构灵活切分。

**2. 根据相似阈值来确定获取的知识分片**

将用户问题对应的向量与知识库中知识向量进行相似度计算时，可以指定一个相似度，将大于该相似度的知识片段都加入到上下文，也可以基于此基础上获取TopK相关知识片段，防止更多噪声影响，如Top5/Top10 的相关片段作为上下文。

注意：当知识库大或问题普遍，相似度大于指定阈值的片段可能很多，导致拼接的prompt 超过最大上下文长度，影响性能甚至报错，过多片段也会引入无关或弱相关内容，模型容易“分心”（噪声）。

**3. 获取相关知识片段的相邻片段**

获取相关知识分片时，不仅仅获取相关分片，也将该分片前后相邻的1~2个分片一并加入到上下文，以提升整体连贯性。这样做的原因主要是单片段上下文往往不够，可能丢失前后语境，加上它的前后相邻段可以增强语境连贯性。例如：“春风又绿江南岸”，如果拆分拆到“春风又绿…” 和“…江南岸”两个片段，单独都不完整，将两块合并才有完整意义。

所以解决手动切分文档出现的以上问题，可以从改进文档分段逻辑（每段大小均衡，更好捕获语义边界）+指定关联相似度阈值（排除低相关结果，避免噪声上下文干扰）+TopK相关知识片段限制（可选，可以进一步降低噪声）+增加相关知识片段前后段（前后端补全上下文，提高语义连贯性） 四个方面进行。

#### **2.2.5.2. 案例实现**

**1) 准备智普AI Chat模型配置及导入依赖**

在“SpringAIEmbedding”项目的resources/application.properties中加入如下chat模型配置，该chat模式将用于整合知识片段回复。

```
#使用 智普AI Chat 模型，需要在pom.xml中引入对应依赖
spring.ai.zhipuai.chat.options.model=GLM-4-Flash
```

在项目pom.xml中导入如下依赖，该依赖将用于文档切分操作。

```
<!-- spring-ai-client-chat 中包括 TokenTextSplitter、TextReader、Document 等工具 -->
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-client-chat</artifactId>
    <version>1.0.0</version>
</dependency>
```

**2) 准备本地知识**

在“SpringAIEmbedding”项目的resources资源目录下放入“古代诗歌常用意向.txt”文件，该步骤在上个案例中已经操作，可忽略。

**3) 准备Service及对应方法**

在“SpringAIEmbedding”项目中创建service/RagService2.java文件，写入如下内容：

```
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
public class RagService2 {
    private final EmbeddingModel em;//嵌入模型，用于生成文本的向量
    private final ChatClient chatClient;// 聊天客户端，用于与 AI 进行交互
    private final List<String> docs = new ArrayList<>(); //存储本地文档内容
    private final List<float[]> vectors = new ArrayList<>(); //存储本地文档向量

    public RagService2(EmbeddingModel embeddingModel, ChatClient.Builder chatBuilder) throws Exception {
        this.em = embeddingModel;
        this.chatClient = chatBuilder.build();// 创建 智普AI Chat 聊天客户端

        // 1. 从 resources 读取长文本文档
        var resource = new ClassPathResource("古代诗歌常用意象.txt");
        // 创建 TextReader 并读取文档内容
        TextReader reader = new TextReader(resource);
        List<Document> rawDocs = reader.read();

        // 2. 使用 TokenTextSplitter 工具将长文本拆分为合理大小片段
        TokenTextSplitter splitter = TokenTextSplitter.builder()
                .withChunkSize(800)            // 拆分每段最多 800 token
                .withMinChunkSizeChars(400)    // 每段最小允许 400 字符
                .withKeepSeparator(true)       // 保留分隔符，提高上下文连贯
                .build();
        //按照设置的参数拆分长文本为多个Chunk
        List<Document> chunks = splitter.apply(rawDocs);

        // 3. 遍历每个 chunk，生成 embedding 并存储
        for (Document d : chunks) {
            // 获取文本内容，并去除首尾空白
            String text = d.getText().strip();
            System.out.println("text: " + text);
            if (text.isBlank()) continue;
            docs.add(text);// 存储切分的文档内容
            vectors.add(em.embed(text));// 将文档生成 embedding 并存储
        }
    }

    public String answer(String q) {
        // 4. 将用户提问 embedding
        float[] qVec = em.embed(q);

        // 5. 计算每个 chunk 与提问的相似度，并找出前 K 高
        int K = 5;
        double threshold = 0.05;  // 相似度阈值
        // 该列表用于存储 大于阈值的 chunk 信息(chunk索引和相似度）
        List<IndexSim> sims = new ArrayList<>();
        for (int i = 0; i < vectors.size(); i++) {
            // 计算用户问题向量与切分的每个文档向量的余弦相似度
            double sim = cosineSimilarity(qVec, vectors.get(i));
            if (sim >= threshold) {
                sims.add(new IndexSim(i, sim));
            }
        }

        // 按相似度降序排序
        sims.sort((a, b) -> Double.compare(b.sim, a.sim));
        // 取相似度最高的前 K 个片段
        List<IndexSim> topKs = sims.stream().limit(K).collect(Collectors.toList());

        // 6. 扩展上下文范围：在每个匹配片段基础上加入其前后相邻片段
        // 为了避免重复，使用 TreeSet 来存储所有需要的片段索引，TreeSet 会自动去重并排序，保持从小到大顺序
        Set<Integer> idxSet = new TreeSet<>();
        for (IndexSim is : topKs) {
            idxSet.add(is.index);
            if (is.index - 1 >= 0) idxSet.add(is.index - 1);
            if (is.index + 1 < docs.size()) idxSet.add(is.index + 1);
        }

        // 7. 将这些片段拼接成最终上下文内容，用于生成回答
        String context = idxSet.stream()
                .map(docs::get)
                .collect(Collectors.joining("\n---\n"));
        String prompt = "以下是相关知识片段：\n" + context + "\n请基于这些内容回答问题：“" + q + "”";

        // 8. 调用 ChatClient 生成回答
        var response = chatClient
                .prompt()
                .system("你是一个知识助手，请结合上下文进行回答")
                .user(prompt)
                .call();

        // 返回大模型的回答结果
        return response.content();
    }

    // 辅助类：记录 index 与 相似度
    static class IndexSim {
        int index;// 文档索引
        double sim;// 相似度
        IndexSim(int index, double sim) {
            this.index = index;
            this.sim = sim;
        }
    }

    // 余弦相似度实现
    // 计算两个向量之间的余弦相似度，数值范围在 [-1, 1] 之间
    // 相似度越高，越接近1，越接近0，越不相似
    private double cosineSimilarity(float[] a, float[] b) {
        double dot = 0, na = 0, nb = 0;
        for (int i = 0; i < a.length; i++) {
            dot += a[i] * b[i];
            na += a[i] * a[i];
            nb += b[i] * b[i];
        }
        return dot / (Math.sqrt(na) * Math.sqrt(nb));
    }
}

```

以上代码中使用TextReader读取文档内容并通过TokenTextSplitter指定文本拆分规则，相比于上个案例改进了文档分段逻辑。并且在用户输入问题后，进行向量相似度比对时加入了0.05阈值，基于此只获取Top5相关分段且获取这些片段的前后相关片段都作为上下文交由模型组织回复。

注意：以上代码中“.withChunkSize(800) 设置的是 token 最大数”，TokenTextSplitter会按最多 800 个 token 为单位去切片（允许适当 overlap），以确保每段不超过模型输入限制，拆分得到的若干 tokenchunk，还会进行合并，合并后，“.withMinChunkSizeChars(400) ”会被用作 字符下限检查，如果合并后一段文本少于 400 个字符，它会尝试和下一段合并，确保至少满足最小字符要求。

**4) 准备Controller及对应方法**

在“SpringAIEmbedding”项目中创建controller/RagController.java文件，写入如下内容：

```
@Autowired
private RagService2 ragService2;

@GetMapping("/ask2")
public Map<String, String> ask2(@RequestParam("question") String question) {
    String answer = ragService2.answer(question);
    return Map.of("question", question, "answer", answer);
}
```

**5) 启动项目并测试**

重新启动“SpringAIEmbedding”项目，然后浏览器输入如下内容进行RAG本地知识库检索测试:

```
http://localhost:8080/rag/ask2?question=古代诗歌常用意象有哪些？
```

可以看到答案如下，本次经过优化的RAG本地知识库检索回复比较全面：

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/a7cdef5fef5e42caacc363a27533f088.jpg)

## 2.3. **Image Models**

Spring 的图像模型 API 旨在提供一个简单且可移植的接口，用于与各种专注于图像生成的 AI 模型交互。该 API 允许开发者在不同的图像模型之间切换时，仅需做最小的代码修改。

Spring AI通过配套类如 ImagePrompt（用于封装输入）和 ImageResponse（用于处理输出），图像模型 API 实现了与图像生成 AI 模型之间通信的统一化，向开发者提供直接简洁的 API 接口来调用图像生成功能。

### **2.3.1. Image Models案例-生成图片**

如下在Spring AI 中使用智普AI来生成图片，按照如下步骤实现。

**1) 创建SpringBoot项目，命名为“SpringAIImage”**

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/5bd390a5594d446f83f9f53f8e2285d9.jpg)

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/8249333e26844bfaa0408d9349addbc7.jpg)

**2) 配置项目pom.xml**

pom.xml内容如下：

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.5.0</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.example</groupId>
    <artifactId>SpringAIImage</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>SpringAIImage</name>
    <description>SpringAIImage</description>

    <properties>
        <java.version>17</java.version>
    </properties>

    <!-- 导入 Spring AI BOM，用于统一管理 Spring AI 依赖的版本，
    引用每个 Spring AI 模块时不用再写 <version>，只要依赖什么模块 Mavens 自动使用 BOM 推荐的版本 -->
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.ai</groupId>
                <artifactId>spring-ai-bom</artifactId>
                <version>1.0.0-SNAPSHOT</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>


    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.ai</groupId>
            <artifactId>spring-ai-starter-model-zhipuai</artifactId>
        </dependency>
    </dependencies>

    <!-- 声明仓库， 用于获取 Spring AI 以及相关预发布版本-->
    <repositories>
        <repository>
            <id>spring-snapshots</id>
            <name>Spring Snapshots</name>
            <url>https://repo.spring.io/snapshot</url>
            <releases>
                <enabled>false</enabled>
            </releases>
        </repository>
        <repository>
            <name>Central Portal Snapshots</name>
            <id>central-portal-snapshots</id>
            <url>https://central.sonatype.com/repository/maven-snapshots/</url>
            <releases>
                <enabled>false</enabled>
            </releases>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
    </repositories>

</project>
```

特别注意：需要在“SpringAIImage”项目的pom.xml中导入“spring-ai-starter-model-zhipuai”依赖。

**3) 配置resources/application.properties**

```
spring.application.name=SpringAIImage


#使用 智普AI Image 模型，需要在pom.xml中引入对应依赖
spring.ai.zhipuai.api-key=ddd2557084494c46ss6xxxGFUGA4
spring.ai.zhipuai.base-url=https://open.bigmodel.cn/api/paas
# CogView-3-Flash 为免费图像生成模型，其他模型需要付费，具体可以参考：https://www.bigmodel.cn/pricing
spring.ai.zhipuai.image.options.model=CogView-3-Flash
```

**4) 创建Service包并创建ImageService类**

在“SpringAIImage”项目中创建“service”包，在该包下编写“ImageService.java”类，该类中实现generateImage方法来将用户输入的文本内容通过智普AI Image模型生成图片。

```
package com.example.springaiimage.service;

import org.springframework.ai.image.*;
import org.springframework.ai.zhipuai.ZhiPuAiImageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageService {
    @Autowired
    private ZhiPuAiImageModel imageModel;

    public Image generateImage(String prompt) {
        ImageOptions options = ImageOptionsBuilder.builder()
                //.model("cogview-3")
                .N(1) // 生成的图片数量
                .width(512) // 图片宽度
                .height(512)// 图片高度
                .build();

        // 创建 ImagePrompt 对象，用于封装本次图像生成请求
        ImagePrompt ip = new ImagePrompt(
                java.util.List.of(new org.springframework.ai.image.ImageMessage(prompt)),
                options
        );

        //调用 ZhiPuAiImageModel 的 call() 方法，传入 ImagePrompt
        ImageResponse resp = imageModel.call(ip);
        System.out.println("返回图片信息"+resp.getResult().getOutput());
        return resp.getResult().getOutput();
    }
}

```

**5) 创建Controller包并创建ImageController类**

在“SpringAIImage”项目中创建“controller”包，在该包下编写“ImageController.java”类，该类中实现genRedirect方法，该方法会将用户输入的文本内容调用ImageService.generateImage方法生成图片，并将该图片的url返回给浏览器进行重定向。

```
package com.example.springaiimage.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.image.*;
import com.example.springaiimage.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/ai")
public class ImageController {
    @Autowired
    private ImageService svc;

    @GetMapping("/generateImage")
    public void genRedirect(
            @RequestParam(value = "prompt", defaultValue = "画一只小狗") String prompt,
            HttpServletResponse response
    ) throws IOException {
        //调用service生成图片 Image 对象
        Image image = svc.generateImage(prompt);
        //获取图片的url
        String url = image.getUrl();
        //直接将浏览器重定向到该URL
        if (url == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "图片生成失败");
        } else {
            // 直接重定向，浏览器会去加载该 URL 并展示图像
            response.sendRedirect(url);
        }
    }

}

```

**6) 启动项目并测试**

启动项目后，浏览器输入“http://localhost:8080/ai/generateImage?prompt=两个中国美女”，可以看到项目返回图片内容如下：

```
返回图片信息Image{url='https://aigc-files.bigmodel.cn/api/cogview/202506181536164147c44d280d478f_0.png', b64Json='null'}
```

浏览器重定向显示图片如下：

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/ce46cdbb8f784c5793ebacec150b42e8.jpg)

## 2.4. **Audio Models**

Spring AI 中的Audio Models部分分为两部分：文本转语音（Text-to-Speech）和音频转文字（Transcription）。目前这两部分主要对Open AI 供应商支持，通过Open AI中的 TTS 模型（tts-1/tts-1-hd）将文本转语音，通过 OpenAI中的 Whisper-1 模型将音频转文字，两者均通过统一配置、bean 自动注入、同步/流式调用方式提供易用、可扩展的 API 接口。

关于Open AI 的Api Key 可以通过OpeanAI 官网购买（国内有封号风险），这里也可以某宝自行搜索Api Key 获取一些商家提供的中转key，也可以使用Open AI相关模型。

使用Open AI 中语音转换相关模型费用比较贵，关于Open AI中模型调用价格可以参考：[https://platform.openai.com/docs/pricing](https://platform.openai.com/docs/pricing)

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/f1b5256354b6428bbd01ebba736192eb.jpg)

### **2.4.1. 文本转语音**

Spring AI 抽象出 SpeechModel 接口进行兼容未来多种模型供应商，以便进行文本转语音。目前该接口仅有OpenAiAudioSpeechModel一个实现类，只能使用Open AI中tts-1 或者 tts-1-hd 模型进行文本转语音操作。

如下案例中创建SpringBoot项目，并构建对应Controller和Service实现文本转语音操作。

**1) 创建SpringBoot项目，命名为“SpringAIAudio”**

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/b76db854fbb0499aae5f8e1f8551d9fe.jpg)

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/6acfb0080af54650a3c409fec1e5690b.jpg)

**2) 配置项目pom.xml**

pom.xml内容如下：

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.5.0</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.example</groupId>
    <artifactId>SpringAIAudio</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>SpringAIAudio</name>
    <description>SpringAIAudio</description>

    <properties>
        <java.version>17</java.version>
    </properties>

    <!-- 导入 Spring AI BOM，用于统一管理 Spring AI 依赖的版本，
    引用每个 Spring AI 模块时不用再写 <version>，只要依赖什么模块 Mavens 自动使用 BOM 推荐的版本 -->
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.ai</groupId>
                <artifactId>spring-ai-bom</artifactId>
                <version>1.0.0-SNAPSHOT</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.ai</groupId>
            <artifactId>spring-ai-starter-model-openai</artifactId>
        </dependency>
    </dependencies>

    <!-- 声明仓库， 用于获取 Spring AI 以及相关预发布版本-->
    <repositories>
        <repository>
            <id>spring-snapshots</id>
            <name>Spring Snapshots</name>
            <url>https://repo.spring.io/snapshot</url>
            <releases>
                <enabled>false</enabled>
            </releases>
        </repository>
        <repository>
            <name>Central Portal Snapshots</name>
            <id>central-portal-snapshots</id>
            <url>https://central.sonatype.com/repository/maven-snapshots/</url>
            <releases>
                <enabled>false</enabled>
            </releases>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
    </repositories>

</project>
```

特别注意：需要在“SpringAIAudio”项目的pom.xml中导入“spring-ai-starter-model-openai”依赖。

**3) 配置resources/application.properties**

```
spring.application.name=SpringAIAudio

#文本转语音文件存放的路径
tts.audio-output-dir=D:\\idea_space\\SpringAICode\\SpringAIAudio\\src\\main\\audio-output

#使用 Open AI 相关模型，实现文字转语音功能、语音转文字功能
spring.ai.openai.base-url=https://api.uchat.site/
spring.ai.openai.api-key=sk-...8A

#使用 Open AI 相关模型，实现文字转语音功能
spring.ai.openai.audio.speech.options.model=tts-1
#指定合成的语音，可以设置为alloy, echo, fable, onyx, nova 和 shimmer
spring.ai.openai.audio.speech.options.voice=alloy

```

注意：“tts.audio-output-dir”配置项为用户自定义配置参数，指定的目录用于存储生成的音频文件。

**4) 创建Service包并创建TtsService类**

在“SpringAIAudio”项目中创建“service”包，在该包下编写“TtsService.java”类，该类中实现textToFile方法来将用户输入的文本转换成mp3文件存入配置的目录中。

```
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.ai.openai.audio.speech.SpeechPrompt;
import org.springframework.ai.openai.audio.speech.SpeechResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class TtsService {

    // 注入 OpenAI 的语音模型对象
    @Autowired
    private OpenAiAudioSpeechModel speechModel;

    // 从配置文件中读取语音输出目录
    @Value("${tts.audio-output-dir}")
    private String audioOutputDir;

    /**
     * 将文本转换为 MP3 文件并保存到配置的目录，返回文件路径
     */
    public String textToFile(String text) throws IOException {
        // 构建语音合成的配置项
        OpenAiAudioSpeechOptions options = OpenAiAudioSpeechOptions.builder()
                //.model("tts-1")
                .voice(OpenAiAudioApi.SpeechRequest.Voice.ALLOY) // 使用 alloy 声音
                .responseFormat(OpenAiAudioApi.SpeechRequest.AudioResponseFormat.MP3) // 音频格式为 mp3
                .speed(1.0f)// 设置语速为 1.0（正常速度）
                .build();

        // 创建语音请求提示对象
        SpeechPrompt prompt = new SpeechPrompt(text, options);
        // 调用模型生成语音
        SpeechResponse response = speechModel.call(prompt);

        System.out.println("response: " + response);

        // 获取音频二进制内容
        byte[] audio = response.getResult().getOutput();

        // 使用配置的目录保存文件
        File dir = new File(audioOutputDir);

        // 确保输出目录存在，不存在则创建
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 创建输出文件名，使用当前时间戳防止重复
        File out = new File(dir, "tts-" + System.currentTimeMillis() + ".mp3");

        // 将音频内容写入文件
        try (FileOutputStream fos = new FileOutputStream(out)) {
            fos.write(audio);
        }

        // 返回生成的文件完整路径
        return out.getAbsolutePath();
    }
}

```

**5) 创建Controller包并创建TtsController类**

在“SpringAIAudio”项目中创建“controller”包，在该包下编写“TtsController.java”类，该类中实现tts方法，该方法会将用户输入的文本内容调用TtsService.textToFile方法转换成语音文件。

```
import com.example.springaiaudio.service.TtsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/ai")
public class TtsController {
    @Autowired
    private TtsService ttsService;

    @GetMapping("/tts")
    public ResponseEntity<String> tts(@RequestParam("text") String text) {
        try {
            String filePath = ttsService.textToFile(text);
            return ResponseEntity.ok("文本转语音成功，文件保存路径：" + filePath);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("文本转语音失败：" + e.getMessage());
        }
    }
}
```

**6) 启动项目并测试**

启动项目后，浏览器输入“http://localhost:8080/ai/tts?text=在这个信息化时代，人工智能技术深刻地改变着我们的生活方式和工作模式。”，可以看到项目返回内容如下：

```
文本转语音成功，文件保存路径：D:\idea_space\SpringAICode\SpringAIAudio\src\main\audio-output\tts-1750sss224.mp3
```

检查对应的目录找到转换的mp3播放后可以听到对应输入的文字已经被转换成语音。

### **2.4.2. 音频转文字**

Spring AI 提供了OpenAiAudioTranscriptionModel类实现通过Open AI 的Whisper 模型将音频转换成文字，输出的格式可以为json, text, srt, verbose\_json, vtt。

如下案例中在“SpringAIAudio”项目中创建对应Controller和Service实现语音转文字操作。

**1) 创建SpringBoot项目**

这里使用“SpringAIAudio”项目，无需重新创建。

**2) 配置resources/application.properties**

在resources/application.properties文件中追加了“语音转文字”的whisper模型：

```
spring.application.name=SpringAIAudio

#文本转语音文件存放的路径
tts.audio-output-dir=D:\\idea_space\\SpringAICode\\SpringAIAudio\\src\\main\\audio-output

#使用 Open AI 相关模型，实现文字转语音功能、语音转文字功能
spring.ai.openai.base-url=https://api.uchat.site/
spring.ai.openai.api-key=sk-xxx...P1HwVlJDjAQDACCPx8A

#使用 Open AI 相关模型，实现文字转语音功能
spring.ai.openai.audio.speech.options.model=tts-1
#指定合成的语音，可以设置为alloy, echo, fable, onyx, nova 和 shimmer
spring.ai.openai.audio.speech.options.voice=alloy

#使用 Open AI 相关模型，实现语音转文字功能
spring.ai.openai.audio.transcription.options.model=whisper-1

```

注意：“tts.audio-output-dir”配置项为用户自定义配置参数，指定的目录中存储之前生成的mp3音频文件。

**3) 创建Service包并创建TranscribeService类**

在“SpringAIAudio”项目中创建“service”包，在该包下编写“TranscribeService.java”类，该类中实现transcribeFromFile方法来将mp3音频文件转换成文字。

```
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class TranscribeService {

    // 注入 OpenAI 音频转录模型的对象
    @Autowired
    private OpenAiAudioTranscriptionModel transcriptionModel;

    /**
     * 从文件系统中读取配置目录下的 mp3 文件进行转录，并返回对应文本
     * @param fileFullPath mp3 文件全路径
     * @param options      转录时的可选参数（语言、格式等）
     * @return             转写后的文字结果
     */
    public String transcribeFromFile(String fileFullPath, OpenAiAudioTranscriptionOptions options) {
        // 根据传入路径构造 File 对象
        File file = new File(fileFullPath);

        // 校验文件存在且是普通文件，否则抛出异常
        if (!file.exists() || !file.isFile()) {
            throw new IllegalArgumentException("文件不存在: " + file.getAbsolutePath());
        }

        // 将 File 封装为 Resource 对象，适配模型输入
        Resource audio = new FileSystemResource(file);

        // 创建转录请求提示对象，包含音频资源和转录选项
        AudioTranscriptionPrompt prompt = new AudioTranscriptionPrompt(audio, options);
        // 调用模型处理请求，返回响应对象
        AudioTranscriptionResponse resp = transcriptionModel.call(prompt);
        System.out.println("response: " + resp.toString());

        // 从响应中获取第一条结果的文字输出并返回（Whisper 通常只返回一条）
        return resp.getResults().get(0).getOutput();
    }

}
```

**4) 创建Controller包并创建TranscribeController类**

在“SpringAIAudio”项目中创建“controller”包，在该包下编写“TranscribeController.java”类，该类中实现transcribe方法，该方法会将指定目录下的mp3文件传入到TranscribeService.transcribeFromFile方法将语音转换成文本。

```
@RestController
@RequestMapping("/ai")
public class TranscribeController {

    @Autowired
    private TranscribeService service;

    // 从应用配置中读取音频目录，用来存放待转录的 mp3 文件
    @Value("${tts.audio-output-dir}")
    private String audioOutputDir;

    // 定义 GET 类型的 /ai/transcribe 接口，返回转录结果
    @GetMapping("/transcribe")
    public ResponseEntity<Map<String, String>> transcribe() {
        // 创建 File 对象，指向配置的音频目录
        File dir = new File(audioOutputDir);
        if (!dir.exists() || !dir.isDirectory()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "目录不存在或不是文件夹: " + audioOutputDir));
        }

        // 列出所有后缀为 .mp3 的文件
        File[] mp3Files = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".mp3"));
        if (mp3Files == null || mp3Files.length == 0) {
            return ResponseEntity.ok(Map.of("message", "目录下没有 MP3 文件"));
        }

        // 构建 OpenAI 转录选项：文本格式、温度设为 0、语言为中文（zh）
        OpenAiAudioTranscriptionOptions opts = OpenAiAudioTranscriptionOptions.builder()
                .responseFormat(OpenAiAudioApi.TranscriptResponseFormat.TEXT)// 设置输出为文本
                .temperature(0f)// 设置温度为 0，取值范围为 0–1，确保输出稳定，越大不确定度越高
                .language("zh")//指定中文音频
                .build();

        Map<String, String> results = new HashMap<>();
        for (File f : mp3Files) {
            String relPath = f.getName();  // 使用文件名作为 classpathLocation
            try {
                // 调用服务进行转录
                String text = service.transcribeFromFile(f.getAbsolutePath(), opts);
                results.put(relPath, text);
            } catch (Exception e) {
                results.put(relPath, "FAILED: " + e.getMessage());
            }
        }

        // 返回所有文件的转录结果
        return ResponseEntity.ok(results);
    }
}

```

**5) 启动项目并测试**

启动项目后，浏览器输入“http://localhost:8080/ai/transcribe”，可以看到项目返回内容如下，已经将音频正确的转换成文字。

```
{
  "tts-1750xxx74224.mp3": "在这个信息化时代,人工智能技术深刻地改变着我们的生活方式和工作模式。\n"
}
```

# 3. **Spring AI结合Ollama**

Ollama 是一个开源的大型语言模型（LLM）平台，Ollama 提供了简洁易用的命令行界面和服务器，使用户能够轻松下载、运行和管理各种开源 LLM，通过 Ollama，用户可以方便地加载和使用各种预训练的语言模型，支持文本生成、翻译、代码编写、问答等多种自然语言处理任务。

ollama官网：[https://ollama.com](https://ollama.com/)

Spring AI支持使用Ollama管理的模型，下面介绍Spring AI中如何使用OllamaChat和Ollama Embeddings。这里默认已经安装好ollama。

## 3.1. **Ollama下载与安装**

Ollama下载地址:https://github.com/ollama/ollama,这里以window中下载为例：

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/4f9fb3960a304e4e94f2a0c6060df23d.jpg)

下载完成后双击“OllamaSetup.exe”进行安装，默认安装在“C\\users\\{user}\\AppData\\Local\\Programs”目录下，建议C盘至少要有10G 剩余的磁盘空间，因为后续Ollama中还要下载其他模型到相应目录中。

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/721db03a57724526827e50f91658c5a0.jpg)

安装完成后，电脑右下角自动有“Ollama”图标并开机启动。可以在cmd中运行模型进行会话

```
#命令 ollama run +模型 deepseek-r1:1.5b
ollama run deepseek-r1:1.5b
>>> 你是谁？
<think>
</think>
您好！我是由中国的深度求索（DeepSeek）公司开发的智能助手DeepSeek-R1。如您有任何任何问题，我会尽我所能为您提供帮助。
>>>
```

注意：使用run命令第一次运行模型，如果模型不存在会自动下载，然后进入模型交互窗口。后续使用会自动进入到交互窗口。

在浏览器中输入“localhost:11434”可以访问ollama，输出“Ollama is running”。默认情况下，Ollama 服务仅监听本地回环地址（127.0.0.1），这意味着只有在本地计算机上运行的应用程序才能访问该服务，如果想要使用“windows ip:11434”访问ollama需要在环境变量中加入“OLLAMA\_HOST”为“0.0.0.0”,这样就将 Ollama 的监听地址设置为 0.0.0.0，使其监听所有网络接口，包括本机 IP 地址。

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/f2391be5b2dd4f28b7e1bf5fbda3173b.jpg)

注意：以上环境变量设置完成后，需要重启ollama。

## 3.2. **Ollama Chat**

下面以Spring AI中通过与Ollama中模型对话为例，演示Spring AI相关配置。

**1) 创建SpringBoot项目，命名为“SpringAIOllama”**

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/458a6bdd6a9540969dbe10263098e99e.jpg)

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/df53ca580e51473db025d6bf43c5a112.jpg)

**2) 配置项目pom.xml**

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.5.0</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.example</groupId>
    <artifactId>SpringAIOllama</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>SpringAIOllama</name>
    <description>SpringAIOllama</description>

    <properties>
        <java.version>17</java.version>
    </properties>

    <!-- 导入 Spring AI BOM，用于统一管理 Spring AI 依赖的版本，
   引用每个 Spring AI 模块时不用再写 <version>，只要依赖什么模块 Mavens 自动使用 BOM 推荐的版本 -->
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.ai</groupId>
                <artifactId>spring-ai-bom</artifactId>
                <version>1.0.0-SNAPSHOT</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>


    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.ai</groupId>
            <artifactId>spring-ai-starter-model-ollama</artifactId>
        </dependency>

    </dependencies>

    <!-- 声明仓库， 用于获取 Spring AI 以及相关预发布版本-->
    <repositories>
        <repository>
            <id>spring-snapshots</id>
            <name>Spring Snapshots</name>
            <url>https://repo.spring.io/snapshot</url>
            <releases>
                <enabled>false</enabled>
            </releases>
        </repository>
        <repository>
            <name>Central Portal Snapshots</name>
            <id>central-portal-snapshots</id>
            <url>https://central.sonatype.com/repository/maven-snapshots/</url>
            <releases>
                <enabled>false</enabled>
            </releases>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
    </repositories>

</project>

```

**3) 配置resources/application.properties**

```
spring.application.name=SpringAIOllama

server.port=8080

#配置 Ollama 的基础 URL 和使用模型
spring.ai.ollama.base-url=http://localhost:11434
spring.ai.ollama.chat.options.model=deepseek-r1:1.5b
spring.ai.ollama.chat.options.temperature=0.8

```

### **3.2.1. 基本聊天**

在“SpringAIOllama”项目中创建“controller”包，在该包下编写“ChatController.java”类，实现与Ollama中模型的基本聊天功能。

```
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
public class ChatController {
    @Autowired
    private DeepSeekChatModel chatModel;

    @GetMapping("/generate")
    public String generate(@RequestParam(value = "message", defaultValue = "给我讲个笑话") String message) {
        System.out.println("收到消息："+message);
        String result = chatModel.call(message);
        //模型返回的内容
        System.out.println(result);
        return result;
    }

}
```

启动项目后，浏览器输入“http://localhost:8080/ai/generate?message=你是谁”，可以看到输出内容如下：

```
我是DeepSeek Chat，由深度求索公司（DeepSeek）开发的智能AI助手！✨ 我可以帮你解答问题、提供建议、陪你聊天，还能处理各种文本、文档等内容。无论是学习、工作，还是日常生活中的疑问，都可以来问我哦！😊 有什么我可以帮你的吗？
```

### **3.2.2. Stream流式聊天**

Sparing AI 使用Ollama中的模型与Chat Models-deepseek部分一样，直接在“SpringAIOllama”项目中“/controller/ChatController.java”中准备如下代码实现Stream流式聊天：

```
//流式获取模型返回的内容，获取返回的 Text 内容
@GetMapping("/generateStream2")
public Flux<String> generateStream2(
        @RequestParam(value = "message", defaultValue = "给我讲个笑话") String message,
        HttpServletResponse response) {
    // 避免返回乱码
    response.setCharacterEncoding("UTF-8");

    System.out.println("收到消息："+message);
    var prompt = new Prompt(new UserMessage(message));
    Flux<String> result = chatModel.stream(prompt)
            .map(chatResponse -> chatResponse.getResult().getOutput().getText());

    return result;
}
```

重新启动项目后，可以在浏览器输入“http://localhost:8080/ai/generateStream2?message=你是谁”查看模型Stream流式返回结果。

### **3.2.3. 运行时参数设置**

Spring AI 使用Ollama中的模式时也支持运行参数设置，可以在调用模型时，创建一个prompt，并嵌入一个新的OllamaOptions来覆盖启动配置。

在“SpringAIOllama”项目的“/controller/ChatController.java”代码中准备如下方法：

```
//运行时参数设置
@GetMapping("/runtimeOptions")
public String runtimeOptions(
        @RequestParam(value = "message") String message,
        @RequestParam(value = "temp", required = false) Double temp
) {
    System.out.println("收到消息："+message);

    Prompt prompt;
    if (temp != null) {
        // 构建带 temperature 的 DeepSeekChatOptions，覆盖默认 temperature
        var opts = OllamaOptions.builder()
                .temperature(temp)
                .build();
        prompt = new Prompt(message, opts);
        System.out.println("使用运行时覆盖 temperature=" + temp);
    } else {
        // 无 temperature 传入时，使用默认配置
        prompt = new Prompt(message);
        System.out.println("使用默认 temperature");
    }

    ChatResponse resp = chatModel.call(prompt);
    String result = resp.getResult().getOutput().getText();
    System.out.println("模型返回："+ result);
    return result;
}

```

以上代码编写好后，重启项目，可以传入不同的参数：

“http://localhost:8080/ai/runtimeOptions?message=1加1等于几&temp=0.1”

## 3.3. **Ollama Embeddings**

Ollama Embeddings 是基于 Spring AI 中 EmbeddingModel 接口的实现，使用 Ollama 本地部署的模型生成文本嵌入（embeddings）。使用Ollam Embeddings你需要准备如下内容：

* 本地安装Ollama
* Ollama中拉取下载了Embedding模型

### **3.3.1. 下载Embedding模型**

Ollama支持很多模型，可以通过“https://ollama.com/library”查看可用的模型列表，在本机Ollama运行7B模型至少需要8GB内存；运行13B模型至少需要16G内存；运行33B模型至少需要32G内存。

注意："7B"、"13B" 和 "33B" 分别表示模型中参数的数量级，分别为 70 亿、130 亿和 330 亿个参数。模型的参数数量直接影响其对内存的需求，参数越多，模型越复杂，所需的内存也就越大，运行这些模型时，确保系统具有足够的内存。

这里在Ollama中下载“nomic-embed-text:latest”嵌入模型，命令如下：

```
ollama pull nomic-embed-text
```

### **3.3.2. Embedding案例-查找相似文本**

按照如下步骤完成Ollama Embedding案例，该案例和上一章节中 “Embedding案例1”代码一样，只是换成了Ollama中的Embedding模型，需要准备如下内容：

* 创建SpringBoot项目，引入“spring-ai-starter-model-ollama”相关依赖
* 在resources/application.properties配置中引入Ollama的嵌入模型

**1) 准备Service及对应方法**

在“SpringAIOllama”项目中创建“service”包，并在该包中创建“EmbeddingService.java”类，写入如下内容：

```
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmbeddingService {

    private EmbeddingModel embeddingModel;

    private final List<float[]> docVectors;

    // 1. 准备知识库文本内容
    private final List<String> docs = List.of(
            "美食非常美味，服务员也很友好。",
            "这部电影既刺激又令人兴奋。",
            "阅读书籍是扩展知识的好方法。"
    );

    public EmbeddingService(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
        // 2. 启动时，将所有文档向量化
        this.docVectors = this.embeddingModel.embed(docs);
    }

    /**
     * 输入用户查询，返回最相似文档的索引 （使用余弦相似度计算）
     * @param query 用户输入的查询文本
     * @return 最相似的知识库文本
     */
    public String queryBestMatch(String query) {
        //将用户的输入通过EmbeddingModel转换成向量
        float[] queryVec = embeddingModel.embed(query);

        int bestIdx = -1; //记录目前最匹配文档在列表中的索引位置
        double bestSim = -1; //记录目前的最高相似度

        // 遍历所有文档对应的向量，计算与查询向量的相似度
        for (int i = 0; i < docVectors.size(); i++) {
            //计算余弦相似度
            double sim = cosineSimilarity(queryVec, docVectors.get(i));
            if (sim > bestSim) {
                bestSim = sim;
                bestIdx = i;
            }
        }
        return docs.get(bestIdx);
    }

    // 余弦相似度实现
    // 计算两个向量之间的余弦相似度，数值范围在 [-1, 1] 之间
    // 相似度越高，越接近1，越接近0，越不相似
    private double cosineSimilarity(float[] a, float[] b) {
        double dot = 0, na = 0, nb = 0;
        for (int i = 0; i < a.length; i++) {
            dot += a[i] * b[i];
            na += a[i] * a[i];
            nb += b[i] * b[i];
        }
        return dot / (Math.sqrt(na) * Math.sqrt(nb));
    }

}
```

**2) 准备Controller及对应方法**

在“SpringAIOllama”项目中的“controller/EmbeddingController.java”类中注入如下依赖和增加如下方法：

```
@Autowired
private EmbeddingService service;

//文本相似检测
@GetMapping("/similarity")
public Map<String, String> similarity(@RequestParam("query") String query) {
    String ans = service.queryBestMatch(query);
    return Map.of("query", query, "answer", ans);
}
```

**3) 启动项目并测试**

启动Spring Boot项目，浏览器中输入如下内容进行相似文本查找（由于Embedding模型大小原因，导致匹配不精准）：

```
#输入http://localhost:8080/ai/similarity?query=美食
{
  "query": "美食",
  "answer": "美食非常美味，服务员也很友好。"
}

#输入http://localhost:8080/ai/similarity?query=电影
{
  "answer": "美食非常美味，服务员也很友好。",
  "query": "电影"
}

#输入http://localhost:8080/ai/similarity?query=书籍
{
  "answer": "美食非常美味，服务员也很友好。",
  "query": "书籍"
}
```

# 4. **Chat Client**

在前面“Chat Models”小节内容中，我们在与各个模型进行对话时使用的是对应模型的ChatModel对象，例如：DeepSeekChatModel、OllamaChatModel。然后通过chatModel.call(...)、chatModel.stream(...)直接调用模型，这其中我们需要手动构造Prompt、处理流响应（例如将返回封装成对象、从模型响应中获取回复内容）、拼装调用链（RAG需要自己构建流程）等操作。

以上使用ChatModel与模型进行对话方式中，如果在项目中使用大模型涉及到记忆上下文、Prompt模版化、RAG开发、返回内容映射为实体等操作时，单纯的ChatModel代码量很多，维护成本高，为了简化这个流程，Spring AI 中提供了ChatClient对象，该对象可以看做一个更高级的“客户端 API”，建立在ChatModel之上，可以用链式的方式快速搭配 Prompt、系统设定、变量替换、上下文记忆等，并支持文本/JSON/实体对象等多种形式的输出。

ChatModel和Chat Client对象对比如下：

| **方面**        | **ChatModel**                     | **ChatClient**              |
| --------------------- | --------------------------------------- | --------------------------------- |
| **Prompt构建**  | 需手动new Prompt(...)                   | 链式.prompt().user(...)使用       |
| **流式处理**    | 手动处理流对象                          | .stream().content()简洁获取流内容 |
| **上下文/记忆** | 手动拼接历史上下文                      | 支持内置ChatMemory和Advisor       |
| **RAG架构**     | 需自行编写检索、检索结果推进流程        | 有内置Advisor支持RAG简化          |
| **输出映射**    | .chatResponse()后自己解析JSON映射到实体 | .entity(...)可直接映射实体类      |

**特别注意**：ChatClient 目前只支持 Chat（对话）模型，不包括 Embedding、Image、Audio 等多模态模型要使用 Embedding、Image 或 Audio 等模型，需要直接使用 Spring AI 提供的对应 API，比如 EmbeddingModel、ImageModel、AudioModel。

## 4.1. **使用ChatClient**

下面以使用DeepSeek为例来演示Spring AI中如何使用ChatClient。涉及ChatClient创建、设置提示词、流式回复、回复映射到对象操作。

**1) 创建SpringBoot项目，命名为“SpringAIChatClient”**

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/35f51cc1faae44ab83022ffb4f31a82f.jpg)

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/4232e7656be545609fc20ab4d1983c4d.jpg)

**2) 配置项目pom.xml**

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.5.0</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.example</groupId>
    <artifactId>SpringAIChatClient</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>SpringAIChatClient</name>
    <description>SpringAIChatClient</description>

    <properties>
        <java.version>17</java.version>
    </properties>

    <!-- 导入 Spring AI BOM，用于统一管理 Spring AI 依赖的版本，
    引用每个 Spring AI 模块时不用再写 <version>，只要依赖什么模块 Mavens 自动使用 BOM 推荐的版本 -->
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.ai</groupId>
                <artifactId>spring-ai-bom</artifactId>
                <version>1.0.0-SNAPSHOT</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.ai</groupId>
            <artifactId>spring-ai-starter-model-deepseek</artifactId>
        </dependency>
    </dependencies>


    <!-- 声明仓库， 用于获取 Spring AI 以及相关预发布版本-->
    <repositories>
        <repository>
            <id>spring-snapshots</id>
            <name>Spring Snapshots</name>
            <url>https://repo.spring.io/snapshot</url>
            <releases>
                <enabled>false</enabled>
            </releases>
        </repository>
        <repository>
            <name>Central Portal Snapshots</name>
            <id>central-portal-snapshots</id>
            <url>https://central.sonatype.com/repository/maven-snapshots/</url>
            <releases>
                <enabled>false</enabled>
            </releases>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
    </repositories>

</project>

```

以上pom.xml中引入了“spring-ai-starter-model-deepseek”依赖。

**3) 配置resources/application.properties**

```
spring.application.name=SpringAIChatClient

server.port=8080

#配置 Deepseek的基础URL、密钥和使用模型
spring.ai.deepseek.base-url=https://api.deepseek.com
spring.ai.deepseek.api-key=sk-...c6a821
spring.ai.deepseek.chat.options.model=deepseek-chat
```

**4) 创建pojo包并创建Student.java类**

由于后续与大模型对话时需要自动映射为实体对象，这里创建Student.java类：

```
public class Student {
    private Long id;
    private String name;
    private Integer age;

    public Student() {}

    public Student(Long id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    // Getter 和 Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    @Override
    public String toString() {
        return "Student{id=" + id + ", name='" + name + "', age=" + age + "}";
    }
}
```

**5) 创建controller包，并创建ChatController.java文件**

```
package com.example.springaichatclient.controller;

import com.example.springaichatclient.pojo.Student;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ai")
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder builder) {
        // 在 controller 构造函数中直接建立 ChatClient，
        // 并设置默认 system 提示词
        this.chatClient = builder
                .defaultSystem("你是一个聊天助手，名字叫小智。")
                .build();
    }

    //使用文本响应用户问题、设置Prompt提示词
    @GetMapping("/chat")
    public Map<String, String> chatText(@RequestParam("message") String message) {
        String reply = chatClient.prompt("如果用户让你讲故事，只能讲解神话故事，不能讲其他的。")
                .user(message)
                .call()
                .content();
        return Map.of("reply", reply);
    }


    @GetMapping("/response")
    public ChatResponse chatResponse(@RequestParam("message") String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .chatResponse();
    }

    @GetMapping("/getOneStudent")
    public Student getOneEntity() {
        return chatClient.prompt()
                .user("生成1个Student对象，输出单个JSON对象，字段：id（Long），name（String），age（Integer）")
                .call()
                .entity(Student.class);
    }

    @GetMapping("/getStudentList")
    public List<Student> getStudentList() {
        return chatClient.prompt()
                .user("生成3个Student对象，JSON数组格式，字段：id（Long），name（String），age（Integer）")
                .call()
                .entity(new ParameterizedTypeReference<List<Student>>() {});
    }

    @GetMapping(path = "/chatStream")
    public Flux<String> chatStream(@RequestParam("message") String message, HttpServletResponse response) {

        // 避免返回乱码
        response.setCharacterEncoding("UTF-8");

        return chatClient.prompt()
                .user(message)
                .stream()// 开启流式响应
                .content();
    }
}
```

以上代码中需要注意：defaultSystem(...)是设置全局系统提示词；prompt(...)是设置当前对话提示词；user(...)为用户输入消息内容。

**6) 启动项目并测试**

启动项目后，浏览器输入如下内容进行测试：

```
# http://localhost:8080/ai/chat?message=给我讲个故事
{
  "reply": "好的，我很高兴为您讲一个神话故事。今天我要讲的是中国上古神话《夸父追日》：\n\n很久很久以前，在北方的大荒之中，住着一个名叫夸父的巨人。他是后土神的孙子，信神的儿子。夸父身高如山，力大无穷，耳朵上挂着两条黄蛇，手里也握着两条黄蛇。\n\n那时候，太阳每天从东边升起，西边落下，夸父觉得太阳跑得太快了，大地上的时间太短暂。于是他决定要追上太阳，让它慢下来。\n\n夸父迈开巨大的步伐，开始追逐太阳。他跑啊跑啊，跨过一座座高山，越过一条条大河。太阳在天上跑，夸父在地上追。眼看就要在禺谷追上太阳了，夸父却感到口渴难忍。\n\n他俯下身来，一口气喝干了黄河的水，又喝干了渭河的水，还是觉得口渴。于是他向北跑去，想要喝大泽的水。可是还没跑到大泽，夸父就因为极度干渴而倒下了。\n\n临死前，夸父扔出了他的手杖。那手杖化作了一片桃林，结满了鲜美的桃子，为后来路过的人解渴。\n\n这个故事展现了古人征服自然的雄心壮志，也告诉我们做事要量力而行。您觉得这个神话故事怎么样？"
}


# http://localhost:8080/ai/response?message=你是谁
{
... ...
"results": [
    {
      "metadata": {
        "finishReason": "STOP",
        "contentFilters": [],
        "empty": true
      },
      "output": {
        "messageType": "ASSISTANT",
        "metadata": {
          "finishReason": "STOP",
          "index": 0,
          "id": "f68527d6-6a26-4bbb-89c0-ca849e17b900",
          "role": "ASSISTANT",
          "messageType": "ASSISTANT"
        },
        "toolCalls": [],
        "media": [],
        "prefix": null,
        "reasoningContent": null,
        "text": "你好！我是小智，一个智能聊天助手，随时为你提供帮助和解答问题。无论是日常疑问、学习辅导，还是闲聊放松，我都可以陪你聊聊！有什么我可以帮你的吗？😊"
      }
    }
  ]
}

# http://localhost:8080/ai/getOneStudent
{
  "id": 1,
  "name": "张三",
  "age": 20
}

# http://localhost:8080/ai/getStudentList
[
  {
    "id": 1,
    "name": "张三",
    "age": 20
  },
  {
    "id": 2,
    "name": "李四",
    "age": 21
  },
  {
    "id": 3,
    "name": "王五",
    "age": 22
  }
]
```

## 4.2. **一个项目使用多个聊天模型**

在“SpringAIChatClient”项目中，如果此刻我们需要只用智普AI进行图片的识别，那么就需要在项目pom.xml中引入智普AI相关依赖，然后再在“resources/application.properties”中配置智普AI相关的URL/ApiKey/Model等信息，这样就不能再使用DeepSeek模型，因为默认Spring AI只配置一个ChatClient.Builder。

如果在一个项目中需要使用多个模型，我们可以通过如下方式手动管理多个ChatClient实例，步骤如下：

**1. 在SpringBoot项目中引入多个模型的依赖包**

**2. 关闭ChatClient.Builder 自动创建，改为手动管理ChatClient创建**

在resources/application.properties配置文件中配置“spring.ai.chat.client.enabled=false”，该配置项默认为true，Spring Boot 会自动创建一个 ChatClient.Builder Bean 可以直接注入使用，手动管理 ChatClient 的创建需要设置为false，即不再使用ChatClient.Builder Bean 注入

**3. 自定义配置类实现多模型ChatClient创建**

如下：

```
@Configuration
public class ChatClientConfig {
    @Bean
    public ChatClient deepseekClient(OpenAiChatModel openAiChatModel) {
        return ChatClient.create(openAiChatModel);
    }

    @Bean
    public ChatClient zhipuaiClient(ZhiPuAIChatModel zhipuAiChatModel) {
        return ChatClient.create(zhipuAiChatModel);
    }
}
```

通过@Configuration 告诉 Spring：这是配置类，类里的 @Bean 方法会被执行，返回的对象将注册为 Spring 管理的 Bean。

**4. Controller中引入多个ChatClient**

如下：

```
private final ChatClient deepseekClient;
private final ChatClient zhipuaiClient;

public MultiModelController(
      @Qualifier("deepseekClient") ChatClient deepseekClient,
      @Qualifier("zhipuaiClient") ChatClient zhipuaiClient) {
  this.deepseekClient = deepseekClient;
  this.zhipuaiClient = zhipuaiClient;
}
```

在 Spring（包括 Spring Boot）中，@Qualifier 注解用于解决多个相同类型 Bean 注入时的歧义问题，当容器中存在多个 ChatClient Bean 时，Spring 无法自动识别到底注入哪一个，这时就需要 @Qualifier 来指定 Bean 名称。例如：@Qualifier("deepseekClient") 告诉 Spring，“从容器中找到名字为 deepseekClient 的 Bean 注入此参数”。

### **4.2.1. 创建项目并配置多模型**

下面在一个SpringBoot项目中使用DeepSeek、智普AI两个模型，DeepSeek模型用于聊天，智普AI模型用于图像内容识别，步骤如下：

**1) 创建SpringBoot项目，命名为“SpringAIChatClient”**

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/dd41e1da274842cab9eccdee0b7f8c83.jpg)

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/d51a495167eb421492f5df8d5dabc884.jpg)

**2) 配置项目pom.xml**

在项目的pom.xml中引入了deepseek和zhipuai相关的依赖包。

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.5.0</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.example</groupId>
    <artifactId>SpringAIMutiModelClient</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>SpringAIMutiModelClient</name>
    <description>SpringAIMutiModelClient</description>

    <properties>
        <java.version>17</java.version>
    </properties>

    <!-- 导入 Spring AI BOM，用于统一管理 Spring AI 依赖的版本，
    引用每个 Spring AI 模块时不用再写 <version>，只要依赖什么模块 Mavens 自动使用 BOM 推荐的版本 -->
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.ai</groupId>
                <artifactId>spring-ai-bom</artifactId>
                <version>1.0.0-SNAPSHOT</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- 引入 DeepSeek 模型 -->
        <dependency>
            <groupId>org.springframework.ai</groupId>
            <artifactId>spring-ai-starter-model-deepseek</artifactId>
        </dependency>

        <!-- 引入 智普AI 模型-->
        <dependency>
            <groupId>org.springframework.ai</groupId>
            <artifactId>spring-ai-starter-model-zhipuai</artifactId>
        </dependency>

    </dependencies>


    <!-- 声明仓库， 用于获取 Spring AI 以及相关预发布版本-->
    <repositories>
        <repository>
            <id>spring-snapshots</id>
            <name>Spring Snapshots</name>
            <url>https://repo.spring.io/snapshot</url>
            <releases>
                <enabled>false</enabled>
            </releases>
        </repository>
        <repository>
            <name>Central Portal Snapshots</name>
            <id>central-portal-snapshots</id>
            <url>https://central.sonatype.com/repository/maven-snapshots/</url>
            <releases>
                <enabled>false</enabled>
            </releases>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
    </repositories>

</project>

```

**3) 配置resources/application.properties**

在配置文件中设置spring.ai.chat.client.enabled=false，关闭ChatClient.Builder自动创建。这里引入了DeepSeek的deepseek-chat聊天模型和智普AI的GLM-4V-Flash图像理解模型。

```
spring.application.name=SpringAIMutiModelClient

# 默认为true，Spring Boot 会自动创建一个 ChatClient.Builder Bean 可以直接注入使用
# 手动管理 ChatClient 的创建需要设置为false，即不再使用ChatClient.Builder Bean 注入
spring.ai.chat.client.enabled=false

#配置 Deepseek的基础URL、密钥和使用模型
spring.ai.deepseek.base-url=https://api.deepseek.com
spring.ai.deepseek.api-key=sk-...21
spring.ai.deepseek.chat.options.model=deepseek-chat

#使用 智普AI Image 模型，需要在pom.xml中引入对应依赖
spring.ai.zhipuai.api-key=dd...GA4
spring.ai.zhipuai.base-url=https://open.bigmodel.cn/api/paas
# 图像识别 / 图像理解功能（多模态模型）
# GLM-4V-Flash 为免费图像理解模型，其他模型需要付费，具体可以参考：https://www.bigmodel.cn/pricing
spring.ai.zhipuai.chat.options.model=GLM-4V-Flash
```

**4) 创建config包并创建聊天配置类**

在项目中创建config包，并在该包下创建ChatClientConfig类，实现多模型ChatClient的创建。

ChatClientConfig.java内容如下，可以设置系统级别提示词，也可以不设置。

```
package com.example.springaimutimodelclient.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.zhipuai.ZhiPuAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {
    @Bean
    public ChatClient deepseekClient(DeepSeekChatModel deepSeekChatModel) {
        // 创建DeepSeekChatModel实例
        //return ChatClient.create(deepSeekChatModel);
        return ChatClient.builder(deepSeekChatModel)
                .defaultSystem("你是一个人工智能助手，你的名字叫做小智，可以回答任何问题。")
                .build();
    }

    @Bean
    public ChatClient zhipuaiClient(ZhiPuAiChatModel zhipuAiChatModel) {
        // 创建ZhiPuAiChatModel实例
        return ChatClient.create(zhipuAiChatModel);
    }

}
```

### **4.2.2. 多模型聊天示例**

**1) 创建controller包，并创建MultiModelController.java文件**

在该文件中，引入配置文件中配置的两个ChatClient，可以在不同的controller方法中使用对应模型的ChatClient来进行对话。

```
package com.example.springaimutimodelclient.controller;

import com.example.springaimutimodelclient.pojo.IdCardInfo;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/ai")
public class MultiModelController {
    private final ChatClient deepseekClient;
    private final ChatClient zhipuaiClient;

    public MultiModelController(
            @Qualifier("deepseekClient") ChatClient deepseekClient,
            @Qualifier("zhipuaiClient") ChatClient zhipuaiClient) {
        this.deepseekClient = deepseekClient;
        this.zhipuaiClient = zhipuaiClient;
    }

    @GetMapping("/deepseek")
    public String chatWithDeepseek(@RequestParam("message") String message) {
        return deepseekClient.prompt()
                .user(message)
                .call()
                .content();
    }

    @GetMapping("/zhipuai")
    public String chatWithZhipuai(@RequestParam("message") String message) {
        return zhipuaiClient.prompt()
                .user(message)
                .call()
                .content();
    }
}
```

**2) 启动项目并测试**

启动项目后，浏览器输入如下内容进行测试：

```
# http://localhost:8080/ai/deepseek?message=你是谁
你好！我是小智，一个智能AI助手，随时准备为你解答问题、提供帮助或陪你聊天。无论是学习、工作，还是日常生活中的疑问，都可以问我哦！有什么我可以帮你的吗？ 

# http://localhost:8080/ai/zhipuai?message=你是谁
我是一个名叫智谱清言的人工智能助手，我的使命是帮助人们解决问题和提供信息。我由智谱AI公司开发，使用的是大型语言模型GPT-4。我的目标是尽可能准确地回答问题并提供有用的信息。如果您有任何其他问题或需要进一步的帮助，请随时告诉我。谢谢！
```

### **4.2.3. 图片内容识别示例**

**1) 准备图片**

将图片“img.png”和“身份证.jpg”上传至项目“SpringAIMutiModelClient”的resources资源目录下。

![](//:0)![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/92e3e39836564f6c944f57ef3e538fb3.jpg)

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/255181ad4fde4badb7aaea7b93ae45f8.jpg)

**2) 创建pojo包并创建IdCardInfo.java类**

由于后续与大模型对话记性身份证识别时需要自动映射为实体对象，这里创建IdCardInfo.java类：

```
package com.example.springaimutimodelclient.pojo;

public class IdCardInfo {
    private String name;
    private String sex;
    private String nation;
    private String birth;
    private String address;
    private String idNo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    @Override
    public String toString() {
        return "IdCardInfo{" +
                "name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", nation='" + nation + '\'' +
                ", birth='" + birth + '\'' +
                ", address='" + address + '\'' +
                ", idNo='" + idNo + '\'' +
                '}';
    }
}
```

**3) 创建controller包，并创建 MultiModelController.java文件**

```
package com.example.springaimutimodelclient.controller;

import com.example.springaimutimodelclient.pojo.IdCardInfo;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/ai")
public class MultiModelController {
    private final ChatClient deepseekClient;
    private final ChatClient zhipuaiClient;

    public MultiModelController(
            @Qualifier("deepseekClient") ChatClient deepseekClient,
            @Qualifier("zhipuaiClient") ChatClient zhipuaiClient) {
        this.deepseekClient = deepseekClient;
        this.zhipuaiClient = zhipuaiClient;
    }

    @GetMapping("/deepseek")
    public String chatWithDeepseek(@RequestParam("message") String message) {
        return deepseekClient.prompt()
                .user(message)
                .call()
                .content();
    }

    @GetMapping("/zhipuai")
    public String chatWithZhipuai(@RequestParam("message") String message) {
        return zhipuaiClient.prompt()
                .user(message)
                .call()
                .content();
    }

}
```

**4) 启动项目并测试**

启动项目后，浏览器输入如下内容进行测试：

```
# http://localhost:8080/ai/imgAnaly
这张图片展示了一个金属制的圆形水果篮，篮子中放置了三根香蕉和两个苹果。 篮子的设计简约而现代，背景是灰色的墙壁，整体给人一种简洁、干净的感觉。

# http://localhost:8080/ai/idCardAnaly
{
  "name": "金阳",
  "sex": "女",
  "nation": "汉",
  "birth": "1978-10-27",
  "address": "北京市市辖区复兴门外大街999号院11号楼3单元502室",
  "idNo": "110102197810272321"
}
```

# 5. **Chat Memory 聊天记忆**

大语言模型（LLMs）是无状态的，即它们不会自动保留先前对话信息。这在需要跨多次交互保持上下文或状态时，是一个显著限制。Spring AI 引入 Chat Memory 功能，允许你在多轮对话中存储并检索信息，以解决这一问题。

Spring AI中的Chat Memory可以将最近N次对话上下文信息保存在内存或者外部数据库（例如PostgreSQL、MySQL、SQL Server、HSQLDB、Cassandra、Neo4j）中，以便LLM在同一会话的多轮互动中保持语义连贯，默认N为20（包括USER、ASSISTANT消息），也可以用户来指定，超出后按时间顺序清理（system 消息一直存在，不计入20条内）。

## 5.1. **ChatMemory抽象与使用方式**

ChatMemory使用中涉及如下两个接口：

Ø ChatMemory接口：提供在指定对话中添加消息、检索记忆内容、以及清理旧消息的能力。

Ø ChatMemoryRepository接口：负责消息的底层储存与提取。

ChatMemory默认实现为MessageWindowChatMemory类，如下方式创建ChatMemory:

```
MessageWindowChatMemory memory = MessageWindowChatMemory.builder()
    .maxMessages(20)
    .build();
```

以上这种方式创建ChatMemory对象默认将保存最近N条对话信息（默认20条），超出后按时间顺序清理（系统消息不会被清除）。最近N条对话消息存储到哪里由ChatMemoryRepository的实现决定（存储后端），ChatMemoryRepository有如下四种实现：

* InMemoryChatMemoryRepository：使用 ConcurrentHashMap 存储消息，这种也是默认方式。
* JdbcChatMemoryRepository：基于 JDBC 的关系型数据库（PostgreSQL、MySQL、SQL Server、HSQLDB）存储数据，支持通过 Spring 属性配置 schema 初始化时机与脚本位置。
* CassandraChatMemoryRepository：使用 Cassandra，可配置 TTL（例如 3 年）实现自动过期。
* Neo4jChatMemoryRepository：基于图数据库 Neo4j存储消息。

**在SpringBoot项目中，使用ChatMemory的步骤如下：**

**1. 在项目中配置ChatMemory Bean**

使用示例如下：

```
#使用默认内存方式
@Bean
ChatMemory chatMemory() {
    // 保留 20条最近消息
    return MessageWindowChatMemory.builder()
            .maxMessages(20)
            .build();
}

#使用JDBC方式
@Bean
ChatMemory chatMemory(JdbcChatMemoryRepository repo) {
    return MessageWindowChatMemory.builder()
            .chatMemoryRepository(repo)
            .maxMessages(20)
            .build();
}
```

**2. 创建ChatClient时加入ChatMemory记忆内容**

使用示例如下：

```
ChatClient client = ChatClient.builder(chatModel)
    .defaultAdvisors(
      MessageChatMemoryAdvisor.builder(chatMemory).build()
    )
    .build();
```

Advisor（顾问）是 Spring AI 中用于增强方法执行逻辑的组件，类似于一个拦截器，通过 Advisor，可以在方法执行前后插入自定义逻辑，从而提升代码的可扩展性与灵活性，常用于任务增强、数据预处理等场景。这里通过Advisor实现将 ChatMemory 集成到ChatClient中。

**3. 使用ChatClient时传入对话ID**

使用示例如下：

```
chatClient.prompt()
        .user(message)
        .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
        .call()
        .content();
```

与模型交互时通过CONVERSATION\_ID标记会话，advisor会自动上下文管理。

## 5.2. **ChatMemory存储在内存案例**

以下案例是将ChatMemory存储在内存中，按照如下步骤实现即可。

**1) 创建SpringBoot项目，命名为“SpringAIChatMemoryInMemory”**

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/56297cd4f5cb4cb08a49b235b713fc5d.jpg)

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/ce5f4cd353374b879cf5aa0d7fe365e4.jpg)

**2) 配置项目pom.xml**

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.5.2</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.example</groupId>
    <artifactId>SpringAIChatMemoryInMemory</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>SpringAIChatMemoryInMemory</name>
    <description>SpringAIChatMemoryInMemory</description>


    <properties>
        <java.version>17</java.version>
    </properties>

    <!-- 导入 Spring AI BOM，用于统一管理 Spring AI 依赖的版本，
    引用每个 Spring AI 模块时不用再写 <version>，只要依赖什么模块 Mavens 自动使用 BOM 推荐的版本 -->
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.ai</groupId>
                <artifactId>spring-ai-bom</artifactId>
                <version>1.0.0-SNAPSHOT</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.ai</groupId>
            <artifactId>spring-ai-starter-model-deepseek</artifactId>
        </dependency>
    </dependencies>


    <!-- 声明仓库， 用于获取 Spring AI 以及相关预发布版本-->
    <repositories>
        <repository>
            <id>spring-snapshots</id>
            <name>Spring Snapshots</name>
            <url>https://repo.spring.io/snapshot</url>
            <releases>
                <enabled>false</enabled>
            </releases>
        </repository>
        <repository>
            <name>Central Portal Snapshots</name>
            <id>central-portal-snapshots</id>
            <url>https://central.sonatype.com/repository/maven-snapshots/</url>
            <releases>
                <enabled>false</enabled>
            </releases>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
    </repositories>

</project>

```

以上pom.xml中引入了“spring-ai-starter-model-deepseek”依赖。

**3) 配置resources/application.properties**

```
spring.application.name=SpringAIChatMemoryInMemory

server.port=8080

#配置 Deepseek的基础URL、密钥和使用模型
spring.ai.deepseek.base-url=https://api.deepseek.com
spring.ai.deepseek.api-key=sk-...21
spring.ai.deepseek.chat.options.model=deepseek-chat
```

**4) 创建config包并创建Config.java类**

```
package com.example.springaichatmemoryinmemory.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    ChatMemory chatMemory() {
        // 返回 ChatMemory 类型的 Bean，用于管理对话中的上下文记忆
        // 这里使用 MessageWindowChatMemory 实现，最多保留 20 条消息
        // 默认会保留系统消息（system message）不被清除
        return MessageWindowChatMemory.builder()
                .maxMessages(20)
                .build();
    }

    @Bean
    ChatClient chatClient(ChatModel chatModel, ChatMemory memory) {
        // 根据 ChatModel 和 ChatMemory 来构建 ChatClient 实例
        // ChatClient 是与 LLM 交互的客户端，支持添加多个 Advisor（顾问）来增强功能
        return ChatClient.builder(chatModel)
                // 安装一个 MessageChatMemoryAdvisor，将 ChatMemory 集成到ChatClient中
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(memory).build())
                .build();
    }
}

```

通过@Configuration 告诉 Spring：这是配置类，类里的 @Bean 方法会被执行，返回的对象将注册为 Spring 管理的 Bean。

**5) 创建controller包，并创建ChatMemoryController.java文件**

```
package com.example.springaichatmemoryinmemory.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
public class ChatMemoryController {

    private final ChatClient chatClient;

    public ChatMemoryController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/chatMemory")
    public String chatMemory(@RequestParam(value = "cid") String conversationId,
                             @RequestParam(value = "message", defaultValue = "你是谁") String message) {

        System.out.println("conversationId："+conversationId);

        return chatClient.prompt()
                .user(message)
                // 设置 advisor 参数：告诉 memory 使用谁的上下文
                // 这里需要使用稳定的 conversationId，保持上下文一致
                // conversationId 不要使用每次新生成的 UUID，否则将无法保留上下文
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();
    }
}

```

**6) 启动项目并测试**

启动项目后，浏览器输入如下内容进行测试：

```
# http://localhost:8080/ai/chatMemory?cid=123&message=1加1等于几
1加1的答案取决于具体的数学体系或上下文： 1. **十进制算术**：在常规的十进制系统中，1 + 1 = 2。 2. **二进制系统**：1 + 1 = 10（读作“一零”），因为二进制逢二进一。 3. **布尔代数**：1 + 1 = 1（逻辑“或”运算中，真值相加仍为真）。 4. **其他情境**： - 在化学中，1升水加1升酒精，体积可能小于2升（分子间空隙变化）。 - 在抽象代数中，某些自定义运算可能有不同结果。 **常见误解**： - 某些幽默或脑筋急转弯会回答“1 + 1 = 11”（数字拼接），但这不属于数学范畴。 如需特定领域的解释，可以进一步说明！

# http://localhost:8080/ai/chatMemory?cid=123&message=这个数再加上1等于几？
根据不同的数学体系或上下文，**“这个数据再加上1”**的结果会有所不同。以下是几种常见情况的分析： --- ### 1. **若原数据是常规算术结果（1+1=2）** - **十进制**：2 + 1 = **3** - **二进制**：10（二进制的2） + 1 = **11**（二进制的3） - **十六进制**：2 + 1 = **3**（与十进制相同） ### 2. **若原数据是二进制结果（1+1=10）** - 10（二进制的2） + 1 = **11**（二进制的3） ### 3. **若原数据是布尔代数结果（1+1=1）** - 逻辑“或”运算中：1（真） + 1（真）仍为 **1**（真），因为真值叠加不改变结果。 ### 4. **其他情境** - **化学混合**：若原数据是1升水加1升酒精的体积（约1.93升），再加1升其他液体，结果取决于具体物质的性质。 - **字符串拼接**：若“1+1=11”是字符串拼接，则“11” + “1” = **“111”**。 --- ### 关键点总结 - **数学默认**：通常理解为十进制，结果是 **3**。 - **编程/逻辑**：需明确变量类型（如二进制、布尔值等）。 - **趣味回答**：若延续脑筋急转弯逻辑，可能是“111”或“王”（汉字叠加）。 需要更具体的上下文吗？可以告诉我您所指的“数据”类型！

#http://localhost:8080/ai/chatMemory?cid=1234&message=这个数再加上1等于几？
### 初始理解问题 首先，我们需要明确问题的陈述：“这个数再加上1等于几？”这句话看起来非常简单，但它有几个关键部分需要拆解： 1. “这个数”：这是一个指代词，指的是某个特定的数。但在当前的上下文中，没有明确说明“这个数”具体是哪个数。因此，我们需要考虑“这个数”可能指代的是什么。 ....
```

可以看到相同的会话ID中可以使用ChatMemory，不同的会话ID中ChatMemory不能共享。

## 5.3. **ChatMemory存储在MySQL案例**

以下案例是将ChatMemory存储在MySQL中，按照如下步骤实现即可。

**1) 创建SpringBoot项目，命名为“SpringAIChatMemoryInMySQL”**

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/d50072eed72e41cfb941242813763ca2.jpg)

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/a3e292c06b9540a184c01f699a9072c8.jpg)

**2) 配置项目pom.xml**

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.5.3</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.example</groupId>
    <artifactId>SpringAIChatMemoryInMySQL</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>SpringAIChatMemoryInMySQL</name>
    <description>SpringAIChatMemoryInMySQL</description>

    <properties>
        <java.version>17</java.version>
    </properties>

    <!-- 导入 Spring AI BOM，用于统一管理 Spring AI 依赖的版本，
    引用每个 Spring AI 模块时不用再写 <version>，只要依赖什么模块 Mavens 自动使用 BOM 推荐的版本 -->
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.ai</groupId>
                <artifactId>spring-ai-bom</artifactId>
                <version>1.0.0-SNAPSHOT</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.ai</groupId>
            <artifactId>spring-ai-starter-model-deepseek</artifactId>
        </dependency>

        <!--将 ChatMemory 存储到关系数据库 依赖包-->
        <dependency>
            <groupId>org.springframework.ai</groupId>
            <artifactId>spring-ai-starter-model-chat-memory-repository-jdbc</artifactId>
        </dependency>

        <!-- 引入 MySQL 驱动包-->
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <version>8.0.33</version>
        </dependency>

    </dependencies>


    <!-- 声明仓库， 用于获取 Spring AI 以及相关预发布版本-->
    <repositories>
        <repository>
            <id>spring-snapshots</id>
            <name>Spring Snapshots</name>
            <url>https://repo.spring.io/snapshot</url>
            <releases>
                <enabled>false</enabled>
            </releases>
        </repository>
        <repository>
            <name>Central Portal Snapshots</name>
            <id>central-portal-snapshots</id>
            <url>https://central.sonatype.com/repository/maven-snapshots/</url>
            <releases>
                <enabled>false</enabled>
            </releases>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
    </repositories>

</project>
```

以上pom.xml中引入了“spring-ai-starter-model-deepseek”依赖、“spring-ai-starter-model-chat-memory-repository-jdbc”、“mysql-connector-j”依赖包。

**3) 配置resources/application.properties**

```
spring.application.name=SpringAIChatMemoryInMySQL

server.port=8080

#MySQL 配置 ，提前在MySQL中创建数据库和表
spring.datasource.url=jdbc:mysql://localhost:3306/chat_memory?useUnicode=true&characterEncoding=UTF-8&connectionCollation=utf8mb4_unicode_ci
spring.datasource.username=root
spring.datasource.password=123456
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

#自动创建ChatMemory存储的表结构
spring.ai.chat.memory.repository.jdbc.initialize-schema=always
#告诉框架当前数据库是 MariaDB/MySQL，会加载 schema-mariadb.sql 初始化脚本，自行创建表 SPRING_AI_CHAT_MEMORY表
spring.ai.chat.memory.repository.jdbc.platform=mariadb
#确保每条 JDBC 连接都使用 utf8mb4 字符集并指定 utf8mb4_unicode_ci 排序
spring.datasource.hikari.connection-init-sql=SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci

#配置 Deepseek的基础URL、密钥和使用模型
spring.ai.deepseek.base-url=https://api.deepseek.com
spring.ai.deepseek.api-key=sk-...821
spring.ai.deepseek.chat.options.model=deepseek-chat
```

关于以上配置项的解释如下：

* spring.datasource.url：连接本地 MySQL 数据库 chat\_memory
* useUnicode=true&characterEncoding=UTF-8：确保 JDBC 使用 Unicode 和 UTF8 编码。
* connectionCollation=utf8mb4\_unicode\_ci：设置数据库连接使用的字符排序规则为 utf8mb4\_unicode\_ci，用于完全支持表情符（emoji）和多语言字符。
* initialize-schema=always：Spring AI 的 JdbcChatMemoryRepository 会尝试自动创建所需表结构（如 ai\_chat\_memory），设置为 always 表示 无论数据库类型 都强制执行结构初始化
* platform=mariadb：告诉框架当前数据库是 MariaDB/MySQL，用于替换 SQL 脚本或处理方言匹配中的 @@platform@@ 占位符。
* spring.datasource.hikari.connection-init-sql：确保每条 JDBC 连接都使用 utf8mb4 字符集并指定 utf8mb4\_unicode\_ci 排序。

**4) 在mysql中创建对应数据库**

这里使用的是Mysql8数据库，在数据库中创建application.properties配置的数据库chat\_memory

```
CREATE DATABASE chat_memory
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
```

注意："COLLATE utf8mb4\_unicode\_ci"是设置默认排序规则为 utf8mb4\_unicode\_ci，支持区分或等同处理特殊字符，保证多语言一致性，避免因排序方式不同导致的查询或显示异常。

**5) 创建config包并创建Config.java类**

```
package com.example.springaichatmemoryinmysql.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    /**
     * 配置 ChatMemory Bean，并使用 JDBC 存储实现：
     *   - 注入系统中已有的 JdbcChatMemoryRepository；
     *   - 使用 MessageWindowChatMemory 策略，最多保留 20 条消息；
     *   - 每次超出后，会删除最旧的消息（系统消息除外）。
     */
    @Bean
    ChatMemory chatMemory(JdbcChatMemoryRepository repo) {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(repo)// 使用 JDBC 后端来存取记忆
                .maxMessages(20)// 最多保留 20 条消息
                .build();
    }

    @Bean
    ChatClient chatClient(ChatModel chatModel, ChatMemory memory) {
        // 根据 ChatModel 和 ChatMemory 来构建 ChatClient 实例
        // ChatClient 是与 LLM 交互的客户端，支持添加多个 Advisor（顾问）来增强功能
        return ChatClient.builder(chatModel)
                // 安装一个 MessageChatMemoryAdvisor，将 ChatMemory 集成到ChatClient中
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(memory).build())
                .build();
    }

}
```

通过@Configuration 告诉 Spring：这是配置类，类里的 @Bean 方法会被执行，返回的对象将注册为 Spring 管理的 Bean。

**6) 创建controller包，并创建CmInMySQLController.java文件**

```
package com.example.springaichatmemoryinmysql.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
public class CmInMySQLController {

    private final ChatClient chatClient;

    public CmInMySQLController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/chatMemory")
    public String chatMemory(@RequestParam(value = "cid") String conversationId,
                       @RequestParam(value = "message", defaultValue = "你是谁") String message) {

        System.out.println("conversationId："+conversationId);

        return chatClient.prompt()
                .user(message)
                // 设置 advisor 参数：告诉 memory 使用谁的上下文
                // 这里需要使用稳定的 conversationId，保持上下文一致
                // conversationId 不要使用每次新生成的 UUID，否则将无法保留上下文
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();
    }
}
```

**7) 启动项目并测试**

启动项目后，浏览器输入如下内容进行测试：

```
# http://localhost:8080/ai/chatMemory?cid=123&message=1加1等于几
1加1等于2。这是基本的数学加法运算结果。 如果你有其他情境下的特殊含义（比如二进制、布尔代数等），可以进一步说明，我会为你详细解答！ 😊

# http://localhost:8080/ai/chatMemory?cid=123&message=这个数再加上1等于几？
在之前的加法结果 **1 + 1 = 2** 的基础上： **2 + 1 = 3** 如果涉及其他情境（比如二进制、计数器溢出等），结果可能不同。例如： - **二进制**：10（二进制的2） + 1 = 11（二进制的3） - **数字系统上限**：比如8位无符号整数中，255 + 1 = 0（溢出） 需要具体场景的话，可以告诉我！ 😄

#http://localhost:8080/ai/chatMemory?cid=1234&message=这个数再加上1等于几？
### 问题陈述 **问题：** 这个数再加上1等于几？ ### 初步理解 首先，我们需要明确问题的含义。问题问的是“这个数”加上1等于多少。关键在于“这个数”指的是哪个数。由于问题中没有明确给出“这个数”的具体值，我们需要考虑几种可能性...
```

可以看到相同的会话ID中可以使用ChatMemory，不同的会话ID中ChatMemory不能共享。

另外，我们可以查看MySql对应的spring\_ai\_chat\_memory表，该表将每次会话信息都存储下来，存储内容如下：

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/0ab2122800fc4b7ca4645cdbcb999708.jpg)

# 6. **Tool Calling 工具调用**

Tool Calling（工具调用），也称为函数调用，是让大语言模型（LLM）在对话中“调用”外部定义的工具或 API 的机制，通过这个机制，模型可以在生成回答前提出需要执行的操作（如获取实时天气、设置数据库记录、触发业务流程等），然后由应用端执行工具，结果再反馈回模型，最终给用户完整回复。

Tool Calling主要应用场景如下：

* 信息检索：从外部源（如数据库、Web 服务、文件系统或 Web 搜索引擎）检索信息，增强模型的知识，使其能够回答其他方式无法回答的问题。
* 执行操作：在软件系统中执行特定操作，如发送电子邮件、在数据库中创建新记录、提交表单或触发工作流。目标是自动执行原本需要人工干预或显式编程的任务。

Spring AI 提供了方便的 API 来定义工具、解析来自模型的工具调用请求以及执行工具调用。

## 6.1. **工具定义与使用**

Spring AI中工具调用流程示意图如下：

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/1bd8767c2cc94ef2890e379d39f003fc.jpg)

1) 开发者定义工具，并将其注册到 Spring 容器中。

2) 模型在生成响应时，识别到需要调用工具，会生成包含工具调用信息的响应。

3) 应用程序接收到模型的响应后，解析其中的工具调用信息，执行相应的工具。

4) 工具执行完成后，将结果返回给应用程序。

5) 应用程序将工具执行结果作为上下文信息，传递给模型。

6) 模型使用工具执行结果，生成最终的响应。

* **工具定义：**

开发者可以通过在方法上添加@Tool注解定义工具，该注解允许提供工具的名称、描述和输入参数等信息，模型在调用工具时，会根据这些信息生成相应的调用请求。

定义工具示例如下：

```
@Component
public class Tools {

    @Tool(description = "获取当前日期和时间")
    public String getCurrentDateTime() {
        return 当前日期...;
    }

    @Tool(description = "设置闹钟")
    public void setAlarm(@ToolParam(description = "闹钟时间") String alarmTime) {
        // 设置闹钟的逻辑
    }
}
```

以上注意点：

a. @Component 加到类中，表示是一个组件，关键是将工具类让Spring容器管理起来，也可以使用@Configuration、@Service标记类，只要Spring 能扫描到并将其实例管理起来就可以。

b. 工具方法使用@Tool标注；工具名称默认就是方法本身，也可以通过@Tool(name="tool name")方式来指定。

c. @Tool(description="...")中，这里的description是工具描述，大模型根据该描述决定要不要调用该工具。

d. 工具方法中需要传入参数的，通过使用@ToolParam标注，大模型自动决定调用时机和自动根据语义传入参数，调用完成后生成最终对话。

e. 工具不支持如下类型作为参数或返回值:Optional、异步类型（如 CompletableFuture、Future）、响应式类型（如 Flow、Mono、Flux）、函数式类型（如 Function、Supplier、Consumer）。

* **工具使用：**

```
//注册多个工具
ChatClient chatClient = ChatClient.builder(chatModel)
		.defaultSystem("你是一个非常有帮助的助手，你可以使用工具来帮助回答问题")
		.defaultTools(new ATools(),new BTools,new CTools ...)
		.build();

//与模型进行对话
String result = chatClient.prompt()
		.user("给我设置10分钟后的闹钟")
		.call()
		.content();
```

## 6.2. **Tool Calling 使用案例**

如下创建SpringBoot项目，在该项目中定义多个工具演示Spring AI中工具使用，按照如下步骤实现即可。

**1) 创建SpringBoot项目，命名为“SpringAIToolCalling”**

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/f1effdd487f649b99aba882b931a80d3.jpg)

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/5e688be77e214fc9947f1a798c78b481.jpg)

**2) 配置项目pom.xml**

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.5.3</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.example</groupId>
    <artifactId>SpringAIToolCalling</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>SpringAIToolCalling</name>
    <description>SpringAIToolCalling</description>

    <properties>
        <java.version>17</java.version>
    </properties>

    <!-- 导入 Spring AI BOM，用于统一管理 Spring AI 依赖的版本，
    引用每个 Spring AI 模块时不用再写 <version>，只要依赖什么模块 Mavens 自动使用 BOM 推荐的版本 -->
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.ai</groupId>
                <artifactId>spring-ai-bom</artifactId>
                <version>1.0.0-SNAPSHOT</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.ai</groupId>
            <artifactId>spring-ai-starter-model-deepseek</artifactId>
        </dependency>
    </dependencies>


    <!-- 声明仓库， 用于获取 Spring AI 以及相关预发布版本-->
    <repositories>
        <repository>
            <id>spring-snapshots</id>
            <name>Spring Snapshots</name>
            <url>https://repo.spring.io/snapshot</url>
            <releases>
                <enabled>false</enabled>
            </releases>
        </repository>
        <repository>
            <name>Central Portal Snapshots</name>
            <id>central-portal-snapshots</id>
            <url>https://central.sonatype.com/repository/maven-snapshots/</url>
            <releases>
                <enabled>false</enabled>
            </releases>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
    </repositories>

</project>
```

以上pom.xml中引入了“spring-ai-starter-model-deepseek”依赖包。

**3) 配置resources/application.properties**

```
spring.application.name=SpringAIToolCalling

server.port=8080

#配置 Deepseek的基础URL、密钥和使用模型
spring.ai.deepseek.base-url=https://api.deepseek.com
spring.ai.deepseek.api-key=sk-...21
spring.ai.deepseek.chat.options.model=deepseek-chat
```

**4) 创建tools包并创建MyTools.java类**

```
package com.example.springaitoolcalling.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MyTools {

    Logger log = LoggerFactory.getLogger(MyTools.class);

    @Tool(description = "返回当前系统时间")
    public String getCurrentTime() {
        log.info("调用 getCurrentTime 工具，当前时间："+java.time.LocalDateTime.now().toString());
        return java.time.LocalDateTime.now().toString();
    }

    @Tool(description = "对两个数字执行加、减、乘、除运算")
    public double calculate(
            @ToolParam(description = "第一个数字") double a,
            @ToolParam(description = "第二个数字") double b,
            @ToolParam(description = "运算类型") String operation) {
        log.info("调用 calculate 工具，第一个数字："+a+",第二个数字："+b+",运算类型："+operation);

        double result = 0;

        switch (operation) {
            case "add":
                result = a + b;
                break;
            case "subtract":
                result = a - b;
                break;
            case "multiply":
                result = a * b;
                break;
            case "divide":
                if (b != 0) {
                    result = a / b;
                }
        }

        return result;
    }

    private RestTemplate restTemplate = new RestTemplate();
    private
    String baseUrl = "http://shanhe.kim/api/youjia/youjia.php";

    @Tool(description = "查询指定省份的油价，直接返回完整 JSON 字符串")
    public String getOilPriceJson(@ToolParam(description = "省份名称") String province) {
        log.info("调用 getOilPriceJson 工具，查询省份："+province);
        String url = String.format("%s?province=%s", baseUrl, province);
        return restTemplate.getForObject(url, String.class);
    }
}
```

在MyTools.java类中我们定义了三个工具，每个方法中进行了log日志输出：

* getCurrentTime：返回当前系统时间。
* calculate:对两个数字执行加、减、乘、除运算。
* getOilPriceJson:查询指定省份的油价，直接返回完整 JSON 字符串。

**5) 创建controller包，并创建ToolUseController.java文件**

```
package com.example.springaitoolcalling.controller;

import com.example.springaitoolcalling.tools.MyTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
public class ToolUseController {
    private final ChatClient chatClient;

    public ToolUseController(ChatModel chatModel, MyTools tools) {
        this.chatClient = ChatClient.builder(chatModel)
                .defaultSystem("你是一个非常有帮助的助手，你可以使用工具来帮助回答问题")
                .defaultTools(tools)
                .build();
    }

    @GetMapping("/chat")
    public String chat(@RequestParam("message") String message) {

        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }
}

```

**6) 启动项目并测试**

启动项目后，浏览器输入如下内容进行测试：

```
# http://localhost:8080/ai/chat?message=今天是什么日期？
今天是xxx年xx月xx日

# http://localhost:8080/ai/chat?message=123乘以200是多少？
123乘以200等于24600。

# http://localhost:8080/ai/chat?message=北京油价是多少？
北京的油价如下： - 92号汽油：7.17 元/升 - 95号汽油：7.64 元/升 - 98号汽油：9.14 元/升 - 0号柴油：6.86 元/升 数据更新时间：xxx xxx

# http://localhost:8080/ai/chat?message=今天天气如何？
我无法直接获取天气信息，但您可以告诉我您所在的城市或地区，我可以尝试为您提供相关的天气查询建议或链接。
```

特别提示：

* 以上代码运行后，与工具调用相关的提问可以看到后台打印的日志会调用到工具对应的方法。
* 我们也可以在“ToolUseController.java”中设置不使用工具，将“defaultTools（new MyTools）”注释掉再进行测试，会发现以上访问不会调用工具，都由大模型根据已有知识进行回复，可能存在不精准情况。

# 7. **MCP模型上下文协议**

## 7.1. **MCP介绍与原理**

MCP（Model Context Protocol，模型上下文协议）是 Anthropic 于 2024 年 11 月推出的开放标准，旨在为大型语言模型（LLMs）提供统一接口，以便连接和调用外部数据源和工具。

目前，各大 LLM 平台（如 Deepseek、ChatGPT、Claude）普遍支持 Function Calling，允许模型在需要时调用特定函数（如访问网络、查询数据库等）来扩展能力。

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/9a25458c571c431bafa22b896450b361.jpg)

然而，不同平台的 Function Call API 存在实现差异，导致开发者在切换平台时需要重新适配，增加了开发成本。

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/b7256bb7d28e4bafbdfe53019f8a4a22.jpg)

**MCP的核心是对大模型调用外部工具建立一个标准化流程**。MCP基于 Function Calling，进一步定义了从请求构建、发送、执行到结果返回的标准化流程。通过 MCP，模型可以以统一方式与各种外部工具和数据源交互，极大提升了跨平台兼容性和 AI 应用开发效率。

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/43a7611169004e1cb1ad8821e57dc23f.jpg)

**MCP 与 Function Calling 的区别和联系如下：**

* Function Calling ：是 LLM 内部定义的一组函数，通过 JSON schema 让 LLM知道有哪些功能能调用。
* MCP:在 Function Calling 基础上，进一步标准化了函数调用的完整流程，包括请求的构建、发送、执行以及结果的返回。

简单来说，MCP 是对 Function Calling 的扩展与升级，实现了更高层次的抽象和更强的可扩展性。可以将 MCP 理解为 AI 世界里的“USB-C标准”，为模型接入各种数据源和工具提供了统一接口，确保连接便捷且安全。

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/c3a233ea28834d2eaa432534d7522f36.jpg)

MCP 遵循客户端-服务器架构，角色主要包含三部分：

* **MCP Host**

运行 LLM（如 Claude、ChatGPT、Deepseek）的实体节点，如果使用的LLM为线上模型，可以忽略这部分。

* **MCP Client**

**运行着与大模型对话的客户端（可能会使用工具）叫做MCP Client**。其与 MCP Server 保持 1:1 连接，负责解析模型请求，如果使用工具会将请求转发到对应 MCP Server。

* **MCP Server**

**实际运行外部工具（如访问文件系统、发送邮件、查询日历）的服务端叫做MCP Server**。负责处理请求并将结果返回给 Client。

MCP Cilent与MCP Server之间有两种通信机制：Stdio（标准输入/输出）和SSE(Server-Sent-Event，服务器发送事件)，两种机制介绍如下：

* Stdio（标准输入/输出）：当服务器和客户端同时运行在本机时，可以使用Stdio机制。
* SSE(Server-Sent-Event)：当服务器部署在远程服务器上，客户端通过HTTP 请求发送消息使用这种方式。

## 7.2. **MCP Java SDK 架构**

下图是MCP Java SDK 架构示意图：

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/f08bba841f504681a49e0c9205a0fb76.jpg)

上图中，McpClient处理客户端操作，McpServer管理服务端操作，两者都使用McpSession进行通信管理。传输层（Mcp Transport）负责处理JSON-RPC 消息的序列化和反序列化，支持三种传输实现：STDIO、Spring MVC SSE、Spring WebFlux SSE，三者区别如下:

* STDIO:基于进程间的标准输入/输出（STDIO）传输，支持单进程，同步交互处理消息。适用于MCP 服务端和客户端都在同一节点上集成。
* Spring MVC SSE（HTTP SSE）:基于Spring MVC的SSE传输，支持Servlet线程池，阻塞式处理消息。适用于普通的Web应用。
* Spring WebFlux SSE:官方建议方式。基于Spring WebFlux的反应式SSE，支持高并发、低延迟，响应式处理消息。适用于高并发的web微服务。

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/8885efc19d02491a853aa7522a41ac12.jpg)

对于以上不同的传输方式，Spring AI 提供了多个启动器（starter），简化MCP在SpringBoot中的使用。

* **客户端Starter:**

spring-ai-starter-mcp-client：支持 STDIO 与 HTTP-SSE。

spring-ai-starter-mcp-client-webflux：基于 WebFlux 的 SSE 客户端实现。

* **服务端Starter:**

spring-ai-starter-mcp-server：支持 STDIO 传输。

spring-ai-starter-mcp-server-webmvc：基于 Spring MVC 的 SSE 服务端实现。

spring-ai-starter-mcp-server-webflux：基于 WebFlux 的 SSE 服务端实现。

## 7.3. **MCP 案例-Stdio传输模式**

在该案例中，我们会创建MCP Server 和MCP Client 两个SpringBoot项目，MCP Server项目中会创建一个getWeather工具，该工具通过OpenWeather可以查询某个城市天气情况；MCP Client 项目中创建相应的Controller，根据配置通过STDIO 方式与MCP Server进行通信，实现调用天气工具。

### **7.3.1. Mcp Server开发**

按照如下步骤创建MCP Server对应的SpringBoot项目。

**1) 创建SpringBoot项目**

SpringBoot项目命名为SpringAIMCPStdioServer，设置使用的JDK为17版本。

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/01fec433ad7d4bc2b7da061b05a64040.jpg)

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/b090ea8a95b64e94a03acc03d4a11fdb.jpg)

**2) 在项目中加入如下Maven依赖**

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.5.3</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.example</groupId>
    <artifactId>SpringAIMCPStdioServer</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>SpringAIMCPStdioServer</name>
    <description>SpringAIMCPStdioServer</description>

    <properties>
        <java.version>17</java.version>
    </properties>

    <!-- 导入 Spring AI BOM，用于统一管理 Spring AI 依赖的版本，
    引用每个 Spring AI 模块时不用再写 <version>，只要依赖什么模块 Mavens 自动使用 BOM 推荐的版本 -->
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.ai</groupId>
                <artifactId>spring-ai-bom</artifactId>
                <version>1.0.0-SNAPSHOT</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- 依赖的MCP 包 ,只支持 STDIO 传输-->
        <dependency>
            <groupId>org.springframework.ai</groupId>
            <artifactId>spring-ai-starter-mcp-server</artifactId>
        </dependency>

        <!-- 依赖的json 包-->
        <dependency>
            <groupId>org.json</groupId>
            <artifactId>json</artifactId>
            <version>20210307</version>
        </dependency>
    </dependencies>

    <!-- 打包插件 -->
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <version>3.4.4</version>
                <configuration>
                    <mainClass>com.example.springaimcpstdioserver.SpringAimcpStdioServerApplication</mainClass>
                </configuration>
                <executions>
                    <execution>
                        <goals>
                            <goal>repackage</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>

    <!-- 声明仓库， 用于获取 Spring AI 以及相关预发布版本-->
    <repositories>
        <repository>
            <id>spring-snapshots</id>
            <name>Spring Snapshots</name>
            <url>https://repo.spring.io/snapshot</url>
            <releases>
                <enabled>false</enabled>
            </releases>
        </repository>
        <repository>
            <name>Central Portal Snapshots</name>
            <id>central-portal-snapshots</id>
            <url>https://central.sonatype.com/repository/maven-snapshots/</url>
            <releases>
                <enabled>false</enabled>
            </releases>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
    </repositories>

</project>
```

注意：在该pom.xml中引入了“spring-ai-starter-mcp-server”MCP 依赖包，该包只支持STDIO 传输。

**3) 配置resources/application.properties**

```
spring.application.name=SpringAIMCPStdioServer

#指定 MCP 服务器的名称为 spring-ai-mcp-weather
spring.ai.mcp.server.name=spring-ai-mcp-weather

#配置应用监听的端口为 8080
server.port=8086

#禁用 Spring Boot 启动时的横幅（Banner）显示，对于使用 STDIO 传输的 MCP 服务器，禁用横幅有助于避免输出干扰。
spring.main.banner-mode=off

#如下参数启用并设置为空，将禁用控制台日志输出格式，减少输出干扰
logging.pattern.console=

#配置日志文件的输出路径，将日志写入指定的文件中
logging.file.name=D:/idea_space/SpringAICode/SpringAIMCPStdioServer/model-context-protocol/mcp-weather-stdio-server.log


#访问 OpenWeather API 的密钥
OPEN_WEATHER_API_KEY=f0...8

```

特别注意：以上配置中logging.file.name指定了MCP Server运行过程中日志输出的位置，可以通过该日志查看Server端运行情况（如：工具是否被调用）。

**4) 创建 WeatherService.java构建查询天气工具**

在项目中创建service包，在该包中创建WeatherService.java类，构建查询天气工具：

```
package com.example.springaimcpstdioserver.service;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 天气服务类，用于获取指定城市的天气信息
 * @Service 标记为 Spring 服务层组件
 */

@Service
public class WeatherService {
    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather";

    @Value("${OPEN_WEATHER_API_KEY}")
    private String OPEN_WEATHER_API_KEY;

    /**
     * 根据城市名称获取天气信息（使用 OpenWeatherMap）
     * @param city 城市名称，如 "Beijing"
     * @return 天气信息文本
     */
    @Tool(description = "获取指定城市的当前天气情况，格式化后的天气报告字符串。")
    public String getWeather(@ToolParam(description = "城市名称，必须是英文格式，比如 London 或 Beijing") String city) {

        logger.info("====== 调用了getWeather工具 ======");

        try {
            String charset = "UTF-8";

            String query = String.format(
                    "q=%s&appid=%s&units=metric&lang=zh_cn",
                    URLEncoder.encode(city, charset),
                    URLEncoder.encode(OPEN_WEATHER_API_KEY, charset)
            );

            URL url = new URL(BASE_URL + "?" + query);
            logger.info("====== 访问URL： ======"+url.toString());

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), charset));

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject data = new JSONObject(response.toString());

            if (data.getInt("cod") == 404) {
                return "未找到该城市的天气信息。";
            }

            JSONObject main = data.getJSONObject("main");
            JSONArray weatherArray = data.getJSONArray("weather");
            JSONObject weather = weatherArray.getJSONObject(0);
            JSONObject wind = data.getJSONObject("wind");

            String weatherDescription = weather.optString("description", "无描述");
            double temperature = main.optDouble("temp", Double.NaN);
            double feelsLike = main.optDouble("feels_like", Double.NaN);
            double tempMin = main.optDouble("temp_min", Double.NaN);
            double tempMax = main.optDouble("temp_max", Double.NaN);
            int pressure = main.optInt("pressure", 0);
            int humidity = main.optInt("humidity", 0);
            double windSpeed = wind.optDouble("speed", Double.NaN);

            return String.format("""
                    城市: %s
                    天气描述: %s
                    当前温度: %.1f°C
                    体感温度: %.1f°C
                    最低温度: %.1f°C
                    最高温度: %.1f°C
                    气压: %d hPa
                    湿度: %d%%
                    风速: %.1f m/s
                    """,
                    data.optString("name", city),
                    weatherDescription,
                    temperature,
                    feelsLike,
                    tempMin,
                    tempMax,
                    pressure,
                    humidity,
                    windSpeed
            );

        } catch (Exception e) {
            return "获取天气信息时出错: " + e.getMessage();
        }
    }

    public static void main(String[] args) {
        //测试方法
        WeatherService client = new WeatherService();
        String beijing = client.getWeather("Beijing");
        System.out.println(beijing);
    }
}
```

注意如下两点：

* 如上代码中使用“@Tool”标记了方法getWeather为工具。
* 代码中不要System输出任何内容，会影响返回结果的处理。

**5) 主应用类中加入工具**

主应用类为SpringAimcpStdioServerApplication.java，内容如下：

```
package com.example.springaimcpstdioserver;

import com.example.springaimcpstdioserver.service.WeatherService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringAimcpStdioServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAimcpStdioServerApplication.class, args);
    }


    /**
     * @Bean 注解用于将方法的返回值作为 Spring Bean 注册到 Spring 容器中。
     *  Spring 容器在启动过程中会扫描并执行所有带有 @Bean 注解的方法，以将其返回的对象注册到应用上下文中。
     *
     * ToolCallbackProvider 接口:Spring AI 提供的接口，其实现类负责将带有 @Tool 注解的方法注册为可供 AI 模型调用的工具。
     */
    @Bean
    public ToolCallbackProvider weatherTools(WeatherService weatherService) {
        return MethodToolCallbackProvider.builder().toolObjects(weatherService).build();
    }
}
```

这里在主应用类中通过@Bean注解创建ToolCallbackProvider类型，该类型是Spring AI 提供的接口，其实现类负责将指定Service类中带有 @Tool 注解的方法注册为可供 AI 模型调用的工具。

**6) 将SpringAIMCPStdioServer项目进行打包**

MCP Client与MCP Server使用STDIO传输时，我们需要将MCP Server项目进行打包，然后在MCP Client中进行配置，无需单独启动MCP Server。这里直接通过Maven工具进行打包即可。

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/ca26c6cc630640d09b39b506d0f83988.jpg)

### **7.3.2. Mcp Client 开发**

按照如下步骤创建MCP Client的SpringBoot项目。该MCP Client 项目中可以使用不同的LLM模型，只需要在对应配置文件中引入不同的模型对应的apikey及相关依赖即可。

**1) 创建SpringBoot项目**

SpringBoot项目命名为SpringAIMCPStdioClient，设置使用的JDK为17版本。

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/923e89b0ac9f45c59224b4bf9caa9897.jpg)

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/3dcfc8fb91794c348f6ef658e3aadaf8.jpg)

**2) 在项目中加入如下Maven依赖**

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.5.3</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.example</groupId>
    <artifactId>SpringAIMCPStdioClient</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>SpringAIMCPStdioClient</name>
    <description>SpringAIMCPStdioClient</description>

    <properties>
        <java.version>17</java.version>
    </properties>

    <!-- 导入 Spring AI BOM，用于统一管理 Spring AI 依赖的版本，
    引用每个 Spring AI 模块时不用再写 <version>，只要依赖什么模块 Mavens 自动使用 BOM 推荐的版本 -->
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.ai</groupId>
                <artifactId>spring-ai-bom</artifactId>
                <version>1.0.0-SNAPSHOT</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <dependencies>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- 基于 STDIO 传输客户端依赖 -->
        <dependency>
            <groupId>org.springframework.ai</groupId>
            <artifactId>spring-ai-starter-mcp-client</artifactId>
        </dependency>

        <!-- DeepSeek 模型依赖 -->
        <dependency>
            <groupId>org.springframework.ai</groupId>
            <artifactId>spring-ai-starter-model-deepseek</artifactId>
        </dependency>
    </dependencies>


    <!-- 声明仓库， 用于获取 Spring AI 以及相关预发布版本-->
    <repositories>
        <repository>
            <id>spring-snapshots</id>
            <name>Spring Snapshots</name>
            <url>https://repo.spring.io/snapshot</url>
            <releases>
                <enabled>false</enabled>
            </releases>
        </repository>
        <repository>
            <name>Central Portal Snapshots</name>
            <id>central-portal-snapshots</id>
            <url>https://central.sonatype.com/repository/maven-snapshots/</url>
            <releases>
                <enabled>false</enabled>
            </releases>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
    </repositories>

</project>

```

注意：以上依赖中引入“spring-ai-starter-mcp-client”包，该包是基于STDIO 传输客户端依赖包；“spring-ai-starter-model-deepseek”包是引入deepseek LLM，使用哪个LLM可以引入相应的依赖，支持的模型以及引入的包查看：https://docs.spring.io/spring-ai/reference/api/chat/openai-chat.html。

**3) 配置resources/application.properties**

MCP Client通过Stadio方式连接到MCP Server需要在项目的resources/application.properties文件中配置如下内容，指定的文件中需要进行MCP Server配置。

```
spring.application.name=SpringAIMCPStdioClient

server.port=8087

#配置 Deepseek的基础URL、密钥和使用模型
spring.ai.deepseek.base-url=https://api.deepseek.com
spring.ai.deepseek.api-key=sk-8...821
spring.ai.deepseek.chat.options.model=deepseek-chat

#STDIO 模式，指定 MCP 客户端的服务器配置文件路径
spring.ai.mcp.client.stdio.servers-configuration=classpath:/mcp-servers-config.json
```

注意：需要通过在配置文件中指定“spring.ai.mcp.client.stdio.servers-configuration”参数来让MCP Client找到MCP Server相应配置，进而启动MCP Server使用工具。

**4) 配置mcp-server-config.json**

resources/mcp-servers-config.json文件内容如下：

```
{
  "mcpServers": {
    "spring-ai-mcp-weather": {
      "command": "D:\\Program Files\\Java\\jdk17\\jdk\\bin\\java.exe",
      "args": [
        "-Dspring.ai.mcp.server.transport=STDIO",
        "-jar",
        "D:\\idea_space\\SpringAICode\\SpringAIMCPStdioServer\\target\\SpringAIMCPStdioServer-0.0.1-SNAPSHOT.jar"
      ]
    }
  }
}
```

该配置文件中需要指定正确的MCP Server打好的jar包路径。

**5) 项目中创建config/Config.java类**

Config.java类为Spring Configuration组件，该组件中注册两个Bean：

* SyncMcpToolCallbackProvider:自动集成 MCP Server 暴露的工具到 ChatClient，使 LLM 可通过 prompt 流调用工具。
* ChatClient：LLM聊天客户端。构建ChatClient时通过“defaultToolCallbacks(toolCallbackProvider)”方法配置工具回调提供者，使用LLM可以知道可以有对应的工具调用。

```
package com.example.springaimcpstdioclient.config;

import io.modelcontextprotocol.client.McpSyncClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class Config {

    /**
     * SyncMcpToolCallbackProvider:自动集成 MCP Server 暴露的工具到 ChatClient，使 LLM 可通过 prompt 流调用工具
     *  mcpSyncClient:SpringAI 根据 spring-ai-starter-mcp-client/spring-ai-starter-mcp-client-webflux
     *  依赖注入的 mcpSyncClients 列表，用于调用外部工具或服务，每个 McpSyncClient 对象代表一个到 MCP Server（通过 STDIO 或 SSE 等方式）的连接实例，
     *  如果你只配置了一个 MCP Server，clients 列表中就只有一个 McpSyncClient；如果配置了多个 Server，则会有多个连接，它们对应不同来源的工具。
     */
    @Bean
    public SyncMcpToolCallbackProvider toolCallbackProvider(List<McpSyncClient> mcpSyncClients) {
        return new SyncMcpToolCallbackProvider(mcpSyncClients);
    }


    @Bean
    public ChatClient chatClient(ChatModel chatModel,
                                 SyncMcpToolCallbackProvider toolCallbackProvider) {

        return ChatClient.builder(chatModel)
                .defaultSystem("你是一个非常有帮助的助手，你可以使用工具来帮助回答问题")
                //配置工具回调提供者，使 AI 能调用外部工具
                .defaultToolCallbacks(toolCallbackProvider)
                .build();
    }

}
```

注意：toolCallbackProvider方法中的参数mcpSyncClients表示SpringAI 根据 spring-ai-starter-mcp-client/spring-ai-starter-mcp-client-webflux依赖注入的 mcpSyncClients 列表，用于调用外部工具或服务，每个 McpSyncClient 对象代表一个到 MCP Server（通过 STDIO 或 SSE 等方式）的连接实例，如果你只配置了一个 MCP Server，clients 列表中就只有一个 McpSyncClient；如果配置了多个 Server，则会有多个连接，它们对应不同来源的工具。

**6) 创建controller/ChatController.java类**

在Controller中直接创建对应的方法与LLM进行对话调用工具即可。

```
package com.example.springaimcpstdioclient.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
public class ChatController {
    private final ChatClient chatClient;

    public ChatController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/chat")
    public String chat(@RequestParam String message) {

        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }
}
```

**7) 启动MCP Client进行测试**

运行“SpringAimcpStdioClientApplication.java”后，在浏览器中输入如下内容进行测试LLM模型自动调用工具：

```
# http://localhost:8087/ai/chat?message=北京天气如何?
北京的天气情况如下： - **天气描述**: 小雨 - **当前温度**: 27.0°C - **体感温度**: 30.0°C - **最低温度**: 27.0°C - **最高温度**: 27.0°C - **气压**: 1004 hPa - **湿度**: 84% - **风速**: 1.6 m/s 请注意携带雨具！

# http://localhost:8087/ai/chat?message=你是谁?
我是一个智能助手，可以帮助你回答问题、提供信息、解决问题或完成任务。如果你有任何问题或需要帮助，随时告诉我！
```

可以在MCP Server端配置的日志路径中找到对应的日志，查看到日志中会有相应工具调用：

```
WeatherService:====== 调用了getWeather工具 ======
WeatherService:====== 访问URL： ======http://api.openweathermap.org/data/2.5/weather?q=Beijing&appid=f04...8&units=metric&lang=zh_cn
```

## 7.4. **MCP 案例-WebFlux SSE传输模式**

在该案例中，我们会创建MCP Server 和MCP Client 两个SpringBoot项目，MCP Server项目中会创建一个getWeather工具，该工具通过OpenWeather可以查询某个城市天气情况；MCP Client 项目中创建相应的Controller，根据配置通过WebFlux SSE方式与MCP Server进行通信，实现调用天气工具。

### **7.4.1. Mcp Server开发**

按照如下步骤创建MCP Server对应的SpringBoot项目。**WebFlux SSE传输模式中创建的Mcp Server相比于STDIO传输模式中创建的MCPServer 只是在pom.xml中引入的依赖不同而已。**

**1) 创建SpringBoot项目**

SpringBoot项目命名为SpringAIMCPStdioServer，设置使用的JDK为17版本。

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/a4e32ebffcf642a3aeca6b893efa0892.jpg)

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/0364e3e9df6e4235928275f99404cbd9.jpg)

**2) 在项目中加入如下Maven依赖**

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.5.3</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.example</groupId>
    <artifactId>SpringAIMCPSseServer</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>SpringAIMCPSseServer</name>
    <description>SpringAIMCPSseServer</description>
    <properties>
        <java.version>17</java.version>
    </properties>

    <!-- 导入 Spring AI BOM，用于统一管理 Spring AI 依赖的版本，
    引用每个 Spring AI 模块时不用再写 <version>，只要依赖什么模块 Mavens 自动使用 BOM 推荐的版本 -->
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.ai</groupId>
                <artifactId>spring-ai-bom</artifactId>
                <version>1.0.0-SNAPSHOT</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <dependencies>
<!--        <dependency>-->
<!--            <groupId>org.springframework.boot</groupId>-->
<!--            <artifactId>spring-boot-starter-web</artifactId>-->
<!--        </dependency>-->

        <!-- 支持 SSE 传输，使用如下依赖 -->
        <dependency>
            <groupId>org.springframework.ai</groupId>
            <artifactId>spring-ai-starter-mcp-server-webflux</artifactId>
        </dependency>

        <!-- 依赖的json 包-->
        <dependency>
            <groupId>org.json</groupId>
            <artifactId>json</artifactId>
            <version>20210307</version>
        </dependency>
    </dependencies>

    <!-- 打包插件 -->
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <version>3.4.4</version>
                <configuration>
                    <mainClass>com.example.springaimcpstdioserver.SpringAimcpStdioServerApplication</mainClass>
                </configuration>
                <executions>
                    <execution>
                        <goals>
                            <goal>repackage</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>

    <!-- 声明仓库， 用于获取 Spring AI 以及相关预发布版本-->
    <repositories>
        <repository>
            <id>spring-snapshots</id>
            <name>Spring Snapshots</name>
            <url>https://repo.spring.io/snapshot</url>
            <releases>
                <enabled>false</enabled>
            </releases>
        </repository>
        <repository>
            <name>Central Portal Snapshots</name>
            <id>central-portal-snapshots</id>
            <url>https://central.sonatype.com/repository/maven-snapshots/</url>
            <releases>
                <enabled>false</enabled>
            </releases>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
    </repositories>

</project>
```

注意：

* 在该pom.xml中引入了“spring-ai-starter-mcp-server-webflux”MCP 依赖包，该包只支持SSE传输；
* 一个SpringBoot项目MVC 和 WebFlux 通常二选其一使用，不要引入“spring-boot-starter-web”依赖包，该包会启用 Spring MVC + 嵌入式 Tomcat，相当于是一套HTTP服务器组件，与Spring WebFlux + Reactor Netty 这套HTTP 服务组件冲突了，导致后续客户端HTTP请求报错。

**3) 配置resources/application.properties**

```
spring.application.name=SpringAIMCPSseServer

#指定 MCP 服务器的名称为 spring-ai-mcp-weather
spring.ai.mcp.server.name=spring-ai-mcp-weather

#配置应用监听的端口为 8089
server.port=8089

#禁用 Spring Boot 启动时的横幅（Banner）显示，对于使用 STDIO 传输的 MCP 服务器，禁用横幅有助于避免输出干扰。
spring.main.banner-mode=off
#如下参数启用并设置为空，将禁用控制台日志输出格式，减少输出干扰
logging.pattern.console=

#配置日志文件的输出路径，将日志写入指定的文件中
logging.file.name=D:/idea_space/SpringAICode/SpringAIMCPSseServer/model-context-protocol/mcp-weather-stdio-server.log

#访问 OpenWeather API 的密钥
OPEN_WEATHER_API_KEY=f04...8

```

特别注意：以上配置中logging.file.name指定了MCP Server运行过程中日志输出的位置，可以通过该日志查看Server端运行情况（如：工具是否被调用）。

**4) 创建 WeatherService.java构建查询天气工具**

在项目中创建service包，在该包中创建WeatherService.java类，构建查询天气工具：

```
package com.example.springaimcpsseserver.service;


import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * 天气服务类，用于获取指定城市的天气信息
 * @Service 标记为 Spring 服务层组件
 */

@Service
public class WeatherService {
    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather";

    @Value("${OPEN_WEATHER_API_KEY}")
    private String OPEN_WEATHER_API_KEY;

    /**
     * 根据城市名称获取天气信息（使用 OpenWeatherMap）
     * @param city 城市名称，如 "Beijing"
     * @return 天气信息文本
     */
    @Tool(description = "获取指定城市的当前天气情况，格式化后的天气报告字符串。")
    public String getWeather(@ToolParam(description = "城市名称，必须是英文格式，比如 London 或 Beijing") String city) {

        logger.info("====== 调用了getWeather工具 ======");

        try {
            String charset = "UTF-8";

            String query = String.format(
                    "q=%s&appid=%s&units=metric&lang=zh_cn",
                    URLEncoder.encode(city, charset),
                    URLEncoder.encode(OPEN_WEATHER_API_KEY, charset)
            );

            URL url = new URL(BASE_URL + "?" + query);
            logger.info("====== 访问URL： ======"+url.toString());

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), charset));

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject data = new JSONObject(response.toString());

            if (data.getInt("cod") == 404) {
                return "未找到该城市的天气信息。";
            }

            JSONObject main = data.getJSONObject("main");
            JSONArray weatherArray = data.getJSONArray("weather");
            JSONObject weather = weatherArray.getJSONObject(0);
            JSONObject wind = data.getJSONObject("wind");

            String weatherDescription = weather.optString("description", "无描述");
            double temperature = main.optDouble("temp", Double.NaN);
            double feelsLike = main.optDouble("feels_like", Double.NaN);
            double tempMin = main.optDouble("temp_min", Double.NaN);
            double tempMax = main.optDouble("temp_max", Double.NaN);
            int pressure = main.optInt("pressure", 0);
            int humidity = main.optInt("humidity", 0);
            double windSpeed = wind.optDouble("speed", Double.NaN);

            return String.format("""
                    城市: %s
                    天气描述: %s
                    当前温度: %.1f°C
                    体感温度: %.1f°C
                    最低温度: %.1f°C
                    最高温度: %.1f°C
                    气压: %d hPa
                    湿度: %d%%
                    风速: %.1f m/s
                    """,
                    data.optString("name", city),
                    weatherDescription,
                    temperature,
                    feelsLike,
                    tempMin,
                    tempMax,
                    pressure,
                    humidity,
                    windSpeed
            );

        } catch (Exception e) {
            return "获取天气信息时出错: " + e.getMessage();
        }
    }

    public static void main(String[] args) {
        //测试方法
        WeatherService client = new WeatherService();
        String beijing = client.getWeather("Beijing");
        System.out.println(beijing);
    }
}
```

注意如下两点：

* 如上代码中使用“@Tool”标记了方法getWeather为工具。
* 代码中不要System输出任何内容，会影响返回结果的处理。

**5) 主应用类中加入工具**

主应用类为SpringAimcpSseServerApplication.java，内容如下：

```
package com.example.springaimcpsseserver;

import com.example.springaimcpsseserver.service.WeatherService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringAimcpSseServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAimcpSseServerApplication.class, args);
    }

    /**
     * @Bean 注解用于将方法的返回值作为 Spring Bean 注册到 Spring 容器中。
     *  Spring 容器在启动过程中会扫描并执行所有带有 @Bean 注解的方法，以将其返回的对象注册到应用上下文中。
     *
     * ToolCallbackProvider 接口:Spring AI 提供的接口，其实现类负责将带有 @Tool 注解的方法注册为可供 AI 模型调用的工具。
     */
    @Bean
    public ToolCallbackProvider weatherTools(WeatherService weatherService) {
        return MethodToolCallbackProvider.builder().toolObjects(weatherService).build();
    }
}
```

这里在主应用类中通过@Bean注解创建ToolCallbackProvider类型，该类型是Spring AI 提供的接口，其实现类负责将指定Service类中带有 @Tool 注解的方法注册为可供 AI 模型调用的工具。

**6) 启动SpringAIMCPSseServer项目**

MCP Client与MCP Server使用SSE传输时，我们需要将MCP Server项目进行启动。

### **7.4.2. Mcp Client 开发**

按照如下步骤创建MCP Client的SpringBoot项目。该MCP Client 项目中可以使用不同的LLM模型，只需要在对应配置文件中引入不同的模型对应的apikey及相关依赖即可。

**WebFlux SSE传输模式中创建的Mcp Client相比于STDIO传输模式中创建的MCP Client** **有两点不同：**  **在pom.xml中引入的依赖不同**  **、在application.properties中配置远程Server** **。**

**1) 创建SpringBoot项目**

SpringBoot项目命名为SpringAIMCPSseClient，设置使用的JDK为17版本。

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/73744f4a5c944ce1a67fa8e4992ac2af.jpg)

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/c6e3757841524a97ab5439e78619cfc1.jpg)

**2) 在项目中加入如下Maven依赖**

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.5.3</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.example</groupId>
    <artifactId>SpringAIMCPSseClient</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>SpringAIMCPSseClient</name>
    <description>SpringAIMCPSseClient</description>


    <properties>
        <java.version>17</java.version>
    </properties>

    <!-- 导入 Spring AI BOM，用于统一管理 Spring AI 依赖的版本，
    引用每个 Spring AI 模块时不用再写 <version>，只要依赖什么模块 Mavens 自动使用 BOM 推荐的版本 -->
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.ai</groupId>
                <artifactId>spring-ai-bom</artifactId>
                <version>1.0.0-SNAPSHOT</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- 基于 WebFlux 的 SSE 传输实现的依赖 -->
        <dependency>
            <groupId>org.springframework.ai</groupId>
            <artifactId>spring-ai-starter-mcp-client-webflux</artifactId>
        </dependency>

        <!-- DeepSeek 模型依赖 -->
        <dependency>
            <groupId>org.springframework.ai</groupId>
            <artifactId>spring-ai-starter-model-deepseek</artifactId>
        </dependency>
    </dependencies>


    <!-- 声明仓库， 用于获取 Spring AI 以及相关预发布版本-->
    <repositories>
        <repository>
            <id>spring-snapshots</id>
            <name>Spring Snapshots</name>
            <url>https://repo.spring.io/snapshot</url>
            <releases>
                <enabled>false</enabled>
            </releases>
        </repository>
        <repository>
            <name>Central Portal Snapshots</name>
            <id>central-portal-snapshots</id>
            <url>https://central.sonatype.com/repository/maven-snapshots/</url>
            <releases>
                <enabled>false</enabled>
            </releases>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
    </repositories>

</project>

```

注意：以上依赖中引入“spring-ai-starter-mcp-client-webflux”包，该包是基于WebFlux SSE传输客户端依赖包；“spring-ai-starter-model-deepseek”包是引入deepseek LLM，使用哪个LLM可以引入相应的依赖，支持的模型以及引入的包查看：https://docs.spring.io/spring-ai/reference/api/chat/openai-chat.html。

**3) 配置resources/application.properties**

MCP Client通过Web Flux SSE方式连接到MCP Server需要在项目的resources/application.properties文件中配置MCP Server地址。

```
spring.application.name=SpringAIMCPSseClient

server.port=8087

#配置 Deepseek的基础URL、密钥和使用模型
spring.ai.deepseek.base-url=https://api.deepseek.com
spring.ai.deepseek.api-key=sk-...1
spring.ai.deepseek.chat.options.model=deepseek-chat

#SSE 模式，配置名为 server1 的 MCP 服务器连接，远程连接到指定的服务器地址
spring.ai.mcp.client.sse.connections.server1.url=http://localhost:8089
```

注意：需要通过在配置文件中指定“spring.ai.mcp.client.sse.connections.server1.url”参数来让MCP Client找到MCP Server访问地址。

**4) 项目中创建config/Config.java类**

Config.java类为Spring Configuration组件，该组件中注册两个Bean：

* SyncMcpToolCallbackProvider:自动集成 MCP Server 暴露的工具到 ChatClient，使 LLM 可通过 prompt 流调用工具。
* ChatClient：LLM聊天客户端。构建ChatClient时通过“defaultToolCallbacks(toolCallbackProvider)”方法配置工具回调提供者，使用LLM可以知道可以有对应的工具调用。

```
package com.example.springaimcpsseclient.config;

import io.modelcontextprotocol.client.McpSyncClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class Config {

    /**
     * SyncMcpToolCallbackProvider:自动集成 MCP Server 暴露的工具到 ChatClient，使 LLM 可通过 prompt 流调用工具
     *  mcpSyncClient:SpringAI 根据 spring-ai-starter-mcp-client/spring-ai-starter-mcp-client-webflux
     *  依赖注入的 mcpSyncClients 列表，用于调用外部工具或服务，每个 McpSyncClient 对象代表一个到 MCP Server（通过 STDIO 或 SSE 等方式）的连接实例，
     *  如果你只配置了一个 MCP Server，clients 列表中就只有一个 McpSyncClient；如果配置了多个 Server，则会有多个连接，它们对应不同来源的工具。
     */
    @Bean
    public SyncMcpToolCallbackProvider toolCallbackProvider(List<McpSyncClient> mcpSyncClients) {
        return new SyncMcpToolCallbackProvider(mcpSyncClients);
    }


    @Bean
    public ChatClient chatClient(ChatModel chatModel,
                                 SyncMcpToolCallbackProvider toolCallbackProvider) {

        return ChatClient.builder(chatModel)
                .defaultSystem("你是一个非常有帮助的助手，你可以使用工具来帮助回答问题")
                //配置工具回调提供者，使 AI 能调用外部工具
                .defaultToolCallbacks(toolCallbackProvider)
                .build();
    }

}
```

注意：toolCallbackProvider方法中的参数mcpSyncClients表示SpringAI 根据 spring-ai-starter-mcp-client/spring-ai-starter-mcp-client-webflux依赖注入的 mcpSyncClients 列表，用于调用外部工具或服务，每个 McpSyncClient 对象代表一个到 MCP Server（通过 STDIO 或 SSE 等方式）的连接实例，如果你只配置了一个 MCP Server，clients 列表中就只有一个 McpSyncClient；如果配置了多个 Server，则会有多个连接，它们对应不同来源的工具。

**5) 创建controller/ChatController.java类**

在Controller中直接创建对应的方法与LLM进行对话调用工具即可。

```
package com.example.springaimcpsseclient.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
public class ChatController {
    private final ChatClient chatClient;

    public ChatController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/chat")
    public String chat(@RequestParam String message) {

        return chatClient.prompt()
                .user(message)
                .call()
                .content();

    }
}
```

**6) 启动MCP Client进行测试**

运行“SpringAimcpSseClientApplication.java”后，在浏览器中输入如下内容进行测试LLM模型自动调用工具：

```
# http://localhost:8087/ai/chat?message=北京天气如何?
北京的天气情况如下： - **天气描述**: 小雨 - **当前温度**: 27.0°C - **体感温度**: 30.0°C - **最低温度**: 27.0°C - **最高温度**: 27.0°C - **气压**: 1004 hPa - **湿度**: 84% - **风速**: 1.6 m/s 请注意携带雨具！

# http://localhost:8087/ai/chat?message=你是谁?
我是一个智能助手，可以帮助你回答问题、提供信息、解决问题或完成任务。如果你有任何问题或需要帮助，随时告诉我！
```

可以在MCP Server端配置的日志路径中找到对应的日志，查看到日志中会有相应工具调用：

```
WeatherService:====== 调用了getWeather工具 ======
WeatherService:====== 访问URL： ======http://api.openweathermap.org/data/2.5/weather?q=Beijing&appid=f04...8&units=metric&lang=zh_cn
```

# 8. **检索增强生成（RAG）**

Spring AI 通过模块化架构支持 RAG，既可以让你自定义 RAG 流程，也可以使用 Advisor API 提供的开箱即用 RAG 流程，本章节给大家介绍使用Advisor API 提供的开箱即用的RAG内容。

在 Retrieval-Augmented Generation (RAG) 中，我们常见的流程是：**Query → Embedding → VectorStore 检索 → 附加上下文 → LLM 回答**，一般VectorStore我们会选择向量数据库，在大模型领域常用的向量数据库是Milvus，下面首先介绍Milvus。

## 8.1. **向量数据库Milvus(米尔乌斯)**

Milvus 是一个开源的高性能向量数据库（Vector Database），专为存储、索引和检索高维向量数据而设计，它能够处理图像、音频、视频、自然语言等嵌入表示（embeddings），支持海量向量（万亿级）毫秒级相似搜索。

Milvus 是 RAG 系统中的核心基础设施之一，Milvus 在RAG过程中可以担当“VectorStore”的角色，能够精准语义检索，查询到相关文档。Milvus文档地址：[https://milvus.io/docs/zh](https://milvus.io/docs/zh,Milvus)

### **8.1.1. Milvus Standalone搭建**

Milvus支持三种方式搭建：

* Milvus Lite:作为 Python 库本地嵌入（类似“向量版 SQLite”）,支持百万级别向量存储，适合快速学习。
* Milvus Standalone（单机版）:Docker 中运行，所有组件（Milvus + etcd）打包在一台主机内，支持10亿向量存储，适合中小型生产环境。
* Milvus Distributed（分布式集群）:部署在 Kubernetes 环境，通过 Proxy、Query、Index 等节点分离，实现高可用与弹性伸缩，支持百亿向量存储，适合大规模、高可用或K8s部署。

这里我们搭建Milvus Standalone 版本，所以需要预先准备一台Linux节点（默认已准备好），按照如下步骤在Linux节点中安装Docker并部署Milvus。

**1) 在Linux中安装docker**

这里在node2安装docker。获取docker repo文件

```

#从阿里源获取docker repo文件
wget -O /etc/yum.repos.d/docker-ce.repo https://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo

#从清华源获取docker repo文件
wget -O /etc/yum.repos.d/docker-ce.repo  https://mirrors.tuna.tsinghua.edu.cn/docker-ce/linux/centos/docker-ce.repo

#从docker官方源获取docker repo文件
wget -O /etc/yum.repos.d/docker-ce.repo  https://download.docker.com/linux/centos/docker-ce.repo
```

查看docker可以安装的版本：

```
yum list docker-ce.x86_64 --showduplicates | sort -r
```

安装docker:这里指定docker版本为26.1.4版本:

```
yum -y install docker-ce-26.1.4-1.el7
```

设置docker 开机启动，并启动docker：

```
systemctl enable docker
systemctl start docker
```

查看docker版本

```
docker version
```

修改cgroup方式，并重启docker。

```
vim /etc/docker/daemon.json

{
        "exec-opts": ["native.cgroupdriver=systemd"],
        "registry-mirrors":[
          "https://docker.m.daocloud.io",
          "https://docker.rainbond.cc",
          "https://docker.lmirror.top"
       ]
}

#重启docker
systemctl restart docker
```

**2) 使用docker compose 安装Milvus**

```
#创建目录并进入
mkdir -p /software/milvus && cd /software/milvus

#下载compose 配置,或者直接将资料中 docker-compose.yml上传至目录下
wget https://github.com/milvus-io/milvus/releases/download/v2.5.14/milvus-standalone-docker-compose.yml -O docker-compose.yml

#启动 Milvus
docker compose up -d

#停止Milvus
docker compose down
```

特别注意：通过如下命令将linux中docker的所有image打包到“all\_milvus\_images.tar”中：

```
docker save -o all_milvus_images.tar milvusdb/milvus:v2.5.14 quay.io/coreos/etcd:v3.5.18 minio/minio:RELEASE.2024-05-28T17-19-04Z
```

在目标linux节点上（已安装docker），使用如下命令将all\_milvus\_images.tar导入到目标计算机中：

```
docker load -i all_milvus_images.tar
```

**3) 访问Milvus WebUI**

浏览器输入 [http://node2:9091/webui/](http://node2:9091/webui/) 查看Milvus WebUI。

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/79e0be5262d14c9299add9c808470449.jpg)

**4) 安装 attu可视化操作Milvus**

Attu是一款专为Milvus向量数据库打造的开源数据库管理工具，提供了便捷的图形化界面，极大地简化了对Milvus数据库的操作与管理流程。

双击资料中“attu-Setup-2.5.12.exe”进行Attu安装，安装完成后进入Attu并连接Milvus：

![](//:0)![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/7a01e8c5b20a42c9a558d167345329a7.jpg)

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/f450868c43bd45918f20100df2c41911.jpg)

### **8.1.2. Milvus 核心概念**

Milvus核心概念有数据库、Collections、Schema，下面分别介绍。

**1. 数据库**

在 Milvus 中，数据库是组织和管理数据的逻辑单元。为了提高数据安全性并实现多租户，你可以创建多个数据库，为不同的应用程序或租户从逻辑上隔离数据。例如，创建一个数据库用于存储用户 A 的数据，另一个数据库用于存储用户 B 的数据。与关系型数据库中所说的数据库一个概念。

**2. Collections**

在 Milvus 中，Collection 可以比作关系存储系统中的表。Collections 是 Milvus 中最大的数据单元，在 Milvus 中，所有数据都是按 Collections（集合）、shard（分片）、partition（分区）、segment（数据片段） 和 entity （数据实体，一行数据就是一个实体）组织的。

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/48bc1d9c0e714bba9c79931a641e5057.jpg)

为了在写入数据时充分利用集群的并行计算能力，Milvus 中的 Collection 必须将数据写入操作分散到不同的节点上，默认情况下，一个 Collections 包含两个分片，根据你的数据集容量，你可以在一个 Collection 中拥有更多的分片。Milvus 使用主密钥散列方法进行分片。关于Milvus数据模型参考：https://milvus.io/zh/blog/deep-dive-1-milvus-architecture-overview.md#Shard

Collection 是一个二维表，具有固定的列和变化的行。每列代表一个字段，每行代表一个实体。下图显示了一个有 8 列和 6 个实体的 Collection。

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/707c5f6cb382427d8362a5773bc8878f.jpg)

**3. Schema**

Schema 定义了 Collections 的数据结构。在创建一个 Collection 之前，你需要设计出它的 Schema，定义如何组织 Collection 中的数据。一个 Collection Schema 有一个主键、最多四个向量字段和几个标量字段。下图说明了如何将文章映射到模式字段列表。

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/63719224bca245d48fd4eab300fefd56.jpg)

Milvus支持的更多类型参考：https://milvus.io/docs/zh/schema.md

**4. 数据一致性**

Milvus 提供了四种一致性级别，允许用户在数据一致性和查询延迟之间做出平衡选择：

* Strong：最强一致性，保证读取到最新数据，但查询延迟较高。
* Bounded：有界一致性，默认级别，允许轻微延迟，满足大多数场景使用。
* Session:会话一致性，保证在同一客户端会话中读取到自己写入的数据，跨会话数据可能不一致。
* Eventually：最终一致性，相应查询最快，数据一致性最弱，适用于实时监控、日志展示等。

关于以上一致性的原理参考：[https://milvus.io/docs/zh/tune\_consistency.md#Set-Consistency-Level-upon-Creating-Collection。](https://milvus.io/docs/zh/tune_consistency.md#Set-Consistency-Level-upon-Creating-Collection%E3%80%82)

### **8.1.3. Milvus Cli操作**

Milvus中提供了Milvus Cli命令行方式对Milvus进行操作，Milvus Cli 基于Python，要求python3.8.5以上，可以使用“pip install milvus-cli”进行安装，如下是在windows中基于anconda（默认已经安装）安装python3.9环境并在该环境中安装 milvus-cli，打开cmd后执行如下命令：

```
# 创建一个名为python_milvus的环境，指定Python版本是3.9
conda create --name python_milvus python=3.9.23

# 安装好后，使用activate激活该环境
activate python_milvus

# 安装milvus-cli
pip install milvus-cli

# 安装 readline（在window中使用 milvus_cli时需要改模块）
pip install pyreadline3

#如果不行使用该环境，可以执行如下删除该环境
conda remove --name python_milvus --all
```

下面进入到 milvus-cli 中连接Milvus并进行基本操作。

**1. 连接Milvus**

```
# 进入 milvus 客户端
> milvus_cli

# 连接milvus_cli ,默认用户名和密码为：root:Milvus
milvus_cli > connect -uri http://node2:19530 -t root:Milvus
Connect Milvus successfully.
Error occurred!
list index out of range
```

解释：-uri:连接Milvus的uri 名称，默认为 "http://127.0.0.1:19530"；-t:连接Milvus的令牌，指定用户名和密码;“Error occurred！”可能是个bug，先忽略。

**2. 数据库操作（创建、列出、使用、删除）**

```
# 创建数据库
milvus_cli > create database -db testdb
Create database testdb successfully!

#列出数据库
milvus_cli > list databases
+---------+
| db_name |
+---------+
| default |
| testdb  |
+---------+

#使用数据库
milvus_cli > use database -db testdb
Using database testdb successfully!

#删除数据库
milvus_cli > delete database -db testdb
Drop database testdb successfully!
```

**3. 用户操作（创建、列出、删除）**

```
# 创建用户，-u 用户名；-p 密码
milvus_cli > create user -u zs -p123456
Create user successfully!
['root', 'zs']

#列出所有用户
milvus_cli > list users
['root', 'zs']

#删除用户，-u 指定删除的用户
milvus_cli > delete user -u zs
Warning!
You are trying to delete the user in milvus. This action cannot be undone!

Do you want to continue? [y/N]: y
Delete user zs successfully!
['root']
```

通过Milvus Cli 操作Collection目前存在问题，我们后续通过Java SDK来演示Collection操作。

### **8.1.4. Milvus Java API 操作**

Java API 操作Milvus 需要JDK8版本以上，maven中需要导入如下依赖：

```
<dependency>
  <groupId>io.milvus</groupId>
  <artifactId>milvus-sdk-java</artifactId>
  <version>2.6.0</version>
</dependency>
```

这里以Java操作Milvus中的Collection为例介绍Milvus Java API使用。完整的 Java操作代码如下：

```
package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.milvus.param.collection.FlushParam;
import io.milvus.v2.client.ConnectConfig;
import io.milvus.v2.client.MilvusClientV2;
import io.milvus.v2.common.DataType;
import io.milvus.v2.common.IndexParam;
import io.milvus.v2.service.collection.request.*;
import io.milvus.v2.service.collection.response.DescribeCollectionResp;
import io.milvus.v2.service.utility.request.FlushReq;
import io.milvus.v2.service.vector.request.DeleteReq;
import io.milvus.v2.service.vector.request.GetReq;
import io.milvus.v2.service.vector.request.InsertReq;
import io.milvus.v2.service.vector.request.SearchReq;
import io.milvus.v2.service.vector.response.DeleteResp;
import io.milvus.v2.service.vector.response.GetResp;
import io.milvus.v2.service.vector.response.InsertResp;
import io.milvus.v2.service.vector.response.SearchResp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestMilvusCollection {

    static String CLUSTER_ENDPOINT = "http://node2:19530";
    static String TOKEN = "root:Milvus";

    public static void main(String[] args) {
        //1. 连接到 Milvus
        ConnectConfig connectConfig = ConnectConfig.builder()
                .uri(CLUSTER_ENDPOINT)
                .token(TOKEN)
                .build();

        MilvusClientV2 client = new MilvusClientV2(connectConfig);


        String collectionName = "testCollection";

        //1. 创建一个集合
        createCollection(client, collectionName);

        //2. 列出集合
        List<String> collectionNames = client.listCollections().getCollectionNames();
        System.out.println("Collections: " + collectionNames);

        //3.插入数据
        insertDataToCollection(client, collectionName);

        //4.查询数据
        GetResp getResp = client.get(GetReq.builder()
                .collectionName(collectionName)// 查询集合名称
                .ids(List.of(0, 1, 2)) // 查询 id 为 0 和 1 的数据
                .outputFields(List.of("id", "color")) // 查询 id 和 color 字段
                .build());
        getResp.getResults.forEach(result->{
               System.out.println("查询结果："+result);
        });


        //4.根据主键删除数据
        DeleteResp delete = client.delete(DeleteReq.builder()
                .collectionName(collectionName)// 删除集合名称
                .ids(List.of(0, 1)) // 删除 id 为 0 和 1 的数据
                .build()
        );
        System.out.println("删除数据反馈："+delete);

        //5.删除集合
//        client.dropCollection(DropCollectionReq.builder()
//               .collectionName(collectionName)
//               .build());
//        System.out.println("删除集合："+collectionName);

    }


    private static void insertDataToCollection(MilvusClientV2 client, String collectionName) {

        //3.1 准备 json 数据
        Gson gson = new Gson();
        List<JsonObject> data = Arrays.asList(
                gson.fromJson("{\"id\": 0, \"vector\": [0.3580376395471989, -0.6023495712049978, 0.18414012509913835, -0.26286205330961354, 0.9029438446296592], \"color\": \"pink_8683\"}", JsonObject.class),
                gson.fromJson("{\"id\": 1, \"vector\": [0.19886812562848388, 0.06023560599112088, 0.6976963061752597, 0.2614474506242501, 0.838729485096104], \"color\": \"red_7025\"}", JsonObject.class),
                gson.fromJson("{\"id\": 2, \"vector\": [0.43742130801983836, -0.5597502546264526, 0.6457887650909682, 0.7894058910881185, 0.20785793220625592], \"color\": \"orange_6781\"}", JsonObject.class),
                gson.fromJson("{\"id\": 3, \"vector\": [0.3172005263489739, 0.9719044792798428, -0.36981146090600725, -0.4860894583077995, 0.95791889146345], \"color\": \"pink_9298\"}", JsonObject.class),
                gson.fromJson("{\"id\": 4, \"vector\": [0.4452349528804562, -0.8757026943054742, 0.8220779437047674, 0.46406290649483184, 0.30337481143159106], \"color\": \"red_4794\"}", JsonObject.class),
                gson.fromJson("{\"id\": 5, \"vector\": [0.985825131989184, -0.8144651566660419, 0.6299267002202009, 0.1206906911183383, -0.1446277761879955], \"color\": \"yellow_4222\"}", JsonObject.class),
                gson.fromJson("{\"id\": 6, \"vector\": [0.8371977790571115, -0.015764369584852833, -0.31062937026679327, -0.562666951622192, -0.8984947637863987], \"color\": \"red_9392\"}", JsonObject.class),
                gson.fromJson("{\"id\": 7, \"vector\": [-0.33445148015177995, -0.2567135004164067, 0.8987539745369246, 0.9402995886420709, 0.5378064918413052], \"color\": \"grey_8510\"}", JsonObject.class),
                gson.fromJson("{\"id\": 8, \"vector\": [0.39524717779832685, 0.4000257286739164, -0.5890507376891594, -0.8650502298996872, -0.6140360785406336], \"color\": \"white_9381\"}", JsonObject.class),
                gson.fromJson("{\"id\": 9, \"vector\": [0.5718280481994695, 0.24070317428066512, -0.3737913482606834, -0.06726932177492717, -0.6980531615588608], \"color\": \"purple_4976\"}", JsonObject.class)
        );

        //3.2 插入数据
        InsertResp insertResp = client.insert(InsertReq.builder()
                .collectionName(collectionName)
                .data(data)
                .build());


        //3.3 刷新数据 ，否则查询不到数据
        FlushReq flushReq = FlushReq.builder()
               .collectionNames(Arrays.asList(collectionName))
               .build();

        client.flush(flushReq);

        System.out.println("插入数据反馈："+insertResp);
    }

    public static void createCollection(MilvusClientV2 client, String collectionName) {
        //1.1 先创建集合 schema
        CreateCollectionReq.CollectionSchema schema = MilvusClientV2.CreateSchema()
                .addField(AddFieldReq.builder()
                        .fieldName("id")
                        .dataType(DataType.Int64)
                        .isPrimaryKey(true)
                        .autoID(false)
                        .build())
                .addField(AddFieldReq.builder()
                        .fieldName("vector")
                        .dataType(DataType.FloatVector)
                        .dimension(5)
                        .build())
                .addField(AddFieldReq.builder()
                        .fieldName("color")
                        .dataType(DataType.VarChar)
                        .maxLength(512)
                        .build());


        //1.2 如果要创建索引，必须为 vector 列创建索引，否则无法创建表
        // 建议对vector列创建索引，方便高效向量搜索
        List<IndexParam> indexParams = new ArrayList<>();

        IndexParam vector = IndexParam.builder()
                .fieldName("vector")
                .indexType(IndexParam.IndexType.IVF_FLAT) // 向量索引类型
                .metricType(IndexParam.MetricType.COSINE) // 余弦相似度
                .build();

        indexParams.add(vector);

        //1.3 创建集合
        if (client.hasCollection(HasCollectionReq.builder().collectionName(collectionName).build())) {
            System.out.println("Collection already exists: " + collectionName);
            return;
        }

        client.createCollection(CreateCollectionReq.builder()
               .collectionName(collectionName) // 集合名称
               .collectionSchema(schema) // 集合 schema
               .indexParams(indexParams) // 索引参数
               .build());

        System.out.println("Collection created: " + collectionName);

    }
}

```

以上代码注意如下几点：

* 创建Collection时需要指定Schema，如果表中有vector字段必须设置索引，加快查询速度。
* 向Collection中插入数据后，需要执行flush，否则查询不到数据。
* 在代码运行过程中可以结合Attu工具查看Milvus中的Collection及其内容。

## 8.2. **Spring AI RAG介绍**

Spring AI 提供了对常见 RAG 流程的开箱支持，通过 Advisor API 实现，向量数据库存储着AI模型所不知道的数据，当用户提问时，Advisor会向向量数据库查询与用户问题相关的文档，将检索结果附加到用户文本中，然后通过AI大模型推理生成最终回答。

在RAG流程中，有两种Advisor：QuestionAnswerAdvisor和RetrievalAugmentationAdvisor。

* **QuestionAnswerAdvisor**

当用户提出问题时，QuestionAnswerAdvisor 会帮助系统从已有的知识库或数据库中检索答案，并将其返回给用户，其核心目的是将用户的问题与知识库中的答案进行匹配，提供答案。QuestionAnswerAdvisor 侧重问答处理，通过理解和分析用户的问题，找到最合适的答案

* **RetrievalAugmentationAdvisor**

当需要从大型数据库或知识库中检索信息时，RetrievalAugmentationAdvisor 会在检索请求前后插入增强逻辑。例如，在查询过程中，RetrievalAugmentationAdvisor 可以自动加入额外的上下文信息（如用户的历史查询、偏好、领域知识等），以提高检索结果的相关性和准确性。RetrievalAugmentationAdvisor 侧重 信息检索，通过增强检索条件和上下文信息来优化查询过程。

_备注：Advisor（顾问）是 Spring AI 中用于增强方法执行逻辑的组件，类似于一个拦截器，通过 Advisor，可以在方法执行前后插入自定义逻辑，从而提升代码的可扩展性与灵活性，常用于任务增强、数据预处理等场景。_

使用以上两种Advisor时需要在项目maven pom.xml导入如下依赖：

```
<!-- QuestionAnswerAdvisor 依赖包-->
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-advisors-vector-store</artifactId>
</dependency>

<!--  RetrievalAugmentationAdvisor  依赖包-->
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-rag</artifactId>
</dependency>
```

下面通过SpringBoot程序演示两种Advisor的使用方式，该案例中我们将固定的文档片段存入到Milvus向量库，然后通过chatClient 输入问题，分别使用两种Advisor进行检索向量库进行回答。

**1) 创建SpringBoot项目**

SpringBoot项目命名为SpringAIRAGWithAdvisor，设置使用的JDK为17版本。

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/5c4a866ae6e34526a3548493bee1fccf.jpg)

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/2edd8178d4d14a9996b256d061c3a477.jpg)

**2) 在项目中加入如下Maven依赖**

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.5.3</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.example</groupId>
    <artifactId>SpringAIRAGWithAdvisor</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>SpringAIRAGWithAdvisor</name>
    <description>SpringAIRAGWithAdvisor</description>


    <properties>
        <java.version>17</java.version>
    </properties>

    <!-- 导入 Spring AI BOM，用于统一管理 Spring AI 依赖的版本，
    引用每个 Spring AI 模块时不用再写 <version>，只要依赖什么模块 Mavens 自动使用 BOM 推荐的版本 -->
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.ai</groupId>
                <artifactId>spring-ai-bom</artifactId>
                <version>1.0.0-SNAPSHOT</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- Deepseek AI 依赖 -->
        <dependency>
            <groupId>org.springframework.ai</groupId>
            <artifactId>spring-ai-starter-model-deepseek</artifactId>
        </dependency>

        <!-- 智普AI 依赖 -->
        <dependency>
            <groupId>org.springframework.ai</groupId>
            <artifactId>spring-ai-starter-model-zhipuai</artifactId>
        </dependency>

        <!-- QuestionAnswerAdvisor 依赖包-->
        <dependency>
            <groupId>org.springframework.ai</groupId>
            <artifactId>spring-ai-advisors-vector-store</artifactId>
        </dependency>

        <!--  RetrievalAugmentationAdviso  依赖包-->
        <dependency>
            <groupId>org.springframework.ai</groupId>
            <artifactId>spring-ai-rag</artifactId>
        </dependency>

        <!--  Milvus VectorStore 依赖包-->
        <dependency>
            <groupId>org.springframework.ai</groupId>
            <artifactId>spring-ai-starter-vector-store-milvus</artifactId>
        </dependency>

    </dependencies>

    <!-- 声明仓库， 用于获取 Spring AI 以及相关预发布版本-->
    <repositories>
        <repository>
            <id>spring-snapshots</id>
            <name>Spring Snapshots</name>
            <url>https://repo.spring.io/snapshot</url>
            <releases>
                <enabled>false</enabled>
            </releases>
        </repository>
        <repository>
            <name>Central Portal Snapshots</name>
            <id>central-portal-snapshots</id>
            <url>https://central.sonatype.com/repository/maven-snapshots/</url>
            <releases>
                <enabled>false</enabled>
            </releases>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
    </repositories>

</project>
```

注意：

* 在该pom.xml中引入了“spring-ai-starter-model-zhipuai”依赖包用于文档片段转换成向量存入向量库；
* 在该pom.xml中引入了“spring-ai-starter-model-deepseek”依赖包用于chatClient聊天。
* 在该pom.xml中引入了“spring-ai-advisors-vector-store”和“spring-ai-rag”依赖包，两个依赖包分别为QuestionAnswerAdvisor和RetrievalAugmentationAdvisor依赖。
* 在该pom.xml中引入了“spring-ai-starter-vector-store-milvus”依赖包，该依赖为Milvus VectorStore 依赖包。

**3) 配置resources/application.properties**

```
spring.application.name=SpringAIRAGWithAdvisor

server.port=8080

## 使用 智普AI Embedding 模型，需要在pom.xml中引入对应依赖
spring.ai.zhipuai.api-key=d...4
spring.ai.zhipuai.base-url=https://open.bigmodel.cn/api/paas
spring.ai.zhipuai.embedding.options.model=embedding-2

## 配置 Chat Model:Deepseek的基础URL、密钥和使用模型
spring.ai.deepseek.base-url=https://api.deepseek.com
spring.ai.deepseek.api-key=sk-8...1
spring.ai.deepseek.chat.options.model=deepseek-chat

## Milvus 配置
spring.ai.vectorstore.milvus.client.host=node2
spring.ai.vectorstore.milvus.client.port=19530
# 默认用户名和密码为 root 和 Milvus
spring.ai.vectorstore.milvus.client.token=root:Milvus
# 要使用的 Milvus 数据库的名称
spring.ai.vectorstore.milvus.database-name=default
# 用于存储 vector 的 Milvus 集合名称
spring.ai.vectorstore.milvus.collection-name=vector_store
#是否自动初始化 schema，如：vector_store如果没有会自动创建
spring.ai.vectorstore.milvus.initialize-schema=true
# Milvus 集合中要存储的 vector 的维度，默认为1536，要与所使用的 embedding 模型的维度匹配（如：DeepSeek、ZhiPuAI 或其他，都是1024维）
spring.ai.vectorstore.milvus.embedding-dimension=1024
```

**4) 构建两个Rag Advisor和Chat Client**

在项目中创建config/AIConfig.java ，该类标记为Configuration类。类中注入QuestionAnswerAdvisor、RetrievalAugmentationAdvisor以及ChatClient，并且每次运行SpringBoot项目时向Milvus中插入指定文档向量数据（如果已存在则不重复插入）。

```
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
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.milvus.MilvusVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AIConfig {

    // 注入 MilvusVectorStore 实例
    @Autowired
    private MilvusVectorStore vectorStore;

    // 从配置文件中读取 Milvus 集合名称
    @Value("${spring.ai.vectorstore.milvus.collection-name}")
    private String collectionName;


    //配置 QuestionAnswerAdvisor，用于问答任务
    @Bean
    public QuestionAnswerAdvisor questionAnswerAdvisor() {
        return QuestionAnswerAdvisor.builder(vectorStore)
                // 配置向量搜索的参数，这里设置了相似度阈值为 0.5，返回前 6 个结果
                .searchRequest(SearchRequest.builder().similarityThreshold(0.5d).topK(6).build())
                .build();
    }


    //配置 RetrievalAugmentationAdvisor，用于增强检索任务
    @Bean
    public RetrievalAugmentationAdvisor retrievalAugmentationAdvisor() {

        //VectorStoreDocumentRetriever 负责在向量数据库中根据查询获取最相关的文档
        //支持根据相似度阈值、top-K（返回前K个最相关文档）和过滤表达式等方式进行精确检索
        VectorStoreDocumentRetriever retriever = VectorStoreDocumentRetriever.builder()
                .vectorStore(vectorStore) // 设置向量存储对象
                .similarityThreshold(0.5)//设置相似度阈值
                .topK(6)//设置返回前K个最相关文档
                .build();

        //ContextualQueryAugmenter 用于增强查询，根据上下文信息生成更具体的查询
        //支持基于上下文的查询增强，例如根据用户输入的上下文信息生成更具体的查询，提高检索的准确性和效率
        ContextualQueryAugmenter cqa = ContextualQueryAugmenter.builder()
                .allowEmptyContext(true)//允许空上下文
                .build();

        //返回 RetrievalAugmentationAdvisor 对象，用于增强检索任务
        return RetrievalAugmentationAdvisor.builder()
                .documentRetriever(retriever) // 设置文档检索器
                .queryAugmenter(cqa) // 设置查询增强器
                .build();
    }


    //配置 ChatClient，初始化聊天客户端
    @Bean
    public ChatClient chatClient(DeepSeekChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .defaultSystem("你是一个助手，回答用户问题是不要提及回复是从上下文信息中获取的，" +
                        "不要回答你不知道的问题，如果你不知道答案，就回答：抱歉，我不清楚这个问题。")
                .build();
    }


    /**
     * @PostConstruct 注解的方法会在 Spring 容器完成依赖注入后自动调用一次，常用于执行初始化逻辑。
     */
    @PostConstruct
    public void initVectorData() {
        System.out.println("初始化向量数据，写入到 Milvus 数据库中...");

        // 获取 Milvus 客户端实例
        MilvusServiceClient client = (MilvusServiceClient)vectorStore.getNativeClient().get();

        // 获取集合统计信息，查询集合的记录数量
        R<GetCollectionStatisticsResponse> resp = client.getCollectionStatistics(GetCollectionStatisticsParam.newBuilder()
                .withCollectionName(collectionName)
                .build());

        // 获取实体数量，即数据行数
        long rowCount = new GetCollStatResponseWrapper(resp.getData()).getRowCount();

        System.out.println("Milvus vector_store 中数据数量：" + rowCount);

        //如果集合没有数据就写入
        if(rowCount==0){
            System.out.println("Milvus vector_store 中没有数据，写入...");
            List<Document> docs = List.of(
                    new Document("Spring AI 是一个开源 AI 集成项目"),
                    new Document("Milvus 是一款高性能向量数据库"),
                    new Document("DeepSeek 是一个开源大语言模型")
            );

            //vectorStore.add(docs) 调用时，Spring AI 的 MilvusVectorStore 会使用注入的 EmbeddingModel 将每个 Document 中的文本内容转换成向量并写入
            vectorStore.add(docs);

            // 获取 Milvus 客户端并手动调用 flush，刷新索引，确保数据被持久化到 Milvus 数据库中
            System.out.println("Milvus vector_store 刷新索引...");
            client.flush(FlushParam.newBuilder()
                    .withCollectionNames(List.of(collectionName)) // 设置刷新操作的集合名称
                    .build());
        }

    }
}

```

**5) 创建RagController.java**

```
package com.example.springairagwithadvisor.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ai")
public class RagController {

    @Autowired
    private ChatClient chatClient;
    @Autowired
    private QuestionAnswerAdvisor qaAdvisor;
    @Autowired
    private RetrievalAugmentationAdvisor ragAdvisor;


    @GetMapping("/chat1")
    public String ask1(@RequestParam String message) {
        return chatClient.prompt()
                .user(message)
                //设置使用 QuestionAnswerAdvisor
                .advisors(List.of(qaAdvisor))
                .call()
                .content();
    }

    @GetMapping("/chat2")
    public String ask2(@RequestParam String message) {
        return chatClient.prompt()
                .user(message)
                //设置使用 RetrievalAugmentationAdvisor
                .advisors(List.of(ragAdvisor))
                .call()
                .content();
    }
}
```

**6) 启动Milvus**

在linux node2 节点启动Milvus：

```
#进入 milvus目录
cd /software/milvus

#启动 Milvus
docker compose up -d
```

**7) 启动项目并测试**

启动SpringAiRAGWithAdvisor项目，运行主应用类SpringAiragWithAdvisorApplication.java，启动该项目。

在浏览器中输入如下内容进行测试：

```
# http://localhost:8080/ai/chat1?message=Milvus是什么？
Milvus是一款高性能向量数据库。

# http://localhost:8080/ai/chat1?message=今天天气如何？
抱歉，我不清楚这个问题。

#http://localhost:8080/ai/chat2?message=Spring AI是什么？
Spring AI 是一个开源 AI 集成项目。

#http://localhost:8080/ai/chat2?message=今天天气如何？
抱歉，我不清楚这个问题。
```

## 8.3. **导游考试RAG案例**

下面通过导游考试RAG案例来演示Spring AI 中RAG 从零到一综合开发，这个案例中涉及如下内容：

1. 从本地doc文件构建知识库，将内容存入Milvus中。

2. 使用QuestionAnswerAdvisor和RetrievalAugmentationAdvisor 实现RAG。

按照如下步骤实现该案例：

**1) 创建SpringBoot项目**

SpringBoot项目命名为SpringAIRAGDemo，设置使用的JDK为17版本。

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/e0fbffb6a8c84ce2a9526ae1270a2af5.jpg)

![](//:0) ![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/20/1751796495022/02960aa1d27949658c259b722ffc8be0.jpg)

**2) 在项目中加入如下Maven依赖**

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
   <modelVersion>4.0.0</modelVersion>
   <parent>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-parent</artifactId>
      <version>3.5.3</version>
      <relativePath/> <!-- lookup parent from repository -->
   </parent>
   <groupId>com.example</groupId>
   <artifactId>SpringAIRAGDemo</artifactId>
   <version>0.0.1-SNAPSHOT</version>
   <name>SpringAIRAGDemo</name>
   <description>SpringAIRAGDemo</description>


   <properties>
      <java.version>17</java.version>
   </properties>

   <!-- 导入 Spring AI BOM，用于统一管理 Spring AI 依赖的版本，
    引用每个 Spring AI 模块时不用再写 <version>，只要依赖什么模块 Mavens 自动使用 BOM 推荐的版本 -->
   <dependencyManagement>
      <dependencies>
         <dependency>
            <groupId>org.springframework.ai</groupId>
            <artifactId>spring-ai-bom</artifactId>
            <version>1.0.0-SNAPSHOT</version>
            <type>pom</type>
            <scope>import</scope>
         </dependency>
      </dependencies>
   </dependencyManagement>

   <dependencies>
      <dependency>
         <groupId>org.springframework.boot</groupId>
         <artifactId>spring-boot-starter-web</artifactId>
      </dependency>

      <!-- Deepseek AI 依赖 -->
      <dependency>
         <groupId>org.springframework.ai</groupId>
         <artifactId>spring-ai-starter-model-deepseek</artifactId>
      </dependency>

      <!-- 智普AI 依赖 -->
      <dependency>
         <groupId>org.springframework.ai</groupId>
         <artifactId>spring-ai-starter-model-zhipuai</artifactId>
      </dependency>

      <!-- QuestionAnswerAdvisor 依赖包-->
      <dependency>
         <groupId>org.springframework.ai</groupId>
         <artifactId>spring-ai-advisors-vector-store</artifactId>
      </dependency>

      <!--  RetrievalAugmentationAdvisor  依赖包-->
      <dependency>
         <groupId>org.springframework.ai</groupId>
         <artifactId>spring-ai-rag</artifactId>
      </dependency>

      <!--  Milvus VectorStore 依赖包-->
      <dependency>
         <groupId>org.springframework.ai</groupId>
         <artifactId>spring-ai-starter-vector-store-milvus</artifactId>
      </dependency>

      <!-- Tika DocumentReader 依赖包-->
      <dependency>
         <groupId>org.springframework.ai</groupId>
         <artifactId>spring-ai-tika-document-reader</artifactId>
      </dependency>


   </dependencies>

   <!-- 声明仓库， 用于获取 Spring AI 以及相关预发布版本-->
   <repositories>
      <repository>
         <id>spring-snapshots</id>
         <name>Spring Snapshots</name>
         <url>https://repo.spring.io/snapshot</url>
         <releases>
            <enabled>false</enabled>
         </releases>
      </repository>
      <repository>
         <name>Central Portal Snapshots</name>
         <id>central-portal-snapshots</id>
         <url>https://central.sonatype.com/repository/maven-snapshots/</url>
         <releases>
            <enabled>false</enabled>
         </releases>
         <snapshots>
            <enabled>true</enabled>
         </snapshots>
      </repository>
   </repositories>

</project>

```

注意：

* 在该pom.xml中引入了“spring-ai-starter-model-zhipuai”依赖包用于文档片段转换成向量存入向量库；
* 在该pom.xml中引入了“spring-ai-starter-model-deepseek”依赖包用于chatClient聊天。
* 在该pom.xml中引入了“spring-ai-advisors-vector-store”和“spring-ai-rag”依赖包，两个依赖包分别为QuestionAnswerAdvisor和RetrievalAugmentationAdvisor依赖。
* 在该pom.xml中引入了“spring-ai-starter-vector-store-milvus”依赖包，该依赖为Milvus VectorStore 依赖包。
* 在该pom.xml中引入了“spring-ai-tika-document-reader”依赖包，该依赖用于切分DOC 文档。

**3) 配置resources/application.properties**

```
spring.application.name=SpringAIRAGDemo
server.port=8080

## 使用 智普AI Embedding 模型，需要在pom.xml中引入对应依赖
spring.ai.zhipuai.api-key=d...4
spring.ai.zhipuai.base-url=https://open.bigmodel.cn/api/paas
spring.ai.zhipuai.embedding.options.model=embedding-2

## 配置 Chat Model:Deepseek的基础URL、密钥和使用模型
spring.ai.deepseek.base-url=https://api.deepseek.com
spring.ai.deepseek.api-key=sk-8...1
spring.ai.deepseek.chat.options.model=deepseek-chat

## Milvus 配置
spring.ai.vectorstore.milvus.client.host=node2
spring.ai.vectorstore.milvus.client.port=19530
# 默认用户名和密码为 root 和 Milvus
spring.ai.vectorstore.milvus.client.token=root:Milvus
# 要使用的 Milvus 数据库的名称
spring.ai.vectorstore.milvus.database-name=default
# 用于存储 vector 的 Milvus 集合名称
spring.ai.vectorstore.milvus.collection-name=guide_qa_store
#是否自动初始化 schema，如：vector_store如果没有会自动创建
spring.ai.vectorstore.milvus.initialize-schema=true
# Milvus 集合中要存储的 vector 的维度，默认为1536，要与所使用的 embedding 模型的维度匹配（如：DeepSeek、ZhiPuAI 或其他，都是1024维）
spring.ai.vectorstore.milvus.embedding-dimension=1024
```

**4) 准备知识库文档**

将“导游面试问答.doc”文档放到项目resources资源目录下。

**5) 构建两个Rag Advisor和Chat Client**

在项目中创建config/AIConfig.java ，该类标记为Configuration类。类中注入QuestionAnswerAdvisor、RetrievalAugmentationAdvisor以及ChatClient，并且每次运行SpringBoot项目时读取项目resources资源目录下的“导游面试问答.doc”内容，切分文档内容，并向Milvus中插入分片文档向量数据（如果已存在则不重复插入）。

```
package com.example.springairagdemo.config;

import io.milvus.client.MilvusServiceClient;
import io.milvus.grpc.GetCollectionStatisticsResponse;
import io.milvus.param.R;
import io.milvus.param.collection.FlushParam;
import io.milvus.param.collection.GetCollectionStatisticsParam;
import io.milvus.response.GetCollStatResponseWrapper;
import jakarta.annotation.PostConstruct;
import org.apache.tika.Tika;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.milvus.MilvusVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class AIConfig {

    // 注入 MilvusVectorStore 实例
    @Autowired
    private MilvusVectorStore vectorStore;

    // 从配置文件中读取 Milvus 集合名称
    @Value("${spring.ai.vectorstore.milvus.collection-name}")
    private String collectionName;

    //配置 QuestionAnswerAdvisor，用于问答任务
    @Bean
    public QuestionAnswerAdvisor questionAnswerAdvisor() {
        return QuestionAnswerAdvisor.builder(vectorStore)
                // 配置向量搜索的参数，这里设置了相似度阈值为 0.5，返回前 6 个结果
                .searchRequest(SearchRequest.builder().similarityThreshold(0.1d).topK(6).build())
                .build();
    }


    //配置 RetrievalAugmentationAdvisor，用于增强检索任务
    @Bean
    public RetrievalAugmentationAdvisor retrievalAugmentationAdvisor() {

        //VectorStoreDocumentRetriever 负责在向量数据库中根据查询获取最相关的文档
        //支持根据相似度阈值、top-K（返回前K个最相关文档）和过滤表达式等方式进行精确检索
        VectorStoreDocumentRetriever retriever = VectorStoreDocumentRetriever.builder()
                .vectorStore(vectorStore) // 设置向量存储对象
                .similarityThreshold(0.1)//设置相似度阈值
                .topK(6)//设置返回前K个最相关文档
                .build();

        //ContextualQueryAugmenter 用于增强查询，根据上下文信息生成更具体的查询
        //支持基于上下文的查询增强，例如根据用户输入的上下文信息生成更具体的查询，提高检索的准确性和效率
        ContextualQueryAugmenter cqa = ContextualQueryAugmenter.builder()
                .allowEmptyContext(true)//允许空上下文
                .build();

        //返回 RetrievalAugmentationAdvisor 对象，用于增强检索任务
        return RetrievalAugmentationAdvisor.builder()
                .documentRetriever(retriever) // 设置文档检索器
                .queryAugmenter(cqa) // 设置查询增强器
                .build();
    }


    //配置 ChatClient，初始化聊天客户端
    @Bean
    public ChatClient chatClient(DeepSeekChatModel chatModel,QuestionAnswerAdvisor qaAdvisor) {
        return ChatClient.builder(chatModel)
                .defaultSystem("你是一个助手，回答用户问题是不要提及回复是从上下文信息中获取的，" +
                        "不要回答你不知道的问题，如果你不知道答案，就回答：抱歉，我不清楚这个问题。")
                .build();
    }


    /**
     * @PostConstruct 注解的方法会在 Spring 容器完成依赖注入后自动调用一次，常用于执行初始化逻辑。
     */
    @PostConstruct
    public void initVectorData() {
        System.out.println("初始化向量数据，写入到 Milvus 数据库中...");

        // 获取 Milvus 客户端实例
        MilvusServiceClient client = (MilvusServiceClient)vectorStore.getNativeClient().get();

        // 获取集合统计信息，查询集合的记录数量
        R<GetCollectionStatisticsResponse> resp = client.getCollectionStatistics(GetCollectionStatisticsParam.newBuilder()
                .withCollectionName(collectionName)
                .build());

        // 获取实体数量，即数据行数
        long rowCount = new GetCollStatResponseWrapper(resp.getData()).getRowCount();

        System.out.println("Milvus vector_store 中数据数量：" + rowCount);

        //如果集合没有数据就写入
        if(rowCount==0){
            System.out.println("Milvus vector_store 中没有数据，开始写入...");
            loadAndStoreDocumentData();
        }

        // 获取 Milvus 客户端并手动调用 flush，刷新索引，确保数据被持久化到 Milvus 数据库中
        System.out.println("Milvus vector_store 刷新索引...");
        client.flush(FlushParam.newBuilder()
                .withCollectionNames(List.of(collectionName)) // 设置刷新操作的集合名称
                .build());


    }


    /**
     * 读取 Word 文件内容并存入 Milvus 向量数据库
     */
    private void loadAndStoreDocumentData() {
        List<Document> documents = new ArrayList<>();

        try {
            // 从 resources 目录加载文档
            var resource = new ClassPathResource("导游面试问答.doc");
            Tika tika = new Tika();
            String text = tika.parseToString(resource.getFile());

            // 使用 TokenTextSplitter 拆分文本
            TokenTextSplitter splitter = TokenTextSplitter.builder()
                    .withChunkSize(800)           // 每段最多 800 个 token
                    .withMinChunkSizeChars(400)   // 每段最小字符数 400
                    .withKeepSeparator(true)      // 保留分隔符
                    .build();


            // 拆分文本
            List<Document> rawDocs = List.of(new Document(text));
            List<Document> chunks = splitter.apply(rawDocs);

            // 遍历每个 chunk，生成 embedding 并存储
            for (Document chunk : chunks) {
                String chunkText = chunk.getText().strip();
                //chunk:chunk: Document{id='10d79633-7d1c-4736-9423-3877d05f6cd6', text='内容。。。', media='null', metadata={}, score=null}
                System.out.println("chunk: " + chunk);
                System.out.println("chunkText: " + chunkText);
                if (!chunkText.isBlank()) {
                    // 将文档内容存入 Milvus 向量数据库
                    documents.add(chunk);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 将文档内容存入 Milvus 向量数据库，自动写入向量数据表 guide_qa_vectors 中
        vectorStore.add(documents);
    }
}
```

**6) 创建RagController.java**

```
package com.example.springairagdemo.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ai")
public class RagController {

    @Autowired
    private ChatClient chatClient;
    @Autowired
    private QuestionAnswerAdvisor qaAdvisor;
    @Autowired
    private RetrievalAugmentationAdvisor ragAdvisor;

    @GetMapping("/chat1")
    public String ask1(@RequestParam String message) {
        return chatClient.prompt()
                .user(message)
                //设置使用 QuestionAnswerAdvisor
                .advisors(List.of(qaAdvisor))
                .call()
                .content();
    }

    @GetMapping("/chat2")
    public String ask2(@RequestParam String message) {
        return chatClient.prompt()
                .user(message)
                //设置使用 RetrievalAugmentationAdvisor
                .advisors(List.of(ragAdvisor))
                .call()
                .content();
    }

}

```

**7) 启动Milvus**

在linux node2 节点启动Milvus：

```
#进入 milvus目录
cd /software/milvus

#启动 Milvus
docker compose up -d
```

**8) 启动项目并测试**

启动SpringAIRAGDemo项目，运行主应用类SpringAiragDemoApplication.java，启动该项目。

在浏览器中输入如下内容进行测试：

```
# http://localhost:8080/ai/chat1?message=何为丹崖断墙
丹崖断墙是一条自燕山运动以来多次活动的断层带，总断距600米以上。它是由紫红色石英砂岩构成的断层面，长250米，高150米。该断墙下的一系列断阶完整地体现了造山运动的过程，展示了地质运动的伟大力量。

# http://localhost:8080/ai/chat1?message=Spring AI是什么？
抱歉，我不清楚这个问题。

#  http://localhost:8080/ai/chat2?message=何为丹崖断墙
丹崖断墙是一条自燕山运动以来多次活动的断层带，总断距600米以上。丹崖断墙是该断层最新一次活动形成的断层面，由紫红色石英砂岩构成，长250米，高150米。断墙下的一系列断阶，完整地体现了造山运动的过程，又展示了造山运动的伟大。

#http://localhost:8080/ai/chat2?message=今天天气如何？
抱歉，我不清楚这个问题。
```
