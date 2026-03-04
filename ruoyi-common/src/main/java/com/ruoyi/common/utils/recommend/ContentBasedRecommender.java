package com.ruoyi.common.utils.recommend;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 基于内容的推荐算法
 * 支持风格、场景、季节等多维度推荐
 *
 * @Author 范佳兴
 */
@Slf4j
public class ContentBasedRecommender {

    /** 最大推荐数量 */
    private static final int MAX_RECOMMENDATIONS = 10;

    /**
     * 衣物特征
     */
    public static class ClothesFeature {
        private Long clothesId;
        private String category;      // 类别
        private String subCategory;   // 子类别
        private String season;        // 季节
        private String color;         // 颜色
        private String material;      // 材质
        private String style;         // 风格
        private String occasion;      // 场合
        private Integer favoriteCount; // 收藏数
        private Integer wearCount;    // 穿着次数
        private Long userId;          // 发布者ID

        // Getters and Setters
        public Long getClothesId() { return clothesId; }
        public void setClothesId(Long clothesId) { this.clothesId = clothesId; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public String getSubCategory() { return subCategory; }
        public void setSubCategory(String subCategory) { this.subCategory = subCategory; }
        public String getSeason() { return season; }
        public void setSeason(String season) { this.season = season; }
        public String getColor() { return color; }
        public void setColor(String color) { this.color = color; }
        public String getMaterial() { return material; }
        public void setMaterial(String material) { this.material = material; }
        public String getStyle() { return style; }
        public void setStyle(String style) { this.style = style; }
        public String getOccasion() { return occasion; }
        public void setOccasion(String occasion) { this.occasion = occasion; }
        public Integer getFavoriteCount() { return favoriteCount; }
        public void setFavoriteCount(Integer favoriteCount) { this.favoriteCount = favoriteCount; }
        public Integer getWearCount() { return wearCount; }
        public void setWearCount(Integer wearCount) { this.wearCount = wearCount; }
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
    }

    /**
     * 用户偏好
     */
    public static class UserPreference {
        private Long userId;
        private List<String> preferredStyles;     // 偏好风格
        private List<String> preferredScenes;     // 偏好场景
        private Map<String, Integer> styleWeights;  // 风格权重
        private Map<String, Integer> sceneWeights;  // 场景权重

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public List<String> getPreferredStyles() { return preferredStyles; }
        public void setPreferredStyles(List<String> preferredStyles) { this.preferredStyles = preferredStyles; }
        public List<String> getPreferredScenes() { return preferredScenes; }
        public void setPreferredScenes(List<String> preferredScenes) { this.preferredScenes = preferredScenes; }
        public Map<String, Integer> getStyleWeights() { return styleWeights; }
        public void setStyleWeights(Map<String, Integer> styleWeights) { this.styleWeights = styleWeights; }
        public Map<String, Integer> getSceneWeights() { return sceneWeights; }
        public void setSceneWeights(Map<String, Integer> sceneWeights) { this.sceneWeights = sceneWeights; }
    }

    /** 所有衣物特征 */
    private List<ClothesFeature> allClothes;
    /** 用户偏好 */
    private UserPreference userPreference;
    /** 用户已有衣物ID */
    private Set<Long> userOwnedClothes;
    /** 用户已收藏衣物ID */
    private Set<Long> userFavoriteClothes;

    public ContentBasedRecommender(List<ClothesFeature> allClothes) {
        this.allClothes = allClothes;
        this.userOwnedClothes = new HashSet<>();
        this.userFavoriteClothes = new HashSet<>();
    }

    public void setUserPreference(UserPreference preference) {
        this.userPreference = preference;
    }

    public void setUserOwnedClothes(Set<Long> ownedClothes) {
        this.userOwnedClothes = ownedClothes != null ? ownedClothes : new HashSet<>();
    }

    public void setUserFavoriteClothes(Set<Long> favoriteClothes) {
        this.userFavoriteClothes = favoriteClothes != null ? favoriteClothes : new HashSet<>();
    }

    /**
     * 基于风格偏好推荐
     *
     * @param count 推荐数量
     * @return 推荐的衣物ID列表
     */
    public List<Long> recommendByStyle(int count) {
        count = Math.min(count, MAX_RECOMMENDATIONS);
        
        if (userPreference == null || userPreference.getPreferredStyles() == null 
                || userPreference.getPreferredStyles().isEmpty()) {
            log.debug("用户没有风格偏好设置，返回热门衣物");
            return getPopularClothes(count);
        }

        Map<String, Integer> styleWeights = userPreference.getStyleWeights();
        if (styleWeights == null) {
            styleWeights = new HashMap<>();
            for (String style : userPreference.getPreferredStyles()) {
                styleWeights.put(style, 1);
            }
        }

        Map<Long, Double> scores = new HashMap<>();
        final Map<String, Integer> finalStyleWeights = styleWeights;

        for (ClothesFeature clothes : allClothes) {
            // 排除用户自己的衣物和已收藏的
            if (userOwnedClothes.contains(clothes.getClothesId()) 
                    || userFavoriteClothes.contains(clothes.getClothesId())) {
                continue;
            }

            double score = 0.0;
            String clothesStyle = clothes.getStyle();
            
            if (clothesStyle != null) {
                // 支持多风格匹配
                String[] styles = clothesStyle.split(",");
                for (String style : styles) {
                    style = style.trim();
                    if (finalStyleWeights.containsKey(style)) {
                        score += finalStyleWeights.get(style);
                    }
                }
            }

            // 加入热度因子
            if (score > 0) {
                score += (clothes.getFavoriteCount() != null ? clothes.getFavoriteCount() : 0) * 0.1;
                score += (clothes.getWearCount() != null ? clothes.getWearCount() : 0) * 0.05;
                scores.put(clothes.getClothesId(), score);
            }
        }

        return scores.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(count)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * 基于场景偏好推荐
     *
     * @param count 推荐数量
     * @return 推荐的衣物ID列表
     */
    public List<Long> recommendByScene(int count) {
        count = Math.min(count, MAX_RECOMMENDATIONS);

        if (userPreference == null || userPreference.getPreferredScenes() == null 
                || userPreference.getPreferredScenes().isEmpty()) {
            log.debug("用户没有场景偏好设置，返回热门衣物");
            return getPopularClothes(count);
        }

        Map<String, Integer> sceneWeights = userPreference.getSceneWeights();
        if (sceneWeights == null) {
            sceneWeights = new HashMap<>();
            for (String scene : userPreference.getPreferredScenes()) {
                sceneWeights.put(scene, 1);
            }
        }

        Map<Long, Double> scores = new HashMap<>();
        final Map<String, Integer> finalSceneWeights = sceneWeights;

        for (ClothesFeature clothes : allClothes) {
            if (userOwnedClothes.contains(clothes.getClothesId()) 
                    || userFavoriteClothes.contains(clothes.getClothesId())) {
                continue;
            }

            double score = 0.0;
            String clothesOccasion = clothes.getOccasion();
            
            if (clothesOccasion != null) {
                String[] occasions = clothesOccasion.split(",");
                for (String occasion : occasions) {
                    occasion = occasion.trim();
                    if (finalSceneWeights.containsKey(occasion)) {
                        score += finalSceneWeights.get(occasion);
                    }
                }
            }

            if (score > 0) {
                score += (clothes.getFavoriteCount() != null ? clothes.getFavoriteCount() : 0) * 0.1;
                scores.put(clothes.getClothesId(), score);
            }
        }

        return scores.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(count)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * 基于季节推荐
     *
     * @param seasons 目标季节列表
     * @param count 推荐数量
     * @return 推荐的衣物ID列表
     */
    public List<Long> recommendBySeason(List<String> seasons, int count) {
        count = Math.min(count, MAX_RECOMMENDATIONS);

        if (seasons == null || seasons.isEmpty()) {
            // 根据当前月份确定季节
            seasons = getCurrentSeasons();
        }

        Map<Long, Double> scores = new HashMap<>();
        final List<String> targetSeasons = seasons;

        for (ClothesFeature clothes : allClothes) {
            if (userOwnedClothes.contains(clothes.getClothesId()) 
                    || userFavoriteClothes.contains(clothes.getClothesId())) {
                continue;
            }

            double score = 0.0;
            String clothesSeason = clothes.getSeason();
            
            if (clothesSeason != null) {
                if ("all_season".equals(clothesSeason)) {
                    score = 0.8; // 四季通用得分稍低
                } else {
                    String[] seasonArr = clothesSeason.split(",");
                    for (String s : seasonArr) {
                        if (targetSeasons.contains(s.trim())) {
                            score = 1.0;
                            break;
                        }
                    }
                }
            }

            if (score > 0) {
                // 加入热度因子
                score += (clothes.getFavoriteCount() != null ? clothes.getFavoriteCount() : 0) * 0.05;
                score += (clothes.getWearCount() != null ? clothes.getWearCount() : 0) * 0.03;
                scores.put(clothes.getClothesId(), score);
            }
        }

        return scores.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(count)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * 获取当前季节
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
     * 获取热门衣物
     */
    private List<Long> getPopularClothes(int count) {
        return allClothes.stream()
                .filter(c -> !userOwnedClothes.contains(c.getClothesId()) 
                        && !userFavoriteClothes.contains(c.getClothesId()))
                .sorted((a, b) -> {
                    int scoreA = (a.getFavoriteCount() != null ? a.getFavoriteCount() : 0) 
                            + (a.getWearCount() != null ? a.getWearCount() : 0);
                    int scoreB = (b.getFavoriteCount() != null ? b.getFavoriteCount() : 0) 
                            + (b.getWearCount() != null ? b.getWearCount() : 0);
                    return scoreB - scoreA;
                })
                .limit(count)
                .map(ClothesFeature::getClothesId)
                .collect(Collectors.toList());
    }

    /**
     * 计算两件衣物的相似度
     */
    public double calculateSimilarity(ClothesFeature c1, ClothesFeature c2) {
        double score = 0.0;
        int factors = 0;

        // 类别相似度
        if (c1.getCategory() != null && c1.getCategory().equals(c2.getCategory())) {
            score += 1.0;
        }
        factors++;

        // 子类别相似度
        if (c1.getSubCategory() != null && c1.getSubCategory().equals(c2.getSubCategory())) {
            score += 1.0;
        }
        factors++;

        // 风格相似度
        if (c1.getStyle() != null && c2.getStyle() != null) {
            Set<String> styles1 = new HashSet<>(Arrays.asList(c1.getStyle().split(",")));
            Set<String> styles2 = new HashSet<>(Arrays.asList(c2.getStyle().split(",")));
            Set<String> intersection = new HashSet<>(styles1);
            intersection.retainAll(styles2);
            if (!intersection.isEmpty()) {
                score += (double) intersection.size() / Math.max(styles1.size(), styles2.size());
            }
        }
        factors++;

        // 颜色相似度
        if (c1.getColor() != null && c1.getColor().equals(c2.getColor())) {
            score += 0.5;
        }
        factors++;

        // 材质相似度
        if (c1.getMaterial() != null && c1.getMaterial().equals(c2.getMaterial())) {
            score += 0.5;
        }
        factors++;

        // 季节相似度
        if (c1.getSeason() != null && c2.getSeason() != null) {
            Set<String> seasons1 = new HashSet<>(Arrays.asList(c1.getSeason().split(",")));
            Set<String> seasons2 = new HashSet<>(Arrays.asList(c2.getSeason().split(",")));
            Set<String> intersection = new HashSet<>(seasons1);
            intersection.retainAll(seasons2);
            if (!intersection.isEmpty()) {
                score += 0.5;
            }
        }
        factors++;

        return score / factors;
    }
}
