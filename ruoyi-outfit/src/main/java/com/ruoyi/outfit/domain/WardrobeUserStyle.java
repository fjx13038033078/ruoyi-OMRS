package com.ruoyi.outfit.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户风格偏好实体类
 *
 * @author fanjiaxing
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@TableName("wardrobe_user_style")
public class WardrobeUserStyle extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @Excel(name = "用户ID")
    private Long userId;

    /**
     * 风格编码
     */
    @Excel(name = "风格编码")
    private String styleCode;

    /**
     * 风格名称
     */
    @Excel(name = "风格名称")
    private String styleName;

    /**
     * 偏好程度(1-5)
     */
    @Excel(name = "偏好程度")
    private Integer preferenceLevel;
}
