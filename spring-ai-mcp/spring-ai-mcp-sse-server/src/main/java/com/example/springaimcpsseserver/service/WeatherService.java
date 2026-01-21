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

@Service
public class WeatherService {
    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather";

    @Value("${openWeather.apiKey}")
    private String openWeatherApiKey;

    @Tool(description = "获得指定城市的当前天气情况，格式化后的天气报告字符")
    public String getWeather(@ToolParam(description = "城市名称，必须是英文格式，比如 London 或 Beijing") String city) {
        logger.info("====== 调用了getWeather工具 ======");
        try {
            String charset = "UTF-8";

            String query = String.format(
                    "q=%s&appid=%s&units=metric&lang=zh_cn",
                    URLEncoder.encode(city, charset),
                    URLEncoder.encode(openWeatherApiKey, charset)
            );
            URL url = new URL(BASE_URL + "?" + query);
            logger.info("===== 访问URL： ====={}", url);
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
            logger.info("data:{}", data);

            if (data.getInt("cod") == 404) {
                return "未查询到该城市的天气信息。";
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
                            城市：%s
                            天气描述：%s
                            当前温度：%s
                            体感温度：%s
                            最低温度：%s
                            最高温度：%s
                            气压：%s
                            湿度：%d%%
                            风速：%.1f m/s
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
            logger.error("====== 调用getWeather工具异常 ======", e);
            return "获取天气信息时出错：" + e.getMessage();
        }
    }

}
