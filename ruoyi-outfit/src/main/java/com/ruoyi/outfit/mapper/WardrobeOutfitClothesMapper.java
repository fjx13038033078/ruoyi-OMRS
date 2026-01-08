package com.ruoyi.outfit.mapper;

import com.ruoyi.outfit.domain.WardrobeOutfitClothes;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 穿搭-衣物关联Mapper接口
 *
 * @author fanjiaxing
 */
public interface WardrobeOutfitClothesMapper {

    /**
     * 根据穿搭ID查询关联的衣物列表
     *
     * @param outfitId 穿搭ID
     * @return 关联列表
     */
    List<WardrobeOutfitClothes> selectByOutfitId(Long outfitId);

    /**
     * 批量新增穿搭-衣物关联
     *
     * @param list 关联列表
     * @return 结果
     */
    int batchInsert(@Param("list") List<WardrobeOutfitClothes> list);

    /**
     * 根据穿搭ID删除关联
     *
     * @param outfitId 穿搭ID
     * @return 结果
     */
    int deleteByOutfitId(Long outfitId);

    /**
     * 根据衣物ID查询被使用的穿搭数量
     *
     * @param clothesId 衣物ID
     * @return 使用数量
     */
    int countByClothesId(Long clothesId);
}
