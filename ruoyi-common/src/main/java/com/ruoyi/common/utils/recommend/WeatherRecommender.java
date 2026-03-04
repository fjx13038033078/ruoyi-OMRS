package com.ruoyi.common.utils.recommend;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 基于天气的推荐算法
 * 根据天气温度、天气状况推荐合适的衣物
 *
 * @Author 范佳兴
 */
@Slf4j
public class WeatherRecommender {

    /** 和风天气API Key (免费版) */
    private static final String QWEATHER_KEY = "your_api_key_here";
    /** 和风天气API基础URL */
    private static final String QWEATHER_BASE_URL = "https://devapi.qweather.com/v7";

    /**
     * 天气信息实体
     */
    public static class WeatherInfo {
        private double temperature;      // 当前温度
        private double feelsLike;        // 体感温度
        private String weatherCondition; // 天气状况（晴、多云、雨、雪等）
        private int humidity;            // 湿度
        private String windScale;        // 风力等级
        private String city;             // 城市名

        public double getTemperature() { return temperature; }
        public void setTemperature(double temperature) { this.temperature = temperature; }
        public double getFeelsLike() { return feelsLike; }
        public void setFeelsLike(double feelsLike) { this.feelsLike = feelsLike; }
        public String getWeatherCondition() { return weatherCondition; }
        public void setWeatherCondition(String weatherCondition) { this.weatherCondition = weatherCondition; }
        public int getHumidity() { return humidity; }
        public void setHumidity(int humidity) { this.humidity = humidity; }
        public String getWindScale() { return windScale; }
        public void setWindScale(String windScale) { this.windScale = windScale; }
        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }
    }

    /**
     * 根据城市名获取天气信息
     * 使用模拟数据，实际项目中可接入真实天气API
     *
     * @param cityName 城市名称
     * @return 天气信息
     */
    public WeatherInfo getWeatherByCity(String cityName) {
        // 先尝试调用真实API
        WeatherInfo realWeather = fetchRealWeather(cityName);
        if (realWeather != null) {
            return realWeather;
        }

        // 如果API调用失败，使用模拟数据
        log.info("使用模拟天气数据 for city: {}", cityName);
        return generateMockWeather(cityName);
    }

    /**
     * 调用真实天气API
     */
    private WeatherInfo fetchRealWeather(String cityName) {
        try {
            // 1. 先通过城市名获取城市ID
            String geoUrl = "https://geoapi.qweather.com/v2/city/lookup?location=" 
                    + URLEncoder.encode(cityName, StandardCharsets.UTF_8.toString()) 
                    + "&key=" + QWEATHER_KEY;
            
            String geoResponse = httpGet(geoUrl);
            if (geoResponse == null) {
                return null;
            }

            JSONObject geoJson = JSON.parseObject(geoResponse);
            if (!"200".equals(geoJson.getString("code"))) {
                log.warn("获取城市ID失败: {}", geoJson);
                return null;
            }

            String locationId = geoJson.getJSONArray("location")
                    .getJSONObject(0).getString("id");

            // 2. 获取实时天气
            String weatherUrl = QWEATHER_BASE_URL + "/weather/now?location=" 
                    + locationId + "&key=" + QWEATHER_KEY;
            
            String weatherResponse = httpGet(weatherUrl);
            if (weatherResponse == null) {
                return null;
            }

            JSONObject weatherJson = JSON.parseObject(weatherResponse);
            if (!"200".equals(weatherJson.getString("code"))) {
                log.warn("获取天气信息失败: {}", weatherJson);
                return null;
            }

            JSONObject now = weatherJson.getJSONObject("now");
            WeatherInfo info = new WeatherInfo();
            info.setCity(cityName);
            info.setTemperature(now.getDoubleValue("temp"));
            info.setFeelsLike(now.getDoubleValue("feelsLike"));
            info.setWeatherCondition(now.getString("text"));
            info.setHumidity(now.getIntValue("humidity"));
            info.setWindScale(now.getString("windScale"));

            return info;
        } catch (Exception e) {
            log.error("调用天气API异常: {}", e.getMessage());
            return null;
        }
    }

    /**
     * HTTP GET请求
     */
    private String httpGet(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                return response.toString();
            }
        } catch (Exception e) {
            log.error("HTTP请求失败: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 生成模拟天气数据
     */
    private WeatherInfo generateMockWeather(String cityName) {
        WeatherInfo info = new WeatherInfo();
        info.setCity(cityName);
        
        // 根据当前月份模拟温度
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        
        Random random = new Random();
        double baseTemp;
        String condition;
        
        if (month >= 6 && month <= 8) {
            // 夏季
            baseTemp = 28 + random.nextInt(10);
            condition = random.nextBoolean() ? "晴" : "多云";
        } else if (month >= 12 || month <= 2) {
            // 冬季
            baseTemp = -5 + random.nextInt(15);
            condition = random.nextInt(3) == 0 ? "雪" : (random.nextBoolean() ? "晴" : "阴");
        } else if (month >= 3 && month <= 5) {
            // 春季
            baseTemp = 15 + random.nextInt(10);
            condition = random.nextInt(3) == 0 ? "雨" : (random.nextBoolean() ? "晴" : "多云");
        } else {
            // 秋季
            baseTemp = 10 + random.nextInt(15);
            condition = random.nextBoolean() ? "晴" : "阴";
        }
        
        info.setTemperature(baseTemp);
        info.setFeelsLike(baseTemp - 2 + random.nextInt(4));
        info.setWeatherCondition(condition);
        info.setHumidity(40 + random.nextInt(40));
        info.setWindScale(String.valueOf(1 + random.nextInt(5)));
        
        return info;
    }

    /**
     * 根据天气获取推荐的季节标签
     *
     * @param weather 天气信息
     * @return 推荐的季节列表
     */
    public List<String> getRecommendedSeasons(WeatherInfo weather) {
        List<String> seasons = new ArrayList<>();
        double temp = weather.getFeelsLike();

        if (temp >= 28) {
            seasons.add("summer");
            seasons.add("all_season");
        } else if (temp >= 20) {
            seasons.add("spring");
            seasons.add("autumn");
            seasons.add("all_season");
        } else if (temp >= 10) {
            seasons.add("spring");
            seasons.add("autumn");
            seasons.add("all_season");
        } else if (temp >= 0) {
            seasons.add("autumn");
            seasons.add("winter");
            seasons.add("all_season");
        } else {
            seasons.add("winter");
            seasons.add("all_season");
        }

        return seasons;
    }

    /**
     * 根据天气获取推荐的衣物类别
     *
     * @param weather 天气信息
     * @return 推荐的类别及权重
     */
    public Map<String, Double> getRecommendedCategories(WeatherInfo weather) {
        Map<String, Double> categories = new HashMap<>();
        double temp = weather.getFeelsLike();
        String condition = weather.getWeatherCondition();

        // 基于温度推荐
        if (temp >= 28) {
            // 炎热天气
            categories.put("tops", 1.0);      // 上衣（短袖）
            categories.put("bottoms", 0.9);   // 裤子（短裤）
            categories.put("dresses", 0.8);   // 裙子
            categories.put("shoes", 0.7);     // 凉鞋
        } else if (temp >= 20) {
            // 温暖天气
            categories.put("tops", 1.0);
            categories.put("bottoms", 0.9);
            categories.put("dresses", 0.8);
            categories.put("outerwear", 0.5); // 薄外套
            categories.put("shoes", 0.7);
        } else if (temp >= 10) {
            // 凉爽天气
            categories.put("tops", 0.9);
            categories.put("bottoms", 0.9);
            categories.put("outerwear", 1.0); // 外套重要
            categories.put("shoes", 0.7);
        } else {
            // 寒冷天气
            categories.put("outerwear", 1.0); // 厚外套最重要
            categories.put("tops", 0.8);      // 保暖内衣
            categories.put("bottoms", 0.8);
            categories.put("accessories", 0.9); // 围巾手套
            categories.put("shoes", 0.8);     // 保暖鞋
        }

        // 基于天气状况调整
        if (condition != null) {
            if (condition.contains("雨")) {
                categories.put("outerwear", categories.getOrDefault("outerwear", 0.5) + 0.3);
                categories.put("shoes", 0.9); // 防水鞋
            }
            if (condition.contains("雪")) {
                categories.put("outerwear", 1.0);
                categories.put("accessories", 1.0);
                categories.put("shoes", 1.0); // 防滑保暖鞋
            }
        }

        return categories;
    }

    /**
     * 根据天气获取推荐的材质
     *
     * @param weather 天气信息
     * @return 推荐的材质列表
     */
    public List<String> getRecommendedMaterials(WeatherInfo weather) {
        List<String> materials = new ArrayList<>();
        double temp = weather.getFeelsLike();
        int humidity = weather.getHumidity();

        if (temp >= 28) {
            materials.add("cotton");    // 棉
            materials.add("linen");     // 麻
            materials.add("chiffon");   // 雪纺
        } else if (temp >= 20) {
            materials.add("cotton");
            materials.add("polyester"); // 涤纶
            materials.add("blend");     // 混纺
        } else if (temp >= 10) {
            materials.add("cotton");
            materials.add("polyester");
            materials.add("denim");     // 牛仔
        } else {
            materials.add("wool");      // 羊毛
            materials.add("down");      // 羽绒
            materials.add("cashmere");  // 羊绒
            materials.add("fleece");    // 抓绒
        }

        // 高湿度推荐速干材质
        if (humidity > 70) {
            materials.add("polyester");
        }

        return materials;
    }

    /**
     * 生成天气推荐说明
     */
    public String generateWeatherAdvice(WeatherInfo weather) {
        StringBuilder advice = new StringBuilder();
        double temp = weather.getFeelsLike();
        String condition = weather.getWeatherCondition();

        advice.append("当前").append(weather.getCity())
              .append("天气：").append(condition)
              .append("，温度").append(String.format("%.0f", weather.getTemperature())).append("°C")
              .append("（体感").append(String.format("%.0f", temp)).append("°C）。");

        if (temp >= 28) {
            advice.append("天气炎热，建议穿着轻薄透气的衣物，如短袖、短裤、裙装等。");
        } else if (temp >= 20) {
            advice.append("气温适宜，可穿着轻便的春秋装，搭配薄外套备用。");
        } else if (temp >= 10) {
            advice.append("天气较凉，建议穿着长袖和外套，做好保暖。");
        } else if (temp >= 0) {
            advice.append("天气寒冷，建议穿着厚外套、毛衣等保暖衣物。");
        } else {
            advice.append("天气严寒，请做好全面保暖，穿羽绒服、保暖内衣等。");
        }

        if (condition != null && condition.contains("雨")) {
            advice.append("有降雨，请携带雨具并穿着防水鞋。");
        }
        if (condition != null && condition.contains("雪")) {
            advice.append("有降雪，注意防滑保暖。");
        }

        return advice.toString();
    }
}
