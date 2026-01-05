package com.ruoyi.outfit.service;

import java.util.List;
import java.util.Map;
import com.ruoyi.outfit.domain.WardrobeClothes;

/**
 * 衣物信息Service接口
 *
 * @author ruoyi
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
     * 切换收藏状态
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
}

