package com.example.springaitoolcalling.tools;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 工具类
 */
@Component
public class MyTools {
    public static Logger logger = LogManager.getLogger(MyTools.class);

    @Tool(name = "getCurrentTime", description = "获取当前系统的时间")
    public String getCurrentTime() {
        String result = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        logger.info("调用getCurrentTime工具,结果:{}", result);
        return result;
    }

    @Tool(description = "对两个数字进行加(add)、减(subtract)、乘(multiply)、除(divide)运算")
    public double calculate(
            @ToolParam(description = "第一个数字") double a,
            @ToolParam(description = "第二个数字") double b,
            @ToolParam(description = "第三个数字") String operation
    ) {
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
                break;
            default:
                result = 0;
                break;
        }
        logger.info("调用calculate工具,第一个参数:{}，第二个参数：{}，第三个参数：{}，结果：{}", a, b, operation, result);
        return result;
    }


    @Tool(description = "查询指定省份的油价，直接返回完整JSON字符串")
    public String getOilPriceJson(@ToolParam(description = "省份名称") String province) {
        logger.info("调用getOilPriceJson工具，省份：{}", province);
        //https://shanhe.kim/api/youjia/youjia.php?province=%E5%8C%97%E4%BA%AC
        String url = String.format("https://shanhe.kim/api/youjia/youjia.php?province=%s", province);
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);
        logger.info("调用getOilPriceJson工具，省份：{}，结果：{}", province, result);
        return result;
    }
}
