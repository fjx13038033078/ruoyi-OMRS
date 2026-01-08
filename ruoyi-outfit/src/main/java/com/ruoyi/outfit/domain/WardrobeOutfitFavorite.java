package com.ruoyi.outfit.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 穿搭收藏关联实体类
 *
 * @author fanjiaxing
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@TableName("wardrobe_outfit_favorite")
public class WardrobeOutfitFavorite implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 收藏ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 收藏用户ID
     */
    private Long userId;

    /**
     * 穿搭ID
     */
    private Long outfitId;

    /**
     * 收藏时间
     */
    private Date createTime;
}
