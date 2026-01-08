package com.ruoyi.outfit.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * 穿搭方案实体类
 *
 * @author fanjiaxing
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@TableName("wardrobe_outfit")
public class WardrobeOutfit extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 穿搭ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @Excel(name = "用户ID")
    private Long userId;

    /**
     * 发布者昵称（非数据库字段）
     */
    @TableField(exist = false)
    private String nickName;

    /**
     * 发布者头像（非数据库字段）
     */
    @TableField(exist = false)
    private String avatar;

    /**
     * 当前用户是否已收藏（非数据库字段）
     */
    @TableField(exist = false)
    private Boolean currentUserFavorite;

    /**
     * 穿搭名称
     */
    @Excel(name = "穿搭名称")
    private String name;

    /**
     * 封面图片
     */
    private String coverImage;

    /**
     * 风格编码
     */
    @Excel(name = "风格")
    private String style;

    /**
     * 适用场景编码
     */
    @Excel(name = "场景")
    private String scene;

    /**
     * 适用季节
     */
    @Excel(name = "季节")
    private String season;

    /**
     * 描述/备注
     */
    @Excel(name = "描述")
    private String description;

    /**
     * 标签（逗号分隔）
     */
    private String tags;

    /**
     * 是否公开（0私密 1公开）
     */
    @Excel(name = "是否公开", readConverterExp = "0=私密,1=公开")
    private String isPublic;

    /**
     * 收藏数量
     */
    @Excel(name = "收藏数量")
    private Integer favoriteCount;

    /**
     * 穿着次数
     */
    @Excel(name = "穿着次数")
    private Integer wearCount;

    /**
     * 最近穿着日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "最近穿着日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastWearDate;

    /**
     * 状态（0正常 1禁用）
     */
    @Excel(name = "状态", readConverterExp = "0=正常,1=禁用")
    private String status;

    /**
     * 删除标志（0存在 2删除）
     */
    private String delFlag;

    /**
     * 穿搭包含的衣物列表（非数据库字段）
     */
    @TableField(exist = false)
    private List<WardrobeOutfitClothes> clothesList;

    /**
     * 穿搭包含的衣物ID列表（用于前端传参）
     */
    @TableField(exist = false)
    private List<Long> clothesIds;
}
