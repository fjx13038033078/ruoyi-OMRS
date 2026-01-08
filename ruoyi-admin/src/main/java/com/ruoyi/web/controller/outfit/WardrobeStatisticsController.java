package com.ruoyi.web.controller.outfit;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.outfit.service.IWardrobeStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页统计Controller
 *
 * @author fanjiaxing
 */
@RestController
@RequestMapping("/outfit/statistics")
@RequiredArgsConstructor
public class WardrobeStatisticsController extends BaseController {

    private final IWardrobeStatisticsService statisticsService;

    /**
     * 获取首页统计数据
     */
    @GetMapping("/home")
    public AjaxResult getHomeStatistics() {
        return success(statisticsService.getHomeStatistics());
    }

    /**
     * 获取衣物分类统计
     */
    @GetMapping("/clothes/category")
    public AjaxResult getClothesCategoryStats() {
        return success(statisticsService.getClothesCategoryStats());
    }

    /**
     * 获取衣物材质统计
     */
    @GetMapping("/clothes/material")
    public AjaxResult getClothesMaterialStats() {
        return success(statisticsService.getClothesMaterialStats());
    }

    /**
     * 获取热门穿搭排行
     */
    @GetMapping("/outfit/popular")
    public AjaxResult getPopularOutfits() {
        return success(statisticsService.getPopularOutfits());
    }

    /**
     * 获取季节分布统计
     */
    @GetMapping("/clothes/season")
    public AjaxResult getClothesSeasonStats() {
        return success(statisticsService.getClothesSeasonStats());
    }

    /**
     * 获取颜色分布统计
     */
    @GetMapping("/clothes/color")
    public AjaxResult getClothesColorStats() {
        return success(statisticsService.getClothesColorStats());
    }

    /**
     * 获取最近穿搭趋势
     */
    @GetMapping("/outfit/trend")
    public AjaxResult getOutfitTrend() {
        return success(statisticsService.getOutfitTrend());
    }
}
