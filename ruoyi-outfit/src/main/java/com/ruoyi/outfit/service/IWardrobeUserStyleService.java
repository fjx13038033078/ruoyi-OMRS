package com.ruoyi.outfit.service;

import java.util.List;
import com.ruoyi.outfit.domain.WardrobeUserStyle;

/**
 * 用户风格偏好Service接口
 *
 * @author ruoyi
 */
public interface IWardrobeUserStyleService {
    /**
     * 查询用户风格偏好
     *
     * @param id 用户风格偏好主键
     * @return 用户风格偏好
     */
    public WardrobeUserStyle selectWardrobeUserStyleById(Long id);

    /**
     * 查询用户风格偏好列表
     *
     * @param wardrobeUserStyle 用户风格偏好
     * @return 用户风格偏好集合
     */
    public List<WardrobeUserStyle> selectWardrobeUserStyleList(WardrobeUserStyle wardrobeUserStyle);

    /**
     * 根据用户ID查询风格偏好列表
     *
     * @param userId 用户ID
     * @return 用户风格偏好集合
     */
    public List<WardrobeUserStyle> selectWardrobeUserStyleByUserId(Long userId);

    /**
     * 新增用户风格偏好
     *
     * @param wardrobeUserStyle 用户风格偏好
     * @return 结果
     */
    public int insertWardrobeUserStyle(WardrobeUserStyle wardrobeUserStyle);

    /**
     * 批量保存用户风格偏好（先删后增）
     *
     * @param userId 用户ID
     * @param styleList 风格偏好列表
     * @return 结果
     */
    public int saveUserStyles(Long userId, List<WardrobeUserStyle> styleList);

    /**
     * 修改用户风格偏好
     *
     * @param wardrobeUserStyle 用户风格偏好
     * @return 结果
     */
    public int updateWardrobeUserStyle(WardrobeUserStyle wardrobeUserStyle);

    /**
     * 批量删除用户风格偏好
     *
     * @param ids 需要删除的用户风格偏好主键集合
     * @return 结果
     */
    public int deleteWardrobeUserStyleByIds(Long[] ids);

    /**
     * 删除用户风格偏好信息
     *
     * @param id 用户风格偏好主键
     * @return 结果
     */
    public int deleteWardrobeUserStyleById(Long id);

    /**
     * 根据用户ID删除风格偏好
     *
     * @param userId 用户ID
     * @return 结果
     */
    public int deleteWardrobeUserStyleByUserId(Long userId);
}

