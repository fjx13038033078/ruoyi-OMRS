package com.ruoyi.outfit.service;

import java.util.List;
import java.util.Map;
import com.ruoyi.outfit.domain.WardrobeClothes;

/**
 * 衣物信息Service接口
 *
 * @author fanjiaxing
 */
public interface IWardrobeClothesService {
    
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
     * 批量删除衣物信息
     *
     * @param ids 需要删除的衣物ID
     * @return 结果
     */
    public int deleteWardrobeClothesByIds(Long[] ids);

    /**
     * 删除衣物信息
     *
     * @param id 衣物ID
     * @return 结果
     */
    public int deleteWardrobeClothesById(Long id);

    /**
     * 更新穿着记录
     *
     * @param id 衣物ID
     * @return 结果
     */
    public int updateWearRecord(Long id);

    /**
     * 切换收藏状态（发布者自己的收藏标记）
     *
     * @param id 衣物ID
     * @param isFavorite 收藏状态
     * @return 结果
     */
    public int toggleFavorite(Long id, String isFavorite);

    /**
     * 获取衣柜统计信息
     *
     * @param userId 用户ID
     * @return 统计信息
     */
    public Map<String, Object> getStatistics(Long userId);

    /**
     * 查询公开的衣物列表（所有用户可见）
     *
     * @param wardrobeClothes 查询条件
     * @return 衣物信息集合
     */
    public List<WardrobeClothes> selectPublicClothesList(WardrobeClothes wardrobeClothes);

    /**
     * 查询用户收藏的衣物列表
     *
     * @param currentUserId 当前用户ID
     * @param wardrobeClothes 查询条件
     * @return 衣物信息集合
     */
    public List<WardrobeClothes> selectFavoriteClothesList(Long currentUserId, WardrobeClothes wardrobeClothes);

    /**
     * 添加收藏（用户收藏其他人的衣物）
     *
     * @param userId 用户ID
     * @param clothesId 衣物ID
     * @return 结果
     */
    public int addFavorite(Long userId, Long clothesId);

    /**
     * 取消收藏
     *
     * @param userId 用户ID
     * @param clothesId 衣物ID
     * @return 结果
     */
    public int removeFavorite(Long userId, Long clothesId);

    /**
     * 检查用户是否已收藏某衣物
     *
     * @param userId 用户ID
     * @param clothesId 衣物ID
     * @return 是否已收藏
     */
    public boolean checkFavorite(Long userId, Long clothesId);

    /**
     * 填充当前用户的收藏状态
     *
     * @param clothesList 衣物列表
     * @param currentUserId 当前用户ID
     */
    public void fillCurrentUserFavorite(List<WardrobeClothes> clothesList, Long currentUserId);
}

