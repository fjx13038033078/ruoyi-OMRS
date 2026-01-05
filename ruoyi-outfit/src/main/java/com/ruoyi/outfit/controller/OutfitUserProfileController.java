package com.ruoyi.outfit.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.outfit.domain.WardrobeUserScene;
import com.ruoyi.outfit.domain.WardrobeUserStyle;
import com.ruoyi.outfit.service.IWardrobeUserSceneService;
import com.ruoyi.outfit.service.IWardrobeUserStyleService;
import com.ruoyi.system.service.ISysUserService;

/**
 * 用户画像管理Controller
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/outfit/profile")
public class OutfitUserProfileController extends BaseController {
    @Autowired
    private ISysUserService userService;

    @Autowired
    private IWardrobeUserStyleService userStyleService;

    @Autowired
    private IWardrobeUserSceneService userSceneService;

    /**
     * 获取当前用户完整画像信息
     */
    @GetMapping
    public AjaxResult getProfile() {
        LoginUser loginUser = getLoginUser();
        SysUser user = userService.selectUserById(loginUser.getUserId());
        
        Map<String, Object> profile = new HashMap<>();
        profile.put("user", user);
        profile.put("styles", userStyleService.selectWardrobeUserStyleByUserId(user.getUserId()));
        profile.put("scenes", userSceneService.selectWardrobeUserSceneByUserId(user.getUserId()));
        
        return AjaxResult.success(profile);
    }

    /**
     * 更新用户身体数据
     */
    @Log(title = "用户画像", businessType = BusinessType.UPDATE)
    @PutMapping("/bodyInfo")
    public AjaxResult updateBodyInfo(@RequestBody SysUser user) {
        LoginUser loginUser = getLoginUser();
        SysUser currentUser = loginUser.getUser();
        
        // 只更新身体相关数据
        currentUser.setHeight(user.getHeight());
        currentUser.setWeight(user.getWeight());
        currentUser.setBust(user.getBust());
        currentUser.setWaist(user.getWaist());
        currentUser.setHip(user.getHip());
        currentUser.setTopSize(user.getTopSize());
        currentUser.setBottomSize(user.getBottomSize());
        currentUser.setShoeSize(user.getShoeSize());
        currentUser.setSkinTone(user.getSkinTone());
        currentUser.setBodyType(user.getBodyType());
        currentUser.setLocation(user.getLocation());
        
        if (userService.updateUserProfile(currentUser) > 0) {
            return success();
        }
        return error("更新身体数据失败");
    }

    /**
     * 获取用户风格偏好
     */
    @GetMapping("/styles")
    public AjaxResult getStyles() {
        LoginUser loginUser = getLoginUser();
        List<WardrobeUserStyle> styles = userStyleService.selectWardrobeUserStyleByUserId(loginUser.getUserId());
        return AjaxResult.success(styles);
    }

    /**
     * 保存用户风格偏好
     */
    @Log(title = "用户画像-风格偏好", businessType = BusinessType.UPDATE)
    @PostMapping("/styles")
    public AjaxResult saveStyles(@RequestBody List<WardrobeUserStyle> styles) {
        LoginUser loginUser = getLoginUser();
        userStyleService.saveUserStyles(loginUser.getUserId(), styles);
        return success();
    }

    /**
     * 获取用户场景偏好
     */
    @GetMapping("/scenes")
    public AjaxResult getScenes() {
        LoginUser loginUser = getLoginUser();
        List<WardrobeUserScene> scenes = userSceneService.selectWardrobeUserSceneByUserId(loginUser.getUserId());
        return AjaxResult.success(scenes);
    }

    /**
     * 保存用户场景偏好
     */
    @Log(title = "用户画像-场景偏好", businessType = BusinessType.UPDATE)
    @PostMapping("/scenes")
    public AjaxResult saveScenes(@RequestBody List<WardrobeUserScene> scenes) {
        LoginUser loginUser = getLoginUser();
        userSceneService.saveUserScenes(loginUser.getUserId(), scenes);
        return success();
    }
}

