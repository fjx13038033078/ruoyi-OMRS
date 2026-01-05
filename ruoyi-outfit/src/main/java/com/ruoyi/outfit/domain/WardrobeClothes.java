package com.ruoyi.outfit.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 衣物信息实体类
 *
 * @author ruoyi
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@TableName("wardrobe_clothes")
public class WardrobeClothes extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 衣物ID，主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @Excel(name = "用户ID")
    private Long userId;

    /**
     * 衣物名称
     */
    @Excel(name = "衣物名称")
    private String name;

    /**
     * 类别编码（上衣、裤子、裙子、外套等）
     */
    @Excel(name = "类别")
    private String category;

    /**
     * 子类别编码（T恤、衬衫、牛仔裤等）
     */
    @Excel(name = "子类别")
    private String subCategory;

    /**
     * 适用季节（春、夏、秋、冬、四季）
     */
    @Excel(name = "适用季节")
    private String season;

    /**
     * 颜色
     */
    @Excel(name = "颜色")
    private String color;

    /**
     * 颜色色值
     */
    private String colorHex;

    /**
     * 材质
     */
    @Excel(name = "材质")
    private String material;

    /**
     * 品牌
     */
    @Excel(name = "品牌")
    private String brand;

    /**
     * 购买价格
     */
    @Excel(name = "价格")
    private BigDecimal price;

    /**
     * 购买日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "购买日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date purchaseDate;

    /**
     * 尺码
     */
    @Excel(name = "尺码")
    private String size;

    /**
     * 风格编码
     */
    @Excel(name = "风格")
    private String style;

    /**
     * 适用场合
     */
    @Excel(name = "适用场合")
    private String occasion;

    /**
     * 洗涤方式
     */
    @Excel(name = "洗涤方式")
    private String washMethod;

    /**
     * 图片地址（多个用逗号分隔）
     */
    private String imageUrl;

    /**
     * 描述/备注
     */
    @Excel(name = "描述")
    private String description;

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
     * 是否收藏（0否 1是）
     */
    @Excel(name = "是否收藏", readConverterExp = "0=否,1=是")
    private String isFavorite;

    /**
     * 状态（0正常 1闲置 2已处理）
     */
    @Excel(name = "状态", readConverterExp = "0=正常,1=闲置,2=已处理")
    private String status;

    /**
     * 删除标志（0存在 2删除）
     */
    private String delFlag;
}

