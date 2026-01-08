package com.ruoyi.outfit.mapper;

import com.ruoyi.outfit.domain.WardrobeClothesFavorite;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 衣物收藏Mapper接口
 *
 * @author fanjiaxing
 */
public interface WardrobeClothesFavoriteMapper {

    /**
     * 查询用户是否已收藏某衣物
     *
     * @param userId 用户ID
     * @param clothesId 衣物ID
     * @return 收藏记录
     */
    WardrobeClothesFavorite selectByUserAndClothes(@Param("userId") Long userId, @Param("clothesId") Long clothesId);

    /**
     * 查询用户收藏的衣物ID列表
     *
     * @param userId 用户ID
     * @return 衣物ID列表
     */
    List<Long> selectFavoriteClothesIds(@Param("userId") Long userId);

    /**
     * 新增收藏
     *
     * @param favorite 收藏信息
     * @return 结果
     */
    int insert(WardrobeClothesFavorite favorite);

    /**
     * 删除收藏
     *
     * @param userId 用户ID
     * @param clothesId 衣物ID
     * @return 结果
     */
    int deleteByUserAndClothes(@Param("userId") Long userId, @Param("clothesId") Long clothesId);

    /**
     * 统计衣物收藏数量
     *
     * @param clothesId 衣物ID
     * @return 收藏数量
     */
    int countByClothesId(@Param("clothesId") Long clothesId);

    /**
     * 删除衣物相关的所有收藏记录
     *
     * @param clothesId 衣物ID
     * @return 结果
     */
    int deleteByClothesId(@Param("clothesId") Long clothesId);
}
