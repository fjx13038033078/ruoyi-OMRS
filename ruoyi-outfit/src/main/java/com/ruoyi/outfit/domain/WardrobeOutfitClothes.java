package com.ruoyi.outfit.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 穿搭-衣物关联实体类
 *
 * @author fanjiaxing
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@TableName("wardrobe_outfit_clothes")
public class WardrobeOutfitClothes implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 穿搭ID
     */
    private Long outfitId;

    /**
     * 衣物ID
     */
    private Long clothesId;

    /**
     * 衣物在穿搭中的位置/类别
     */
    private String category;

    /**
     * 排序
     */
    private Integer sortOrder;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 关联的衣物信息（非数据库字段）
     */
    @TableField(exist = false)
    private WardrobeClothes clothes;
}
