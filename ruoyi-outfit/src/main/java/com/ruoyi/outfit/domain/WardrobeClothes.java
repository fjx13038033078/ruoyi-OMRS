package com.ruoyi.outfit.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 衣物信息对象 wardrobe_clothes
 *
 * @author ruoyi
 */
public class WardrobeClothes extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 衣物ID */
    private Long id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 衣物名称 */
    @Excel(name = "衣物名称")
    private String name;

    /** 类别编码 */
    @Excel(name = "类别")
    private String category;

    /** 子类别编码 */
    @Excel(name = "子类别")
    private String subCategory;

    /** 适用季节 */
    @Excel(name = "适用季节")
    private String season;

    /** 颜色 */
    @Excel(name = "颜色")
    private String color;

    /** 颜色色值 */
    private String colorHex;

    /** 材质 */
    @Excel(name = "材质")
    private String material;

    /** 品牌 */
    @Excel(name = "品牌")
    private String brand;

    /** 购买价格 */
    @Excel(name = "价格")
    private BigDecimal price;

    /** 购买日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "购买日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date purchaseDate;

    /** 尺码 */
    @Excel(name = "尺码")
    private String size;

    /** 风格编码 */
    @Excel(name = "风格")
    private String style;

    /** 适用场合 */
    @Excel(name = "适用场合")
    private String occasion;

    /** 洗涤方式 */
    @Excel(name = "洗涤方式")
    private String washMethod;

    /** 图片地址 */
    private String imageUrl;

    /** 描述/备注 */
    @Excel(name = "描述")
    private String description;

    /** 穿着次数 */
    @Excel(name = "穿着次数")
    private Integer wearCount;

    /** 最近穿着日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "最近穿着日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastWearDate;

    /** 是否收藏（0否 1是） */
    @Excel(name = "是否收藏", readConverterExp = "0=否,1=是")
    private String isFavorite;

    /** 状态（0正常 1闲置 2已处理） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=闲置,2=已处理")
    private String status;

    /** 删除标志（0存在 2删除） */
    private String delFlag;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getSeason() {
        return season;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public void setColorHex(String colorHex) {
        this.colorHex = colorHex;
    }

    public String getColorHex() {
        return colorHex;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getMaterial() {
        return material;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBrand() {
        return brand;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSize() {
        return size;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getStyle() {
        return style;
    }

    public void setOccasion(String occasion) {
        this.occasion = occasion;
    }

    public String getOccasion() {
        return occasion;
    }

    public void setWashMethod(String washMethod) {
        this.washMethod = washMethod;
    }

    public String getWashMethod() {
        return washMethod;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setWearCount(Integer wearCount) {
        this.wearCount = wearCount;
    }

    public Integer getWearCount() {
        return wearCount;
    }

    public void setLastWearDate(Date lastWearDate) {
        this.lastWearDate = lastWearDate;
    }

    public Date getLastWearDate() {
        return lastWearDate;
    }

    public void setIsFavorite(String isFavorite) {
        this.isFavorite = isFavorite;
    }

    public String getIsFavorite() {
        return isFavorite;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getDelFlag() {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("userId", getUserId())
                .append("name", getName())
                .append("category", getCategory())
                .append("subCategory", getSubCategory())
                .append("season", getSeason())
                .append("color", getColor())
                .append("colorHex", getColorHex())
                .append("material", getMaterial())
                .append("brand", getBrand())
                .append("price", getPrice())
                .append("purchaseDate", getPurchaseDate())
                .append("size", getSize())
                .append("style", getStyle())
                .append("occasion", getOccasion())
                .append("washMethod", getWashMethod())
                .append("imageUrl", getImageUrl())
                .append("description", getDescription())
                .append("wearCount", getWearCount())
                .append("lastWearDate", getLastWearDate())
                .append("isFavorite", getIsFavorite())
                .append("status", getStatus())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}

