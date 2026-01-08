package com.ruoyi.outfit.service;

import com.ruoyi.outfit.domain.WardrobeOutfit;

import java.util.List;

/**
 * 穿搭方案Service接口
 *
 * @author fanjiaxing
 */
public interface IWardrobeOutfitService {

    /**
     * 查询穿搭方案
     *
     * @param id 穿搭ID
     * @return 穿搭方案
     */
    WardrobeOutfit selectWardrobeOutfitById(Long id);

    /**
     * 查询穿搭方案列表
     *
     * @param wardrobeOutfit 穿搭方案
     * @return 穿搭方案集合
     */
    List<WardrobeOutfit> selectWardrobeOutfitList(WardrobeOutfit wardrobeOutfit);

    /**
     * 查询公开的穿搭列表
     *
     * @param wardrobeOutfit 查询条件
     * @return 穿搭方案集合
     */
    List<WardrobeOutfit> selectPublicOutfitList(WardrobeOutfit wardrobeOutfit);

    /**
     * 查询用户收藏的穿搭列表
     *
     * @param currentUserId 当前用户ID
     * @param wardrobeOutfit 查询条件
     * @return 穿搭方案集合
     */
    List<WardrobeOutfit> selectFavoriteOutfitList(Long currentUserId, WardrobeOutfit wardrobeOutfit);

    /**
     * 新增穿搭方案
     *
     * @param wardrobeOutfit 穿搭方案
     * @return 结果
     */
    int insertWardrobeOutfit(WardrobeOutfit wardrobeOutfit);

    /**
     * 修改穿搭方案
     *
     * @param wardrobeOutfit 穿搭方案
     * @return 结果
     */
    int updateWardrobeOutfit(WardrobeOutfit wardrobeOutfit);

    /**
     * 批量删除穿搭方案
     *
     * @param ids 需要删除的穿搭ID
     * @return 结果
     */
    int deleteWardrobeOutfitByIds(Long[] ids);

    /**
     * 删除穿搭方案
     *
     * @param id 穿搭ID
     * @return 结果
     */
    int deleteWardrobeOutfitById(Long id);

    /**
     * 更新穿着记录
     *
     * @param id 穿搭ID
     * @return 结果
     */
    int updateWearRecord(Long id);

    /**
     * 添加收藏
     *
     * @param userId 用户ID
     * @param outfitId 穿搭ID
     * @return 结果
     */
    int addFavorite(Long userId, Long outfitId);

    /**
     * 取消收藏
     *
     * @param userId 用户ID
     * @param outfitId 穿搭ID
     * @return 结果
     */
    int removeFavorite(Long userId, Long outfitId);

    /**
     * 检查用户是否已收藏某穿搭
     *
     * @param userId 用户ID
     * @param outfitId 穿搭ID
     * @return 是否已收藏
     */
    boolean checkFavorite(Long userId, Long outfitId);

    /**
     * 填充当前用户的收藏状态
     *
     * @param outfitList 穿搭列表
     * @param currentUserId 当前用户ID
     */
    void fillCurrentUserFavorite(List<WardrobeOutfit> outfitList, Long currentUserId);
}
