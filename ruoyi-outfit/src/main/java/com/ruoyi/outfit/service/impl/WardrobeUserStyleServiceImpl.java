package com.ruoyi.outfit.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.outfit.mapper.WardrobeUserStyleMapper;
import com.ruoyi.outfit.domain.WardrobeUserStyle;
import com.ruoyi.outfit.service.IWardrobeUserStyleService;

/**
 * 用户风格偏好Service业务层处理
 *
 * @author fanjiaxing
 */
@Service
public class WardrobeUserStyleServiceImpl implements IWardrobeUserStyleService {
    @Autowired
    private WardrobeUserStyleMapper wardrobeUserStyleMapper;

    /**
     * 查询用户风格偏好
     *
     * @param id 用户风格偏好主键
     * @return 用户风格偏好
     */
    @Override
    public WardrobeUserStyle selectWardrobeUserStyleById(Long id) {
        return wardrobeUserStyleMapper.selectWardrobeUserStyleById(id);
    }

    /**
     * 查询用户风格偏好列表
     *
     * @param wardrobeUserStyle 用户风格偏好
     * @return 用户风格偏好
     */
    @Override
    public List<WardrobeUserStyle> selectWardrobeUserStyleList(WardrobeUserStyle wardrobeUserStyle) {
        return wardrobeUserStyleMapper.selectWardrobeUserStyleList(wardrobeUserStyle);
    }

    /**
     * 根据用户ID查询风格偏好列表
     *
     * @param userId 用户ID
     * @return 用户风格偏好集合
     */
    @Override
    public List<WardrobeUserStyle> selectWardrobeUserStyleByUserId(Long userId) {
        return wardrobeUserStyleMapper.selectWardrobeUserStyleByUserId(userId);
    }

    /**
     * 新增用户风格偏好
     *
     * @param wardrobeUserStyle 用户风格偏好
     * @return 结果
     */
    @Override
    public int insertWardrobeUserStyle(WardrobeUserStyle wardrobeUserStyle) {
        return wardrobeUserStyleMapper.insertWardrobeUserStyle(wardrobeUserStyle);
    }

    /**
     * 批量保存用户风格偏好（先删后增）
     *
     * @param userId 用户ID
     * @param styleList 风格偏好列表
     * @return 结果
     */
    @Override
    @Transactional
    public int saveUserStyles(Long userId, List<WardrobeUserStyle> styleList) {
        // 先删除用户原有的风格偏好
        wardrobeUserStyleMapper.deleteWardrobeUserStyleByUserId(userId);
        
        if (styleList != null && !styleList.isEmpty()) {
            // 设置用户ID
            for (WardrobeUserStyle style : styleList) {
                style.setUserId(userId);
            }
            // 批量插入新的风格偏好
            return wardrobeUserStyleMapper.batchInsertWardrobeUserStyle(styleList);
        }
        return 0;
    }

    /**
     * 修改用户风格偏好
     *
     * @param wardrobeUserStyle 用户风格偏好
     * @return 结果
     */
    @Override
    public int updateWardrobeUserStyle(WardrobeUserStyle wardrobeUserStyle) {
        return wardrobeUserStyleMapper.updateWardrobeUserStyle(wardrobeUserStyle);
    }

    /**
     * 批量删除用户风格偏好
     *
     * @param ids 需要删除的用户风格偏好主键
     * @return 结果
     */
    @Override
    public int deleteWardrobeUserStyleByIds(Long[] ids) {
        return wardrobeUserStyleMapper.deleteWardrobeUserStyleByIds(ids);
    }

    /**
     * 删除用户风格偏好信息
     *
     * @param id 用户风格偏好主键
     * @return 结果
     */
    @Override
    public int deleteWardrobeUserStyleById(Long id) {
        return wardrobeUserStyleMapper.deleteWardrobeUserStyleById(id);
    }

    /**
     * 根据用户ID删除风格偏好
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public int deleteWardrobeUserStyleByUserId(Long userId) {
        return wardrobeUserStyleMapper.deleteWardrobeUserStyleByUserId(userId);
    }
}

