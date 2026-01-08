package com.ruoyi.outfit.service.impl;

import com.ruoyi.outfit.domain.WardrobeOutfit;
import com.ruoyi.outfit.domain.WardrobeOutfitClothes;
import com.ruoyi.outfit.domain.WardrobeOutfitFavorite;
import com.ruoyi.outfit.mapper.WardrobeOutfitClothesMapper;
import com.ruoyi.outfit.mapper.WardrobeOutfitFavoriteMapper;
import com.ruoyi.outfit.mapper.WardrobeOutfitMapper;
import com.ruoyi.outfit.service.IWardrobeOutfitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 穿搭方案Service业务层处理
 *
 * @author fanjiaxing
 */
@Service
@RequiredArgsConstructor
public class WardrobeOutfitServiceImpl implements IWardrobeOutfitService {

    private final WardrobeOutfitMapper wardrobeOutfitMapper;
    private final WardrobeOutfitClothesMapper wardrobeOutfitClothesMapper;
    private final WardrobeOutfitFavoriteMapper wardrobeOutfitFavoriteMapper;

    /**
     * 查询穿搭方案
     */
    @Override
    public WardrobeOutfit selectWardrobeOutfitById(Long id) {
        WardrobeOutfit outfit = wardrobeOutfitMapper.selectWardrobeOutfitById(id);
        if (outfit != null) {
            // 查询关联的衣物列表
            List<WardrobeOutfitClothes> clothesList = wardrobeOutfitClothesMapper.selectByOutfitId(id);
            outfit.setClothesList(clothesList);
        }
        return outfit;
    }

    /**
     * 查询穿搭方案列表
     */
    @Override
    public List<WardrobeOutfit> selectWardrobeOutfitList(WardrobeOutfit wardrobeOutfit) {
        return wardrobeOutfitMapper.selectWardrobeOutfitList(wardrobeOutfit);
    }

    /**
     * 查询公开的穿搭列表
     */
    @Override
    public List<WardrobeOutfit> selectPublicOutfitList(WardrobeOutfit wardrobeOutfit) {
        return wardrobeOutfitMapper.selectPublicOutfitList(wardrobeOutfit);
    }

    /**
     * 查询用户收藏的穿搭列表
     */
    @Override
    public List<WardrobeOutfit> selectFavoriteOutfitList(Long currentUserId, WardrobeOutfit wardrobeOutfit) {
        return wardrobeOutfitMapper.selectFavoriteOutfitList(currentUserId, wardrobeOutfit);
    }

    /**
     * 新增穿搭方案
     */
    @Override
    @Transactional
    public int insertWardrobeOutfit(WardrobeOutfit wardrobeOutfit) {
        int rows = wardrobeOutfitMapper.insertWardrobeOutfit(wardrobeOutfit);
        // 保存穿搭-衣物关联
        if (rows > 0 && wardrobeOutfit.getClothesIds() != null && !wardrobeOutfit.getClothesIds().isEmpty()) {
            saveOutfitClothes(wardrobeOutfit.getId(), wardrobeOutfit.getClothesIds());
        }
        return rows;
    }

    /**
     * 修改穿搭方案
     */
    @Override
    @Transactional
    public int updateWardrobeOutfit(WardrobeOutfit wardrobeOutfit) {
        // 先删除原有的衣物关联
        wardrobeOutfitClothesMapper.deleteByOutfitId(wardrobeOutfit.getId());
        // 重新保存衣物关联
        if (wardrobeOutfit.getClothesIds() != null && !wardrobeOutfit.getClothesIds().isEmpty()) {
            saveOutfitClothes(wardrobeOutfit.getId(), wardrobeOutfit.getClothesIds());
        }
        return wardrobeOutfitMapper.updateWardrobeOutfit(wardrobeOutfit);
    }

    /**
     * 保存穿搭-衣物关联
     */
    private void saveOutfitClothes(Long outfitId, List<Long> clothesIds) {
        List<WardrobeOutfitClothes> list = new ArrayList<>();
        for (int i = 0; i < clothesIds.size(); i++) {
            WardrobeOutfitClothes outfitClothes = new WardrobeOutfitClothes();
            outfitClothes.setOutfitId(outfitId);
            outfitClothes.setClothesId(clothesIds.get(i));
            outfitClothes.setSortOrder(i);
            list.add(outfitClothes);
        }
        if (!list.isEmpty()) {
            wardrobeOutfitClothesMapper.batchInsert(list);
        }
    }

    /**
     * 批量删除穿搭方案
     */
    @Override
    @Transactional
    public int deleteWardrobeOutfitByIds(Long[] ids) {
        // 删除关联的衣物和收藏记录
        for (Long id : ids) {
            wardrobeOutfitClothesMapper.deleteByOutfitId(id);
            wardrobeOutfitFavoriteMapper.deleteByOutfitId(id);
        }
        return wardrobeOutfitMapper.deleteWardrobeOutfitByIds(ids);
    }

    /**
     * 删除穿搭方案
     */
    @Override
    @Transactional
    public int deleteWardrobeOutfitById(Long id) {
        wardrobeOutfitClothesMapper.deleteByOutfitId(id);
        wardrobeOutfitFavoriteMapper.deleteByOutfitId(id);
        return wardrobeOutfitMapper.deleteWardrobeOutfitById(id);
    }

    /**
     * 更新穿着记录
     */
    @Override
    public int updateWearRecord(Long id) {
        return wardrobeOutfitMapper.updateWearRecord(id);
    }

    /**
     * 添加收藏
     */
    @Override
    @Transactional
    public int addFavorite(Long userId, Long outfitId) {
        // 检查是否已收藏
        if (checkFavorite(userId, outfitId)) {
            return 0;
        }
        // 添加收藏记录
        WardrobeOutfitFavorite favorite = new WardrobeOutfitFavorite();
        favorite.setUserId(userId);
        favorite.setOutfitId(outfitId);
        int result = wardrobeOutfitFavoriteMapper.insert(favorite);
        // 更新穿搭收藏数量
        if (result > 0) {
            int count = wardrobeOutfitFavoriteMapper.countByOutfitId(outfitId);
            wardrobeOutfitMapper.updateFavoriteCount(outfitId, count);
        }
        return result;
    }

    /**
     * 取消收藏
     */
    @Override
    @Transactional
    public int removeFavorite(Long userId, Long outfitId) {
        int result = wardrobeOutfitFavoriteMapper.deleteByUserAndOutfit(userId, outfitId);
        // 更新穿搭收藏数量
        if (result > 0) {
            int count = wardrobeOutfitFavoriteMapper.countByOutfitId(outfitId);
            wardrobeOutfitMapper.updateFavoriteCount(outfitId, count);
        }
        return result;
    }

    /**
     * 检查用户是否已收藏某穿搭
     */
    @Override
    public boolean checkFavorite(Long userId, Long outfitId) {
        return wardrobeOutfitFavoriteMapper.selectByUserAndOutfit(userId, outfitId) != null;
    }

    /**
     * 填充当前用户的收藏状态
     */
    @Override
    public void fillCurrentUserFavorite(List<WardrobeOutfit> outfitList, Long currentUserId) {
        if (outfitList == null || outfitList.isEmpty() || currentUserId == null) {
            return;
        }
        // 获取用户收藏的所有穿搭ID
        List<Long> favoriteIds = wardrobeOutfitFavoriteMapper.selectFavoriteOutfitIds(currentUserId);
        Set<Long> favoriteSet = new HashSet<>(favoriteIds);
        // 填充收藏状态
        for (WardrobeOutfit outfit : outfitList) {
            outfit.setCurrentUserFavorite(favoriteSet.contains(outfit.getId()));
        }
    }
}
