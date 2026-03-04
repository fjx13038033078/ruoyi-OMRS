package com.ruoyi.common.utils.recommend;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 混合推荐算法
 * 融合多种推荐方法，提高推荐准确率与满意度
 *
 * @Author 范佳兴
 */
@Slf4j
public class HybridRecommender {

    /** 最大推荐数量 */
    private static final int MAX_RECOMMENDATIONS = 10;

    /**
     * 推荐来源枚举
     */
    public enum RecommendSource {
        WEATHER("天气推荐", 0.20),
        SCENE("场景推荐", 0.20),
        STYLE("风格推荐", 0.20),
        SEASON("季节推荐", 0.15),
        COLLABORATIVE("协同过滤", 0.25);

        private final String name;
        private final double defaultWeight;

        RecommendSource(String name, double defaultWeight) {
            this.name = name;
            this.defaultWeight = defaultWeight;
        }

        public String getName() { return name; }
        public double getDefaultWeight() { return defaultWeight; }
    }

    /**
     * 推荐结果项
     */
    public static class RecommendItem {
        private Long clothesId;
        private double score;
        private Set<RecommendSource> sources;
        private String reason;

        public RecommendItem(Long clothesId) {
            this.clothesId = clothesId;
            this.score = 0.0;
            this.sources = new HashSet<>();
        }

        public Long getClothesId() { return clothesId; }
        public double getScore() { return score; }
        public void setScore(double score) { this.score = score; }
        public void addScore(double score) { this.score += score; }
        public Set<RecommendSource> getSources() { return sources; }
        public void addSource(RecommendSource source) { this.sources.add(source); }
        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
    }

    /** 各推荐方法的权重 */
    private Map<RecommendSource, Double> weights;

    /** 各推荐方法的结果 */
    private Map<RecommendSource, List<Long>> sourceResults;

    /** 用户已有/已收藏的衣物ID（需要排除） */
    private Set<Long> excludeIds;

    public HybridRecommender() {
        this.weights = new HashMap<>();
        this.sourceResults = new HashMap<>();
        this.excludeIds = new HashSet<>();
        
        // 初始化默认权重
        for (RecommendSource source : RecommendSource.values()) {
            weights.put(source, source.getDefaultWeight());
        }
    }

    /**
     * 设置推荐方法权重
     */
    public void setWeight(RecommendSource source, double weight) {
        if (weight >= 0 && weight <= 1) {
            weights.put(source, weight);
        }
    }

    /**
     * 设置需要排除的衣物ID
     */
    public void setExcludeIds(Set<Long> excludeIds) {
        this.excludeIds = excludeIds != null ? excludeIds : new HashSet<>();
    }

    /**
     * 添加推荐来源的结果
     *
     * @param source 推荐来源
     * @param clothesIds 推荐的衣物ID列表
     */
    public void addSourceResult(RecommendSource source, List<Long> clothesIds) {
        if (clothesIds != null && !clothesIds.isEmpty()) {
            sourceResults.put(source, clothesIds);
        }
    }

    /**
     * 执行混合推荐
     *
     * @param count 推荐数量
     * @return 推荐结果列表
     */
    public List<RecommendItem> recommend(int count) {
        count = Math.min(count, MAX_RECOMMENDATIONS);

        if (sourceResults.isEmpty()) {
            log.warn("没有任何推荐来源数据");
            return new ArrayList<>();
        }

        // 归一化权重
        normalizeWeights();

        // 合并各来源的推荐结果
        Map<Long, RecommendItem> mergedResults = new HashMap<>();

        for (Map.Entry<RecommendSource, List<Long>> entry : sourceResults.entrySet()) {
            RecommendSource source = entry.getKey();
            List<Long> clothesIds = entry.getValue();
            double weight = weights.getOrDefault(source, 0.0);

            if (weight <= 0 || clothesIds == null) {
                continue;
            }

            // 计算该来源下每个衣物的分数（排名越靠前分数越高）
            int rank = 0;
            int total = clothesIds.size();
            for (Long clothesId : clothesIds) {
                // 排除用户已有的衣物
                if (excludeIds.contains(clothesId)) {
                    continue;
                }

                // 基于排名的分数计算：排名靠前的分数更高
                double rankScore = (double) (total - rank) / total;
                double weightedScore = rankScore * weight;

                RecommendItem item = mergedResults.computeIfAbsent(clothesId, RecommendItem::new);
                item.addScore(weightedScore);
                item.addSource(source);

                rank++;
            }
        }

        // 对于出现在多个推荐来源中的物品，增加额外的多样性加成
        for (RecommendItem item : mergedResults.values()) {
            if (item.getSources().size() > 1) {
                // 多来源加成：每多一个来源加10%的分数
                double diversityBonus = 1.0 + (item.getSources().size() - 1) * 0.1;
                item.setScore(item.getScore() * diversityBonus);
            }
            // 生成推荐理由
            item.setReason(generateReason(item.getSources()));
        }

        // 排序并返回Top N
        return mergedResults.values().stream()
                .sorted((a, b) -> Double.compare(b.getScore(), a.getScore()))
                .limit(count)
                .collect(Collectors.toList());
    }

    /**
     * 归一化权重，使所有权重之和为1
     */
    private void normalizeWeights() {
        // 只计算有结果的来源的权重
        double sum = 0.0;
        for (RecommendSource source : sourceResults.keySet()) {
            sum += weights.getOrDefault(source, 0.0);
        }

        if (sum > 0 && sum != 1.0) {
            for (RecommendSource source : sourceResults.keySet()) {
                double oldWeight = weights.getOrDefault(source, 0.0);
                weights.put(source, oldWeight / sum);
            }
        }
    }

    /**
     * 生成推荐理由
     */
    private String generateReason(Set<RecommendSource> sources) {
        if (sources.isEmpty()) {
            return "综合推荐";
        }

        List<String> reasons = new ArrayList<>();
        for (RecommendSource source : sources) {
            switch (source) {
                case WEATHER:
                    reasons.add("适合当前天气");
                    break;
                case SCENE:
                    reasons.add("符合您的场景偏好");
                    break;
                case STYLE:
                    reasons.add("契合您的风格喜好");
                    break;
                case SEASON:
                    reasons.add("当季推荐");
                    break;
                case COLLABORATIVE:
                    reasons.add("相似用户都在穿");
                    break;
            }
        }

        return String.join("、", reasons);
    }

    /**
     * 获取推荐结果的衣物ID列表
     */
    public List<Long> getRecommendedIds(int count) {
        return recommend(count).stream()
                .map(RecommendItem::getClothesId)
                .collect(Collectors.toList());
    }

    /**
     * 清除推荐数据
     */
    public void clear() {
        sourceResults.clear();
    }

    /**
     * 动态调整权重 - 根据用户反馈调整
     * 如果用户频繁点击某一类推荐，则增加该类推荐的权重
     *
     * @param source 推荐来源
     * @param feedback 反馈类型（正向/负向）
     */
    public void adjustWeight(RecommendSource source, boolean feedback) {
        double currentWeight = weights.getOrDefault(source, source.getDefaultWeight());
        double adjustment = feedback ? 0.05 : -0.05;
        double newWeight = Math.max(0.05, Math.min(0.5, currentWeight + adjustment));
        weights.put(source, newWeight);
        
        log.debug("调整推荐权重 {}: {} -> {}", source.getName(), currentWeight, newWeight);
    }

    /**
     * 重置权重为默认值
     */
    public void resetWeights() {
        for (RecommendSource source : RecommendSource.values()) {
            weights.put(source, source.getDefaultWeight());
        }
    }
}
