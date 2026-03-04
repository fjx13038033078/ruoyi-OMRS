package com.ruoyi.web.controller.outfit;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.outfit.service.IWardrobeRecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 智能推荐Controller
 *
 * @author fanjiaxing
 */
@RestController
@RequestMapping("/outfit/recommend")
@RequiredArgsConstructor
public class WardrobeRecommendController extends BaseController {

    private final IWardrobeRecommendService recommendService;

    /**
     * 基于天气的推荐
     */
    @PreAuthorize("@ss.hasPermi('outfit:recommend:query')")
    @GetMapping("/weather")
    public AjaxResult recommendByWeather(@RequestParam(required = false) String cityName) {
        Long userId = SecurityUtils.getUserId();
        Map<String, Object> result = recommendService.recommendByWeather(userId, cityName);
        return AjaxResult.success(result);
    }

    /**
     * 基于场景的推荐
     */
    @PreAuthorize("@ss.hasPermi('outfit:recommend:query')")
    @GetMapping("/scene")
    public AjaxResult recommendByScene(@RequestParam(required = false) String sceneCode) {
        Long userId = SecurityUtils.getUserId();
        Map<String, Object> result = recommendService.recommendByScene(userId, sceneCode);
        return AjaxResult.success(result);
    }

    /**
     * 基于风格偏好的推荐
     */
    @PreAuthorize("@ss.hasPermi('outfit:recommend:query')")
    @GetMapping("/style")
    public AjaxResult recommendByStyle(@RequestParam(required = false) String styleCode) {
        Long userId = SecurityUtils.getUserId();
        Map<String, Object> result = recommendService.recommendByStyle(userId, styleCode);
        return AjaxResult.success(result);
    }

    /**
     * 基于季节规律的推荐
     */
    @PreAuthorize("@ss.hasPermi('outfit:recommend:query')")
    @GetMapping("/season")
    public AjaxResult recommendBySeason(@RequestParam(required = false) String season) {
        Long userId = SecurityUtils.getUserId();
        Map<String, Object> result = recommendService.recommendBySeason(userId, season);
        return AjaxResult.success(result);
    }

    /**
     * 协同过滤推荐
     */
    @PreAuthorize("@ss.hasPermi('outfit:recommend:query')")
    @GetMapping("/collaborative")
    public AjaxResult recommendByCollaborativeFiltering() {
        Long userId = SecurityUtils.getUserId();
        Map<String, Object> result = recommendService.recommendByCollaborativeFiltering(userId);
        return AjaxResult.success(result);
    }

    /**
     * 混合推荐
     */
    @PreAuthorize("@ss.hasPermi('outfit:recommend:query')")
    @GetMapping("/hybrid")
    public AjaxResult recommendHybrid(@RequestParam(required = false) String cityName) {
        Long userId = SecurityUtils.getUserId();
        Map<String, Object> result = recommendService.recommendHybrid(userId, cityName);
        return AjaxResult.success(result);
    }
}
