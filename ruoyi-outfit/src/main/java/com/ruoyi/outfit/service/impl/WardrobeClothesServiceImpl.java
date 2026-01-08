package com.ruoyi.outfit.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.outfit.domain.WardrobeClothes;
import com.ruoyi.outfit.domain.WardrobeClothesFavorite;
import com.ruoyi.outfit.mapper.WardrobeClothesMapper;
import com.ruoyi.outfit.mapper.WardrobeClothesFavoriteMapper;
import com.ruoyi.outfit.service.IWardrobeClothesService;
import lombok.RequiredArgsConstructor;

/**
 * 衣物信息Service业务层处理
 *
 * @author fanjiaxing
 */
@Service
@RequiredArgsConstructor
public class WardrobeClothesServiceImpl implements IWardrobeClothesService {

    private final WardrobeClothesMapper wardrobeClothesMapper;
    private final WardrobeClothesFavoriteMapper wardrobeClothesFavoriteMapper;

    /**
     * 查询衣物信息
     *
     * @param id 衣物ID
     * @return 衣物信息
     */
    @Override
    public WardrobeClothes selectWardrobeClothesById(Long id) {
        return wardrobeClothesMapper.selectWardrobeClothesById(id);
    }

    /**
     * 查询衣物信息列表
     *
     * @param wardrobeClothes 衣物信息
     * @return 衣物信息
     */
    @Override
    public List<WardrobeClothes> selectWardrobeClothesList(WardrobeClothes wardrobeClothes) {
        return wardrobeClothesMapper.selectWardrobeClothesList(wardrobeClothes);
    }

    /**
     * 新增衣物信息
     *
     * @param wardrobeClothes 衣物信息
     * @return 结果
     */
    @Override
    public int insertWardrobeClothes(WardrobeClothes wardrobeClothes) {
        return wardrobeClothesMapper.insertWardrobeClothes(wardrobeClothes);
    }

    /**
     * 修改衣物信息
     *
     * @param wardrobeClothes 衣物信息
     * @return 结果
     */
    @Override
    public int updateWardrobeClothes(WardrobeClothes wardrobeClothes) {
        return wardrobeClothesMapper.updateWardrobeClothes(wardrobeClothes);
    }

    /**
     * 批量删除衣物信息
     *
     * @param ids 需要删除的衣物ID
     * @return 结果
     */
    @Override
    public int deleteWardrobeClothesByIds(Long[] ids) {
        return wardrobeClothesMapper.deleteWardrobeClothesByIds(ids);
    }

    /**
     * 删除衣物信息
     *
     * @param id 衣物ID
     * @return 结果
     */
    @Override
    public int deleteWardrobeClothesById(Long id) {
        return wardrobeClothesMapper.deleteWardrobeClothesById(id);
    }

    /**
     * 更新穿着记录
     *
     * @param id 衣物ID
     * @return 结果
     */
    @Override
    public int updateWearRecord(Long id) {
        return wardrobeClothesMapper.updateWearRecord(id);
    }

    /**
     * 切换收藏状态
     *
     * @param id 衣物ID
     * @param isFavorite 收藏状态
     * @return 结果
     */
    @Override
    public int toggleFavorite(Long id, String isFavorite) {
        WardrobeClothes clothes = new WardrobeClothes();
        clothes.setId(id);
        clothes.setIsFavorite(isFavorite);
        return wardrobeClothesMapper.updateFavorite(clothes);
    }

    /**
     * 获取衣柜统计信息
     *
     * @param userId 用户ID
     * @return 统计信息
     */
    @Override
    public Map<String, Object> getStatistics(Long userId) {
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("total", wardrobeClothesMapper.countByUserId(userId));
        statistics.put("categoryStats", wardrobeClothesMapper.countByCategory(userId));
        statistics.put("seasonStats", wardrobeClothesMapper.countBySeason(userId));
        statistics.put("colorStats", wardrobeClothesMapper.countByColor(userId));
        return statistics;
    }

    /**
     * 查询公开的衣物列表
     *
     * @param wardrobeClothes 查询条件
     * @return 衣物信息集合
     */
    @Override
    public List<WardrobeClothes> selectPublicClothesList(WardrobeClothes wardrobeClothes) {
        return wardrobeClothesMapper.selectPublicClothesList(wardrobeClothes);
    }

    /**
     * 查询用户收藏的衣物列表
     *
     * @param currentUserId 当前用户ID
     * @param wardrobeClothes 查询条件
     * @return 衣物信息集合
     */
    @Override
    public List<WardrobeClothes> selectFavoriteClothesList(Long currentUserId, WardrobeClothes wardrobeClothes) {
        return wardrobeClothesMapper.selectFavoriteClothesList(currentUserId, wardrobeClothes);
    }

    /**
     * 添加收藏
     *
     * @param userId 用户ID
     * @param clothesId 衣物ID
     * @return 结果
     */
    @Override
    @Transactional
    public int addFavorite(Long userId, Long clothesId) {
        // 检查是否已收藏
        if (checkFavorite(userId, clothesId)) {
            return 0;
        }
        // 添加收藏记录
        WardrobeClothesFavorite favorite = new WardrobeClothesFavorite();
        favorite.setUserId(userId);
        favorite.setClothesId(clothesId);
        int result = wardrobeClothesFavoriteMapper.insert(favorite);
        // 更新衣物收藏数量
        if (result > 0) {
            int count = wardrobeClothesFavoriteMapper.countByClothesId(clothesId);
            wardrobeClothesMapper.updateFavoriteCount(clothesId, count);
        }
        return result;
    }

    /**
     * 取消收藏
     *
     * @param userId 用户ID
     * @param clothesId 衣物ID
     * @return 结果
     */
    @Override
    @Transactional
    public int removeFavorite(Long userId, Long clothesId) {
        int result = wardrobeClothesFavoriteMapper.deleteByUserAndClothes(userId, clothesId);
        // 更新衣物收藏数量
        if (result > 0) {
            int count = wardrobeClothesFavoriteMapper.countByClothesId(clothesId);
            wardrobeClothesMapper.updateFavoriteCount(clothesId, count);
        }
        return result;
    }

    /**
     * 检查用户是否已收藏某衣物
     *
     * @param userId 用户ID
     * @param clothesId 衣物ID
     * @return 是否已收藏
     */
    @Override
    public boolean checkFavorite(Long userId, Long clothesId) {
        return wardrobeClothesFavoriteMapper.selectByUserAndClothes(userId, clothesId) != null;
    }

    /**
     * 填充当前用户的收藏状态
     *
     * @param clothesList 衣物列表
     * @param currentUserId 当前用户ID
     */
    @Override
    public void fillCurrentUserFavorite(List<WardrobeClothes> clothesList, Long currentUserId) {
        if (clothesList == null || clothesList.isEmpty() || currentUserId == null) {
            return;
        }
        // 获取用户收藏的所有衣物ID
        List<Long> favoriteIds = wardrobeClothesFavoriteMapper.selectFavoriteClothesIds(currentUserId);
        Set<Long> favoriteSet = new HashSet<>(favoriteIds);
        // 填充收藏状态
        for (WardrobeClothes clothes : clothesList) {
            clothes.setCurrentUserFavorite(favoriteSet.contains(clothes.getId()));
        }
    }
}

