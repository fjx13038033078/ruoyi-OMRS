package com.ruoyi.outfit.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.outfit.mapper.WardrobeUserSceneMapper;
import com.ruoyi.outfit.domain.WardrobeUserScene;
import com.ruoyi.outfit.service.IWardrobeUserSceneService;

/**
 * 用户场景偏好Service业务层处理
 *
 * @author fanjiaxing
 */
@Service
public class WardrobeUserSceneServiceImpl implements IWardrobeUserSceneService {
    @Autowired
    private WardrobeUserSceneMapper wardrobeUserSceneMapper;

    /**
     * 查询用户场景偏好
     *
     * @param id 用户场景偏好主键
     * @return 用户场景偏好
     */
    @Override
    public WardrobeUserScene selectWardrobeUserSceneById(Long id) {
        return wardrobeUserSceneMapper.selectWardrobeUserSceneById(id);
    }

    /**
     * 查询用户场景偏好列表
     *
     * @param wardrobeUserScene 用户场景偏好
     * @return 用户场景偏好
     */
    @Override
    public List<WardrobeUserScene> selectWardrobeUserSceneList(WardrobeUserScene wardrobeUserScene) {
        return wardrobeUserSceneMapper.selectWardrobeUserSceneList(wardrobeUserScene);
    }

    /**
     * 根据用户ID查询场景偏好列表
     *
     * @param userId 用户ID
     * @return 用户场景偏好集合
     */
    @Override
    public List<WardrobeUserScene> selectWardrobeUserSceneByUserId(Long userId) {
        return wardrobeUserSceneMapper.selectWardrobeUserSceneByUserId(userId);
    }

    /**
     * 新增用户场景偏好
     *
     * @param wardrobeUserScene 用户场景偏好
     * @return 结果
     */
    @Override
    public int insertWardrobeUserScene(WardrobeUserScene wardrobeUserScene) {
        return wardrobeUserSceneMapper.insertWardrobeUserScene(wardrobeUserScene);
    }

    /**
     * 批量保存用户场景偏好（先删后增）
     *
     * @param userId 用户ID
     * @param sceneList 场景偏好列表
     * @return 结果
     */
    @Override
    @Transactional
    public int saveUserScenes(Long userId, List<WardrobeUserScene> sceneList) {
        // 先删除用户原有的场景偏好
        wardrobeUserSceneMapper.deleteWardrobeUserSceneByUserId(userId);
        
        if (sceneList != null && !sceneList.isEmpty()) {
            // 设置用户ID
            for (WardrobeUserScene scene : sceneList) {
                scene.setUserId(userId);
            }
            // 批量插入新的场景偏好
            return wardrobeUserSceneMapper.batchInsertWardrobeUserScene(sceneList);
        }
        return 0;
    }

    /**
     * 修改用户场景偏好
     *
     * @param wardrobeUserScene 用户场景偏好
     * @return 结果
     */
    @Override
    public int updateWardrobeUserScene(WardrobeUserScene wardrobeUserScene) {
        return wardrobeUserSceneMapper.updateWardrobeUserScene(wardrobeUserScene);
    }

    /**
     * 批量删除用户场景偏好
     *
     * @param ids 需要删除的用户场景偏好主键
     * @return 结果
     */
    @Override
    public int deleteWardrobeUserSceneByIds(Long[] ids) {
        return wardrobeUserSceneMapper.deleteWardrobeUserSceneByIds(ids);
    }

    /**
     * 删除用户场景偏好信息
     *
     * @param id 用户场景偏好主键
     * @return 结果
     */
    @Override
    public int deleteWardrobeUserSceneById(Long id) {
        return wardrobeUserSceneMapper.deleteWardrobeUserSceneById(id);
    }

    /**
     * 根据用户ID删除场景偏好
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public int deleteWardrobeUserSceneByUserId(Long userId) {
        return wardrobeUserSceneMapper.deleteWardrobeUserSceneByUserId(userId);
    }
}

