package com.ruoyi.outfit.mapper;

import java.util.List;
import com.ruoyi.outfit.domain.WardrobeUserStyle;
import org.apache.ibatis.annotations.Param;

/**
 * 用户风格偏好Mapper接口
 *
 * @author fanjiaxing
 */
public interface WardrobeUserStyleMapper {
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
     * 批量新增用户风格偏好
     *
     * @param list 用户风格偏好列表
     * @return 结果
     */
    public int batchInsertWardrobeUserStyle(List<WardrobeUserStyle> list);

    /**
     * 修改用户风格偏好
     *
     * @param wardrobeUserStyle 用户风格偏好
     * @return 结果
     */
    public int updateWardrobeUserStyle(WardrobeUserStyle wardrobeUserStyle);

    /**
     * 删除用户风格偏好
     *
     * @param id 用户风格偏好主键
     * @return 结果
     */
    public int deleteWardrobeUserStyleById(Long id);

    /**
     * 批量删除用户风格偏好
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWardrobeUserStyleByIds(Long[] ids);

    /**
     * 根据用户ID删除风格偏好
     *
     * @param userId 用户ID
     * @return 结果
     */
    public int deleteWardrobeUserStyleByUserId(Long userId);
}

