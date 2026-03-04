package com.ruoyi.outfit.service.impl;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.recommend.ContentBasedRecommender;
import com.ruoyi.common.utils.recommend.HybridRecommender;
import com.ruoyi.common.utils.recommend.UserBasedCollaborativeFiltering;
import com.ruoyi.common.utils.recommend.WeatherRecommender;
import com.ruoyi.outfit.mapper.WardrobeRecommendMapper;
import com.ruoyi.outfit.service.IWardrobeRecommendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 衣物推荐Service实现类
 *
 * @author fanjiaxing
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WardrobeRecommendServiceImpl implements IWardrobeRecommendService {

    private final WardrobeRecommendMapper recommendMapper;

    /** 默认推荐数量 */
    private static final int DEFAULT_RECOMMEND_COUNT = 10;

    @Override
    public Map<String, Object> recommendByWeather(Long userId, String cityName) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 获取城市名
            if (StringUtils.isEmpty(cityName)) {
                cityName = recommendMapper.selectUserLocation(userId);
            }
            if (StringUtils.isEmpty(cityName)) {
                cityName = "北京"; // 默认城市
            }

            // 获取天气信息
            WeatherRecommender weatherRecommender = new WeatherRecommender();
            WeatherRecommender.WeatherInfo weather = weatherRecommender.getWeatherByCity(cityName);

            // 获取用户需要排除的衣物
            List<Long> excludeIds = getUserExcludeIds(userId);

            // 根据天气获取推荐条件
            List<String> seasons = weatherRecommender.getRecommendedSeasons(weather);
            Map<String, Double> categories = weatherRecommender.getRecommendedCategories(weather);
            List<String> materials = weatherRecommender.getRecommendedMaterials(weather);

            // 查询符合条件的衣物
            List<Long> clothesIds = recommendMapper.selectClothesByWeather(
                    seasons,
                    new ArrayList<>(categories.keySet()),
                    materials,
                    excludeIds,
                    DEFAULT_RECOMMEND_COUNT
            );

            // 获取衣物详情
            List<Map<String, Object>> clothes = getClothesDetails(clothesIds, userId);

            result.put("success", true);
            result.put("weather", weather);
            result.put("advice", weatherRecommender.generateWeatherAdvice(weather));
            result.put("clothes", clothes);
            result.put("total", clothes.size());

        } catch (Exception e) {
            log.error("天气推荐异常", e);
            result.put("success", false);
            result.put("message", "获取天气推荐失败：" + e.getMessage());
            result.put("clothes", new ArrayList<>());
        }

        return result;
    }

    @Override
    public Map<String, Object> recommendByScene(Long userId, String sceneCode) {
        Map<String, Object> result = new HashMap<>();

        try {
            List<Long> excludeIds = getUserExcludeIds(userId);
            List<String> occasions = new ArrayList<>();

            if (StringUtils.isNotEmpty(sceneCode)) {
                occasions.add(sceneCode);
            } else {
                // 获取用户场景偏好
                List<Map<String, Object>> scenePrefs = recommendMapper.selectUserScenePreferences(userId);
                if (scenePrefs != null && !scenePrefs.isEmpty()) {
                    occasions = scenePrefs.stream()
                            .map(p -> (String) p.get("sceneCode"))
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                }
            }

            List<Long> clothesIds;
            if (occasions.isEmpty()) {
                // 没有场景偏好，返回热门衣物
                clothesIds = recommendMapper.selectPopularClothes(excludeIds, DEFAULT_RECOMMEND_COUNT);
                result.put("reason", "为您推荐热门衣物");
            } else {
                clothesIds = recommendMapper.selectClothesByOccasion(occasions, excludeIds, DEFAULT_RECOMMEND_COUNT);
                result.put("reason", "根据您的场景偏好推荐");
                result.put("scenes", occasions);
            }

            List<Map<String, Object>> clothes = getClothesDetails(clothesIds, userId);

            result.put("success", true);
            result.put("clothes", clothes);
            result.put("total", clothes.size());

        } catch (Exception e) {
            log.error("场景推荐异常", e);
            result.put("success", false);
            result.put("message", "获取场景推荐失败：" + e.getMessage());
            result.put("clothes", new ArrayList<>());
        }

        return result;
    }

    @Override
    public Map<String, Object> recommendByStyle(Long userId, String styleCode) {
        Map<String, Object> result = new HashMap<>();

        try {
            List<Long> excludeIds = getUserExcludeIds(userId);
            List<String> styles = new ArrayList<>();

            if (StringUtils.isNotEmpty(styleCode)) {
                styles.add(styleCode);
            } else {
                // 获取用户风格偏好
                List<Map<String, Object>> stylePrefs = recommendMapper.selectUserStylePreferences(userId);
                if (stylePrefs != null && !stylePrefs.isEmpty()) {
                    styles = stylePrefs.stream()
                            .map(p -> (String) p.get("styleCode"))
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                }
            }

            List<Long> clothesIds;
            if (styles.isEmpty()) {
                clothesIds = recommendMapper.selectPopularClothes(excludeIds, DEFAULT_RECOMMEND_COUNT);
                result.put("reason", "为您推荐热门衣物");
            } else {
                clothesIds = recommendMapper.selectClothesByStyle(styles, excludeIds, DEFAULT_RECOMMEND_COUNT);
                result.put("reason", "根据您的风格偏好推荐");
                result.put("styles", styles);
            }

            List<Map<String, Object>> clothes = getClothesDetails(clothesIds, userId);

            result.put("success", true);
            result.put("clothes", clothes);
            result.put("total", clothes.size());

        } catch (Exception e) {
            log.error("风格推荐异常", e);
            result.put("success", false);
            result.put("message", "获取风格推荐失败：" + e.getMessage());
            result.put("clothes", new ArrayList<>());
        }

        return result;
    }

    @Override
    public Map<String, Object> recommendBySeason(Long userId, String season) {
        Map<String, Object> result = new HashMap<>();

        try {
            List<Long> excludeIds = getUserExcludeIds(userId);
            List<String> seasons = new ArrayList<>();

            if (StringUtils.isNotEmpty(season)) {
                seasons.add(season);
            } else {
                // 根据当前月份确定季节
                seasons = getCurrentSeasons();
            }

            List<Long> clothesIds = recommendMapper.selectClothesBySeason(seasons, excludeIds, DEFAULT_RECOMMEND_COUNT);
            List<Map<String, Object>> clothes = getClothesDetails(clothesIds, userId);

            result.put("success", true);
            result.put("season", seasons.get(0));
            result.put("seasonName", getSeasonName(seasons.get(0)));
            result.put("reason", "为您推荐当季衣物");
            result.put("clothes", clothes);
            result.put("total", clothes.size());

        } catch (Exception e) {
            log.error("季节推荐异常", e);
            result.put("success", false);
            result.put("message", "获取季节推荐失败：" + e.getMessage());
            result.put("clothes", new ArrayList<>());
        }

        return result;
    }

    @Override
    public Map<String, Object> recommendByCollaborativeFiltering(Long userId) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 获取所有用户的收藏数据
            List<Map<String, Object>> allFavorites = recommendMapper.selectAllUserFavorites();
            
            if (allFavorites == null || allFavorites.isEmpty()) {
                result.put("success", true);
                result.put("reason", "暂无足够的用户行为数据，为您推荐热门衣物");
                List<Long> excludeIds = getUserExcludeIds(userId);
                List<Long> popularIds = recommendMapper.selectPopularClothes(excludeIds, DEFAULT_RECOMMEND_COUNT);
                result.put("clothes", getClothesDetails(popularIds, userId));
                result.put("total", popularIds.size());
                return result;
            }

            // 构建用户评分矩阵
            Map<Long, Map<Long, Double>> userRatings = new HashMap<>();
            Map<Long, Map<Long, Long>> userActionTimes = new HashMap<>();

            for (Map<String, Object> favorite : allFavorites) {
                Long favUserId = ((Number) favorite.get("userId")).longValue();
                Long clothesId = ((Number) favorite.get("clothesId")).longValue();
                Long actionTime = favorite.get("actionTime") != null 
                        ? ((Number) favorite.get("actionTime")).longValue() 
                        : System.currentTimeMillis();

                userRatings.computeIfAbsent(favUserId, k -> new HashMap<>()).put(clothesId, 1.0);
                userActionTimes.computeIfAbsent(favUserId, k -> new HashMap<>()).put(clothesId, actionTime);
            }

            // 如果用户没有行为数据，返回热门推荐
            if (!userRatings.containsKey(userId)) {
                result.put("success", true);
                result.put("reason", "您还没有收藏记录，为您推荐热门衣物");
                List<Long> excludeIds = getUserExcludeIds(userId);
                List<Long> popularIds = recommendMapper.selectPopularClothes(excludeIds, DEFAULT_RECOMMEND_COUNT);
                result.put("clothes", getClothesDetails(popularIds, userId));
                result.put("total", popularIds.size());
                return result;
            }

            // 执行协同过滤推荐
            UserBasedCollaborativeFiltering cf = new UserBasedCollaborativeFiltering(userRatings, userActionTimes);
            List<Long> recommendedIds = cf.recommendItems(userId, DEFAULT_RECOMMEND_COUNT);

            // 获取相似用户数量
            List<Long> similarUsers = cf.getSimilarUsers(userId, 5);

            List<Map<String, Object>> clothes = getClothesDetails(recommendedIds, userId);

            result.put("success", true);
            result.put("reason", "根据相似用户的喜好为您推荐");
            result.put("similarUserCount", similarUsers.size());
            result.put("clothes", clothes);
            result.put("total", clothes.size());

        } catch (Exception e) {
            log.error("协同过滤推荐异常", e);
            result.put("success", false);
            result.put("message", "获取协同过滤推荐失败：" + e.getMessage());
            result.put("clothes", new ArrayList<>());
        }

        return result;
    }

    @Override
    public Map<String, Object> recommendHybrid(Long userId, String cityName) {
        Map<String, Object> result = new HashMap<>();

        try {
            List<Long> excludeIds = getUserExcludeIds(userId);
            Set<Long> excludeSet = new HashSet<>(excludeIds);

            HybridRecommender hybridRecommender = new HybridRecommender();
            hybridRecommender.setExcludeIds(excludeSet);

            // 1. 天气推荐
            try {
                Map<String, Object> weatherResult = recommendByWeather(userId, cityName);
                if (Boolean.TRUE.equals(weatherResult.get("success"))) {
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> weatherClothes = (List<Map<String, Object>>) weatherResult.get("clothes");
                    if (weatherClothes != null && !weatherClothes.isEmpty()) {
                        List<Long> weatherIds = weatherClothes.stream()
                                .map(c -> ((Number) c.get("id")).longValue())
                                .collect(Collectors.toList());
                        hybridRecommender.addSourceResult(HybridRecommender.RecommendSource.WEATHER, weatherIds);
                    }
                    result.put("weatherInfo", weatherResult.get("weather"));
                    result.put("weatherAdvice", weatherResult.get("advice"));
                }
            } catch (Exception e) {
                log.warn("混合推荐-天气推荐失败", e);
            }

            // 2. 场景推荐
            try {
                Map<String, Object> sceneResult = recommendByScene(userId, null);
                if (Boolean.TRUE.equals(sceneResult.get("success"))) {
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> sceneClothes = (List<Map<String, Object>>) sceneResult.get("clothes");
                    if (sceneClothes != null && !sceneClothes.isEmpty()) {
                        List<Long> sceneIds = sceneClothes.stream()
                                .map(c -> ((Number) c.get("id")).longValue())
                                .collect(Collectors.toList());
                        hybridRecommender.addSourceResult(HybridRecommender.RecommendSource.SCENE, sceneIds);
                    }
                }
            } catch (Exception e) {
                log.warn("混合推荐-场景推荐失败", e);
            }

            // 3. 风格推荐
            try {
                Map<String, Object> styleResult = recommendByStyle(userId, null);
                if (Boolean.TRUE.equals(styleResult.get("success"))) {
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> styleClothes = (List<Map<String, Object>>) styleResult.get("clothes");
                    if (styleClothes != null && !styleClothes.isEmpty()) {
                        List<Long> styleIds = styleClothes.stream()
                                .map(c -> ((Number) c.get("id")).longValue())
                                .collect(Collectors.toList());
                        hybridRecommender.addSourceResult(HybridRecommender.RecommendSource.STYLE, styleIds);
                    }
                }
            } catch (Exception e) {
                log.warn("混合推荐-风格推荐失败", e);
            }

            // 4. 季节推荐
            try {
                Map<String, Object> seasonResult = recommendBySeason(userId, null);
                if (Boolean.TRUE.equals(seasonResult.get("success"))) {
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> seasonClothes = (List<Map<String, Object>>) seasonResult.get("clothes");
                    if (seasonClothes != null && !seasonClothes.isEmpty()) {
                        List<Long> seasonIds = seasonClothes.stream()
                                .map(c -> ((Number) c.get("id")).longValue())
                                .collect(Collectors.toList());
                        hybridRecommender.addSourceResult(HybridRecommender.RecommendSource.SEASON, seasonIds);
                    }
                    result.put("currentSeason", seasonResult.get("seasonName"));
                }
            } catch (Exception e) {
                log.warn("混合推荐-季节推荐失败", e);
            }

            // 5. 协同过滤推荐
            try {
                Map<String, Object> cfResult = recommendByCollaborativeFiltering(userId);
                if (Boolean.TRUE.equals(cfResult.get("success"))) {
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> cfClothes = (List<Map<String, Object>>) cfResult.get("clothes");
                    if (cfClothes != null && !cfClothes.isEmpty()) {
                        List<Long> cfIds = cfClothes.stream()
                                .map(c -> ((Number) c.get("id")).longValue())
                                .collect(Collectors.toList());
                        hybridRecommender.addSourceResult(HybridRecommender.RecommendSource.COLLABORATIVE, cfIds);
                    }
                }
            } catch (Exception e) {
                log.warn("混合推荐-协同过滤失败", e);
            }

            // 执行混合推荐
            List<HybridRecommender.RecommendItem> recommendItems = hybridRecommender.recommend(DEFAULT_RECOMMEND_COUNT);

            if (recommendItems.isEmpty()) {
                // 如果混合推荐没有结果，返回热门推荐
                List<Long> popularIds = recommendMapper.selectPopularClothes(excludeIds, DEFAULT_RECOMMEND_COUNT);
                List<Map<String, Object>> clothes = getClothesDetails(popularIds, userId);
                result.put("success", true);
                result.put("reason", "为您推荐热门衣物");
                result.put("clothes", clothes);
                result.put("total", clothes.size());
                return result;
            }

            // 获取推荐衣物详情
            List<Long> recommendedIds = recommendItems.stream()
                    .map(HybridRecommender.RecommendItem::getClothesId)
                    .collect(Collectors.toList());
            
            List<Map<String, Object>> clothes = getClothesDetails(recommendedIds, userId);

            // 添加推荐理由
            Map<Long, String> reasonMap = recommendItems.stream()
                    .collect(Collectors.toMap(
                            HybridRecommender.RecommendItem::getClothesId,
                            HybridRecommender.RecommendItem::getReason
                    ));

            for (Map<String, Object> item : clothes) {
                Long id = ((Number) item.get("id")).longValue();
                item.put("recommendReason", reasonMap.getOrDefault(id, "综合推荐"));
            }

            result.put("success", true);
            result.put("reason", "综合多种推荐策略为您精选");
            result.put("clothes", clothes);
            result.put("total", clothes.size());

        } catch (Exception e) {
            log.error("混合推荐异常", e);
            result.put("success", false);
            result.put("message", "获取混合推荐失败：" + e.getMessage());
            result.put("clothes", new ArrayList<>());
        }

        return result;
    }

    @Override
    public List<Map<String, Object>> getClothesDetails(List<Long> clothesIds, Long currentUserId) {
        if (clothesIds == null || clothesIds.isEmpty()) {
            return new ArrayList<>();
        }

        List<Map<String, Object>> clothes = recommendMapper.selectClothesByIds(clothesIds);
        
        // 获取当前用户的收藏列表
        List<Long> favoriteIds = recommendMapper.selectUserFavoriteClothesIds(currentUserId);
        Set<Long> favoriteSet = new HashSet<>(favoriteIds != null ? favoriteIds : new ArrayList<>());

        // 添加是否已收藏标记
        for (Map<String, Object> item : clothes) {
            Long id = ((Number) item.get("id")).longValue();
            item.put("currentUserFavorite", favoriteSet.contains(id));
        }

        return clothes;
    }

    /**
     * 获取用户需要排除的衣物ID（自己的 + 已收藏的）
     */
    private List<Long> getUserExcludeIds(Long userId) {
        List<Long> excludeIds = new ArrayList<>();
        
        List<Long> ownedIds = recommendMapper.selectUserOwnedClothesIds(userId);
        if (ownedIds != null) {
            excludeIds.addAll(ownedIds);
        }
        
        List<Long> favoriteIds = recommendMapper.selectUserFavoriteClothesIds(userId);
        if (favoriteIds != null) {
            excludeIds.addAll(favoriteIds);
        }
        
        return excludeIds;
    }

    /**
     * 获取当前季节列表
     */
    private List<String> getCurrentSeasons() {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;

        List<String> seasons = new ArrayList<>();
        if (month >= 3 && month <= 5) {
            seasons.add("spring");
        } else if (month >= 6 && month <= 8) {
            seasons.add("summer");
        } else if (month >= 9 && month <= 11) {
            seasons.add("autumn");
        } else {
            seasons.add("winter");
        }
        seasons.add("all_season");
        return seasons;
    }

    /**
     * 获取季节中文名
     */
    private String getSeasonName(String season) {
        switch (season) {
            case "spring": return "春季";
            case "summer": return "夏季";
            case "autumn": return "秋季";
            case "winter": return "冬季";
            case "all_season": return "四季通用";
            default: return season;
        }
    }
}
