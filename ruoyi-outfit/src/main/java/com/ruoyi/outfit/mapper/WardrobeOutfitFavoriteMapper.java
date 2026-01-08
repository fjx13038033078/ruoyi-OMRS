package com.ruoyi.outfit.mapper;

import com.ruoyi.outfit.domain.WardrobeOutfitFavorite;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 穿搭收藏Mapper接口
 *
 * @author fanjiaxing
 */
public interface WardrobeOutfitFavoriteMapper {

    /**
     * 查询用户是否已收藏某穿搭
     *
     * @param userId 用户ID
     * @param outfitId 穿搭ID
     * @return 收藏记录
     */
    WardrobeOutfitFavorite selectByUserAndOutfit(@Param("userId") Long userId, @Param("outfitId") Long outfitId);

    /**
     * 查询用户收藏的穿搭ID列表
     *
     * @param userId 用户ID
     * @return 穿搭ID列表
     */
    List<Long> selectFavoriteOutfitIds(@Param("userId") Long userId);

    /**
     * 新增收藏
     *
     * @param favorite 收藏信息
     * @return 结果
     */
    int insert(WardrobeOutfitFavorite favorite);

    /**
     * 删除收藏
     *
     * @param userId 用户ID
     * @param outfitId 穿搭ID
     * @return 结果
     */
    int deleteByUserAndOutfit(@Param("userId") Long userId, @Param("outfitId") Long outfitId);

    /**
     * 统计穿搭收藏数量
     *
     * @param outfitId 穿搭ID
     * @return 收藏数量
     */
    int countByOutfitId(@Param("outfitId") Long outfitId);

    /**
     * 删除穿搭相关的所有收藏记录
     *
     * @param outfitId 穿搭ID
     * @return 结果
     */
    int deleteByOutfitId(@Param("outfitId") Long outfitId);
}
