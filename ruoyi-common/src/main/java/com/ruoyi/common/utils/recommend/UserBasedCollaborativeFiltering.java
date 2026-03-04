package com.ruoyi.common.utils.recommend;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 基于用户的协同过滤推荐算法
 * 改进版本：支持多种相似度计算方式，增加时间衰减因子
 *
 * @Author 范佳兴
 */
@Slf4j
public class UserBasedCollaborativeFiltering {
    
    /** 相似度阈值，过滤掉相似度过低的用户 */
    private static final double SIMILARITY_THRESHOLD = 0.05;
    /** 最大推荐数量 */
    private static final int MAX_RECOMMENDATIONS = 10;
    /** 最大相似用户数量 */
    private static final int MAX_SIMILAR_USERS = 30;
    /** 时间衰减因子（每天衰减的比例） */
    private static final double TIME_DECAY_FACTOR = 0.99;

    /** 用户评分/行为数据 Map<用户ID, Map<物品ID, 评分>> */
    private Map<Long, Map<Long, Double>> userRatings;
    /** 物品-用户倒排表 Map<物品ID, List<用户ID>> */
    private Map<Long, List<Long>> itemUsers;
    /** 用户索引映射 */
    private Map<Long, Integer> userIndex;
    /** 索引-用户映射 */
    private Map<Integer, Long> indexUser;
    /** 用户相似度矩阵 */
    private double[][] similarityMatrix;
    /** 用户行为时间 Map<用户ID, Map<物品ID, 时间戳>> */
    private Map<Long, Map<Long, Long>> userActionTimes;

    /**
     * 构造函数
     *
     * @param userRatings 用户评分数据
     */
    public UserBasedCollaborativeFiltering(Map<Long, Map<Long, Double>> userRatings) {
        this(userRatings, null);
    }

    /**
     * 构造函数（带时间信息）
     *
     * @param userRatings 用户评分数据
     * @param userActionTimes 用户行为时间数据
     */
    public UserBasedCollaborativeFiltering(Map<Long, Map<Long, Double>> userRatings, 
                                           Map<Long, Map<Long, Long>> userActionTimes) {
        this.userRatings = userRatings;
        this.userActionTimes = userActionTimes;
        this.itemUsers = new HashMap<>();
        this.userIndex = new HashMap<>();
        this.indexUser = new HashMap<>();

        // 构建物品-用户倒排表和用户索引
        int keyIndex = 0;
        for (Long user : userRatings.keySet()) {
            Map<Long, Double> ratings = userRatings.get(user);
            for (Long item : ratings.keySet()) {
                itemUsers.computeIfAbsent(item, k -> new ArrayList<>()).add(user);
            }
            this.userIndex.put(user, keyIndex);
            this.indexUser.put(keyIndex, user);
            keyIndex++;
        }

        // 构建用户相似度矩阵
        buildSimilarityMatrix();
    }

    /**
     * 构建用户相似度矩阵（使用余弦相似度）
     */
    private void buildSimilarityMatrix() {
        int N = userRatings.size();
        this.similarityMatrix = new double[N][N];
        
        // 初始化为0
        for (int i = 0; i < N; i++) {
            Arrays.fill(similarityMatrix[i], 0.0);
        }

        // 计算共同评分物品数量
        int[][] commonItems = new int[N][N];
        for (Long item : itemUsers.keySet()) {
            List<Long> userList = itemUsers.get(item);
            for (Long u1 : userList) {
                for (Long u2 : userList) {
                    if (!u1.equals(u2)) {
                        int id1 = userIndex.get(u1);
                        int id2 = userIndex.get(u2);
                        commonItems[id1][id2]++;
                    }
                }
            }
        }

        // 计算余弦相似度
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                if (commonItems[i][j] > 0) {
                    Long user1 = indexUser.get(i);
                    Long user2 = indexUser.get(j);
                    double similarity = calculateCosineSimilarity(user1, user2);
                    similarityMatrix[i][j] = similarity;
                    similarityMatrix[j][i] = similarity;
                }
            }
        }
    }

    /**
     * 计算两个用户之间的余弦相似度
     */
    private double calculateCosineSimilarity(Long user1, Long user2) {
        Map<Long, Double> ratings1 = userRatings.get(user1);
        Map<Long, Double> ratings2 = userRatings.get(user2);

        if (ratings1 == null || ratings2 == null) {
            return 0.0;
        }

        // 找到共同评分的物品
        Set<Long> commonItems = new HashSet<>(ratings1.keySet());
        commonItems.retainAll(ratings2.keySet());

        if (commonItems.isEmpty()) {
            return 0.0;
        }

        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (Long item : commonItems) {
            double r1 = ratings1.get(item);
            double r2 = ratings2.get(item);
            dotProduct += r1 * r2;
        }

        for (Double rating : ratings1.values()) {
            norm1 += rating * rating;
        }
        for (Double rating : ratings2.values()) {
            norm2 += rating * rating;
        }

        if (norm1 == 0 || norm2 == 0) {
            return 0.0;
        }

        // 增加共同物品数量的惩罚因子，避免偶然相似
        double penalty = Math.min(1.0, commonItems.size() / 5.0);
        return (dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2))) * penalty;
    }

    /**
     * 计算两个用户之间的相似度
     */
    public double calculateSimilarity(Long user1, Long user2) {
        Integer id1 = userIndex.get(user1);
        Integer id2 = userIndex.get(user2);

        if (id1 == null || id2 == null) {
            return 0.0;
        }

        return similarityMatrix[id1][id2];
    }

    /**
     * 为目标用户推荐物品
     *
     * @param targetUser 目标用户ID
     * @param numRecommendations 推荐数量
     * @return 推荐的物品ID列表
     */
    public List<Long> recommendItems(Long targetUser, int numRecommendations) {
        numRecommendations = Math.min(numRecommendations, MAX_RECOMMENDATIONS);

        Map<Long, Double> targetUserRatings = userRatings.get(targetUser);
        if (targetUserRatings == null || targetUserRatings.isEmpty()) {
            log.debug("目标用户 {} 没有评分数据，返回热门物品推荐", targetUser);
            return getPopularItems(numRecommendations);
        }

        // 获取相似用户
        Map<Long, Double> userSimilarities = new HashMap<>();
        for (Long user : userRatings.keySet()) {
            if (!user.equals(targetUser)) {
                double similarity = calculateSimilarity(targetUser, user);
                if (similarity > SIMILARITY_THRESHOLD) {
                    userSimilarities.put(user, similarity);
                }
            }
        }

        if (userSimilarities.isEmpty()) {
            log.debug("没有找到与用户 {} 相似度超过阈值的用户，返回热门物品推荐", targetUser);
            return getPopularItems(numRecommendations);
        }

        // 排序并限制相似用户数量
        List<Long> similarUsers = userSimilarities.entrySet()
                .stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(MAX_SIMILAR_USERS)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // 计算推荐分数
        Map<Long, Double> recommendations = new HashMap<>();
        Map<Long, Double> similaritySum = new HashMap<>();

        for (Long user : similarUsers) {
            Map<Long, Double> ratings = userRatings.get(user);
            if (ratings == null) continue;

            double userSimilarity = userSimilarities.get(user);
            for (Map.Entry<Long, Double> entry : ratings.entrySet()) {
                Long item = entry.getKey();
                Double rating = entry.getValue();

                if (!targetUserRatings.containsKey(item)) {
                    // 应用时间衰减
                    double timeWeight = getTimeDecayWeight(user, item);
                    double weightedRating = rating * userSimilarity * timeWeight;
                    
                    recommendations.merge(item, weightedRating, Double::sum);
                    similaritySum.merge(item, userSimilarity, Double::sum);
                }
            }
        }

        // 归一化推荐分数
        for (Long item : recommendations.keySet()) {
            double sim = similaritySum.getOrDefault(item, 1.0);
            if (sim > 0) {
                recommendations.put(item, recommendations.get(item) / sim);
            }
        }

        if (recommendations.isEmpty()) {
            log.debug("没有找到可推荐的物品给用户 {}", targetUser);
            return getPopularItems(numRecommendations);
        }

        return recommendations.entrySet()
                .stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(numRecommendations)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * 计算时间衰减权重
     */
    private double getTimeDecayWeight(Long userId, Long itemId) {
        if (userActionTimes == null) {
            return 1.0;
        }
        
        Map<Long, Long> actionTimes = userActionTimes.get(userId);
        if (actionTimes == null || !actionTimes.containsKey(itemId)) {
            return 1.0;
        }

        long actionTime = actionTimes.get(itemId);
        long currentTime = System.currentTimeMillis();
        long daysDiff = (currentTime - actionTime) / (1000 * 60 * 60 * 24);
        
        return Math.pow(TIME_DECAY_FACTOR, daysDiff);
    }

    /**
     * 获取热门物品（用于冷启动）
     */
    private List<Long> getPopularItems(int count) {
        Map<Long, Integer> itemPopularity = new HashMap<>();
        for (List<Long> users : itemUsers.values()) {
            for (Long user : users) {
                // 统计每个物品被多少用户评分
            }
        }
        
        for (Long item : itemUsers.keySet()) {
            itemPopularity.put(item, itemUsers.get(item).size());
        }

        return itemPopularity.entrySet()
                .stream()
                .sorted(Map.Entry.<Long, Integer>comparingByValue().reversed())
                .limit(count)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * 默认推荐10个物品
     */
    public List<Long> recommendItems(Long targetUser) {
        return recommendItems(targetUser, MAX_RECOMMENDATIONS);
    }

    /**
     * 获取与目标用户最相似的N个用户
     */
    public List<Long> getSimilarUsers(Long targetUser, int count) {
        Map<Long, Double> userSimilarities = new HashMap<>();
        for (Long user : userRatings.keySet()) {
            if (!user.equals(targetUser)) {
                double similarity = calculateSimilarity(targetUser, user);
                if (similarity > 0) {
                    userSimilarities.put(user, similarity);
                }
            }
        }

        return userSimilarities.entrySet()
                .stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(count)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
