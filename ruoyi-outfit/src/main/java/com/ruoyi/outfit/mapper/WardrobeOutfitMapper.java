package com.ruoyi.outfit.mapper;

import com.ruoyi.outfit.domain.WardrobeOutfit;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 穿搭方案Mapper接口
 *
 * @author fanjiaxing
 */
public interface WardrobeOutfitMapper {

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
     * @param outfit 查询条件
     * @return 穿搭方案集合
     */
    List<WardrobeOutfit> selectFavoriteOutfitList(@Param("currentUserId") Long currentUserId, @Param("outfit") WardrobeOutfit outfit);

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
     * 删除穿搭方案
     *
     * @param id 穿搭ID
     * @return 结果
     */
    int deleteWardrobeOutfitById(Long id);

    /**
     * 批量删除穿搭方案
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteWardrobeOutfitByIds(Long[] ids);

    /**
     * 更新收藏数量
     *
     * @param id 穿搭ID
     * @param favoriteCount 收藏数量
     * @return 结果
     */
    int updateFavoriteCount(@Param("id") Long id, @Param("favoriteCount") Integer favoriteCount);

    /**
     * 更新穿着记录
     *
     * @param id 穿搭ID
     * @return 结果
     */
    int updateWearRecord(Long id);
}
