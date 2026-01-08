package com.ruoyi.outfit.mapper;

import java.util.List;
import java.util.Map;

/**
 * 首页统计Mapper接口
 *
 * @author fanjiaxing
 */
public interface WardrobeStatisticsMapper {

    /**
     * 统计总衣物数
     */
    int countTotalClothes();

    /**
     * 统计总穿搭数
     */
    int countTotalOutfits();

    /**
     * 统计总用户数
     */
    int countTotalUsers();

    /**
     * 统计总收藏数
     */
    int countTotalFavorites();

    /**
     * 衣物分类统计
     */
    List<Map<String, Object>> selectClothesCategoryStats();

    /**
     * 衣物材质统计
     */
    List<Map<String, Object>> selectClothesMaterialStats();

    /**
     * 热门穿搭排行
     */
    List<Map<String, Object>> selectPopularOutfits();

    /**
     * 季节分布统计
     */
    List<Map<String, Object>> selectClothesSeasonStats();

    /**
     * 颜色分布统计
     */
    List<Map<String, Object>> selectClothesColorStats();

    /**
     * 最近穿搭趋势（按日期统计）
     */
    List<Map<String, Object>> selectOutfitTrend();
}
