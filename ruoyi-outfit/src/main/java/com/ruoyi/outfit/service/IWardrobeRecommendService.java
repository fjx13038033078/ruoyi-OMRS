package com.ruoyi.outfit.service;

import java.util.List;
import java.util.Map;

/**
 * 衣物推荐Service接口
 *
 * @author fanjiaxing
 */
public interface IWardrobeRecommendService {

    /**
     * 基于天气的推荐
     *
     * @param userId 用户ID
     * @param cityName 城市名称（可选，为空则使用用户设置的位置）
     * @return 推荐结果
     */
    Map<String, Object> recommendByWeather(Long userId, String cityName);

    /**
     * 基于场景的推荐
     *
     * @param userId 用户ID
     * @param sceneCode 指定场景编码（可选）
     * @return 推荐结果
     */
    Map<String, Object> recommendByScene(Long userId, String sceneCode);

    /**
     * 基于风格偏好的推荐
     *
     * @param userId 用户ID
     * @param styleCode 指定风格编码（可选）
     * @return 推荐结果
     */
    Map<String, Object> recommendByStyle(Long userId, String styleCode);

    /**
     * 基于季节规律的推荐
     *
     * @param userId 用户ID
     * @param season 指定季节（可选，为空则使用当前季节）
     * @return 推荐结果
     */
    Map<String, Object> recommendBySeason(Long userId, String season);

    /**
     * 协同过滤推荐
     *
     * @param userId 用户ID
     * @return 推荐结果
     */
    Map<String, Object> recommendByCollaborativeFiltering(Long userId);

    /**
     * 混合推荐
     *
     * @param userId 用户ID
     * @param cityName 城市名称（可选）
     * @return 推荐结果
     */
    Map<String, Object> recommendHybrid(Long userId, String cityName);

    /**
     * 根据衣物ID列表获取衣物详情
     *
     * @param clothesIds 衣物ID列表
     * @param currentUserId 当前用户ID（用于判断是否已收藏）
     * @return 衣物详情列表
     */
    List<Map<String, Object>> getClothesDetails(List<Long> clothesIds, Long currentUserId);
}
