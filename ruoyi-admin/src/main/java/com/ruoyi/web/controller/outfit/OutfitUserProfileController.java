package com.ruoyi.web.controller.outfit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.outfit.domain.WardrobeUserScene;
import com.ruoyi.outfit.domain.WardrobeUserStyle;
import com.ruoyi.outfit.service.IWardrobeUserSceneService;
import com.ruoyi.outfit.service.IWardrobeUserStyleService;
import com.ruoyi.system.service.ISysUserService;
import lombok.RequiredArgsConstructor;

/**
 * 用户画像管理Controller
 *
 * @author fanjiaxing
 */
@RestController
@RequestMapping("/outfit/profile")
@RequiredArgsConstructor
public class OutfitUserProfileController extends BaseController {

    private final ISysUserService userService;

    private final IWardrobeUserStyleService userStyleService;

    private final IWardrobeUserSceneService userSceneService;

    private final TokenService tokenService;

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

    /**
     * 提交用户问卷（初始化用户画像）
     */
    @Log(title = "用户画像-问卷提交", businessType = BusinessType.INSERT)
    @PostMapping("/questionnaire")
    public AjaxResult submitQuestionnaire(@RequestBody Map<String, Object> data) {
        LoginUser loginUser = getLoginUser();
        Long userId = loginUser.getUserId();
        SysUser currentUser = loginUser.getUser();
        
        // 1. 更新身体数据
        @SuppressWarnings("unchecked")
        Map<String, Object> bodyInfo = (Map<String, Object>) data.get("bodyInfo");
        if (bodyInfo != null) {
            if (bodyInfo.get("height") != null) {
                currentUser.setHeight(new java.math.BigDecimal(bodyInfo.get("height").toString()));
            }
            if (bodyInfo.get("weight") != null) {
                currentUser.setWeight(new java.math.BigDecimal(bodyInfo.get("weight").toString()));
            }
            if (bodyInfo.get("bodyType") != null) {
                currentUser.setBodyType(bodyInfo.get("bodyType").toString());
            }
            if (bodyInfo.get("skinTone") != null) {
                currentUser.setSkinTone(bodyInfo.get("skinTone").toString());
            }
            if (bodyInfo.get("location") != null) {
                currentUser.setLocation(bodyInfo.get("location").toString());
            }
        }
        
        // 2. 更新问卷完成状态
        currentUser.setQuestionnaireCompleted("1");
        userService.updateUserProfile(currentUser);
        
        // 3. 保存风格偏好
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> stylesData = (List<Map<String, Object>>) data.get("styles");
        if (stylesData != null && !stylesData.isEmpty()) {
            List<WardrobeUserStyle> styles = new java.util.ArrayList<>();
            for (Map<String, Object> item : stylesData) {
                WardrobeUserStyle style = new WardrobeUserStyle();
                style.setStyleCode(item.get("styleCode").toString());
                style.setStyleName(item.get("styleName").toString());
                style.setPreferenceLevel(item.get("preferenceLevel") != null ? 
                    Integer.parseInt(item.get("preferenceLevel").toString()) : 1);
                styles.add(style);
            }
            userStyleService.saveUserStyles(userId, styles);
        }
        
        // 4. 保存场景偏好
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> scenesData = (List<Map<String, Object>>) data.get("scenes");
        if (scenesData != null && !scenesData.isEmpty()) {
            List<WardrobeUserScene> scenes = new java.util.ArrayList<>();
            for (Map<String, Object> item : scenesData) {
                WardrobeUserScene scene = new WardrobeUserScene();
                scene.setSceneCode(item.get("sceneCode").toString());
                scene.setSceneName(item.get("sceneName").toString());
                scene.setFrequency(item.get("frequencyLevel") != null ? 
                    Integer.parseInt(item.get("frequencyLevel").toString()) : 1);
                scenes.add(scene);
            }
            userSceneService.saveUserScenes(userId, scenes);
        }
        
        // 5. 更新缓存中的用户信息（确保刷新页面后不会再次弹出问卷）
        tokenService.setLoginUser(loginUser);
        
        return success("个人画像保存成功");
    }
}

