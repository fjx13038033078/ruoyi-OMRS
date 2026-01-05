package com.ruoyi.outfit.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.ruoyi.outfit.domain.WardrobeClothes;
import com.ruoyi.outfit.mapper.WardrobeClothesMapper;
import com.ruoyi.outfit.service.IWardrobeClothesService;
import lombok.RequiredArgsConstructor;

/**
 * 衣物信息Service业务层处理
 *
 * @author ruoyi
 */
@Service
@RequiredArgsConstructor
public class WardrobeClothesServiceImpl implements IWardrobeClothesService {

    private final WardrobeClothesMapper wardrobeClothesMapper;

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
}

