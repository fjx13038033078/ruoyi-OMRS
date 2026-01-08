package com.ruoyi.outfit.mapper;

import java.util.List;
import com.ruoyi.outfit.domain.WardrobeClothes;
import org.apache.ibatis.annotations.Param;

/**
 * 衣物信息Mapper接口
 *
 * @author fanjiaxing
 */
public interface WardrobeClothesMapper {
    
    /**
     * 查询衣物信息
     *
     * @param id 衣物ID
     * @return 衣物信息
     */
    public WardrobeClothes selectWardrobeClothesById(Long id);

    /**
     * 查询衣物信息列表
     *
     * @param wardrobeClothes 衣物信息
     * @return 衣物信息集合
     */
    public List<WardrobeClothes> selectWardrobeClothesList(WardrobeClothes wardrobeClothes);

    /**
     * 新增衣物信息
     *
     * @param wardrobeClothes 衣物信息
     * @return 结果
     */
    public int insertWardrobeClothes(WardrobeClothes wardrobeClothes);

    /**
     * 修改衣物信息
     *
     * @param wardrobeClothes 衣物信息
     * @return 结果
     */
    public int updateWardrobeClothes(WardrobeClothes wardrobeClothes);

    /**
     * 删除衣物信息
     *
     * @param id 衣物ID
     * @return 结果
     */
    public int deleteWardrobeClothesById(Long id);

    /**
     * 批量删除衣物信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteWardrobeClothesByIds(Long[] ids);

    /**
     * 更新穿着记录
     *
     * @param id 衣物ID
     * @return 结果
     */
    public int updateWearRecord(Long id);

    /**
     * 切换收藏状态
     *
     * @param wardrobeClothes 衣物信息
     * @return 结果
     */
    public int updateFavorite(WardrobeClothes wardrobeClothes);

    /**
     * 统计用户衣物数量
     *
     * @param userId 用户ID
     * @return 衣物数量
     */
    public int countByUserId(Long userId);

    /**
     * 按类别统计衣物数量
     *
     * @param userId 用户ID
     * @return 统计结果
     */
    public List<java.util.Map<String, Object>> countByCategory(Long userId);

    /**
     * 按季节统计衣物数量
     *
     * @param userId 用户ID
     * @return 统计结果
     */
    public List<java.util.Map<String, Object>> countBySeason(Long userId);

    /**
     * 按颜色统计衣物数量
     *
     * @param userId 用户ID
     * @return 统计结果
     */
    public List<java.util.Map<String, Object>> countByColor(Long userId);

    /**
     * 查询公开的衣物列表
     *
     * @param wardrobeClothes 查询条件
     * @return 衣物信息集合
     */
    public List<WardrobeClothes> selectPublicClothesList(WardrobeClothes wardrobeClothes);

    /**
     * 查询用户收藏的衣物列表
     *
     * @param currentUserId 当前用户ID
     * @param clothes 查询条件
     * @return 衣物信息集合
     */
    public List<WardrobeClothes> selectFavoriteClothesList(@Param("currentUserId") Long currentUserId, @Param("clothes") WardrobeClothes clothes);

    /**
     * 更新收藏数量
     *
     * @param id 衣物ID
     * @param favoriteCount 收藏数量
     * @return 结果
     */
    public int updateFavoriteCount(@Param("id") Long id, @Param("favoriteCount") Integer favoriteCount);
}

