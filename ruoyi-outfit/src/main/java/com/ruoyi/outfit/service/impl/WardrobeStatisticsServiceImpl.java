package com.ruoyi.outfit.service.impl;

import com.ruoyi.outfit.mapper.WardrobeStatisticsMapper;
import com.ruoyi.outfit.service.IWardrobeStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页统计Service实现
 *
 * @author fanjiaxing
 */
@Service
@RequiredArgsConstructor
public class WardrobeStatisticsServiceImpl implements IWardrobeStatisticsService {

    private final WardrobeStatisticsMapper statisticsMapper;

    @Override
    public Map<String, Object> getHomeStatistics() {
        Map<String, Object> result = new HashMap<>();
        result.put("totalClothes", statisticsMapper.countTotalClothes());
        result.put("totalOutfits", statisticsMapper.countTotalOutfits());
        result.put("totalUsers", statisticsMapper.countTotalUsers());
        result.put("totalFavorites", statisticsMapper.countTotalFavorites());
        return result;
    }

    @Override
    public List<Map<String, Object>> getClothesCategoryStats() {
        return statisticsMapper.selectClothesCategoryStats();
    }

    @Override
    public List<Map<String, Object>> getClothesMaterialStats() {
        return statisticsMapper.selectClothesMaterialStats();
    }

    @Override
    public List<Map<String, Object>> getPopularOutfits() {
        return statisticsMapper.selectPopularOutfits();
    }

    @Override
    public List<Map<String, Object>> getClothesSeasonStats() {
        return statisticsMapper.selectClothesSeasonStats();
    }

    @Override
    public List<Map<String, Object>> getClothesColorStats() {
        return statisticsMapper.selectClothesColorStats();
    }

    @Override
    public List<Map<String, Object>> getOutfitTrend() {
        return statisticsMapper.selectOutfitTrend();
    }
}
