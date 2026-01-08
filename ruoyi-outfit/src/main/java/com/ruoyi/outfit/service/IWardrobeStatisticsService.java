package com.ruoyi.outfit.service;

import java.util.List;
import java.util.Map;

/**
 * 首页统计Service接口
 *
 * @author fanjiaxing
 */
public interface IWardrobeStatisticsService {

    /**
     * 获取首页统计数据
     */
    Map<String, Object> getHomeStatistics();

    /**
     * 获取衣物分类统计
     */
    List<Map<String, Object>> getClothesCategoryStats();

    /**
     * 获取衣物材质统计
     */
    List<Map<String, Object>> getClothesMaterialStats();

    /**
     * 获取热门穿搭排行
     */
    List<Map<String, Object>> getPopularOutfits();

    /**
     * 获取季节分布统计
     */
    List<Map<String, Object>> getClothesSeasonStats();

    /**
     * 获取颜色分布统计
     */
    List<Map<String, Object>> getClothesColorStats();

    /**
     * 获取最近穿搭趋势
     */
    List<Map<String, Object>> getOutfitTrend();
}
