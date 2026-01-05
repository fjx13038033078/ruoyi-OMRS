package com.ruoyi.outfit.service;

import java.util.List;
import com.ruoyi.outfit.domain.WardrobeUserScene;

/**
 * 用户场景偏好Service接口
 *
 * @author fanjiaxing
 */
public interface IWardrobeUserSceneService {
    /**
     * 查询用户场景偏好
     *
     * @param id 用户场景偏好主键
     * @return 用户场景偏好
     */
    public WardrobeUserScene selectWardrobeUserSceneById(Long id);

    /**
     * 查询用户场景偏好列表
     *
     * @param wardrobeUserScene 用户场景偏好
     * @return 用户场景偏好集合
     */
    public List<WardrobeUserScene> selectWardrobeUserSceneList(WardrobeUserScene wardrobeUserScene);

    /**
     * 根据用户ID查询场景偏好列表
     *
     * @param userId 用户ID
     * @return 用户场景偏好集合
     */
    public List<WardrobeUserScene> selectWardrobeUserSceneByUserId(Long userId);

    /**
     * 新增用户场景偏好
     *
     * @param wardrobeUserScene 用户场景偏好
     * @return 结果
     */
    public int insertWardrobeUserScene(WardrobeUserScene wardrobeUserScene);

    /**
     * 批量保存用户场景偏好（先删后增）
     *
     * @param userId 用户ID
     * @param sceneList 场景偏好列表
     * @return 结果
     */
    public int saveUserScenes(Long userId, List<WardrobeUserScene> sceneList);

    /**
     * 修改用户场景偏好
     *
     * @param wardrobeUserScene 用户场景偏好
     * @return 结果
     */
    public int updateWardrobeUserScene(WardrobeUserScene wardrobeUserScene);

    /**
     * 批量删除用户场景偏好
     *
     * @param ids 需要删除的用户场景偏好主键集合
     * @return 结果
     */
    public int deleteWardrobeUserSceneByIds(Long[] ids);

    /**
     * 删除用户场景偏好信息
     *
     * @param id 用户场景偏好主键
     * @return 结果
     */
    public int deleteWardrobeUserSceneById(Long id);

    /**
     * 根据用户ID删除场景偏好
     *
     * @param userId 用户ID
     * @return 结果
     */
    public int deleteWardrobeUserSceneByUserId(Long userId);
}

