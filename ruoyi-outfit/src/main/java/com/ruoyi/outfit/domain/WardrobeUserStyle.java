package com.ruoyi.outfit.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 用户风格偏好对象 wardrobe_user_style
 *
 * @author ruoyi
 */
public class WardrobeUserStyle extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 风格编码 */
    @Excel(name = "风格编码")
    private String styleCode;

    /** 风格名称 */
    @Excel(name = "风格名称")
    private String styleName;

    /** 偏好程度(1-5) */
    @Excel(name = "偏好程度")
    private Integer preferenceLevel;

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

    public void setStyleCode(String styleCode) {
        this.styleCode = styleCode;
    }

    public String getStyleCode() {
        return styleCode;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public String getStyleName() {
        return styleName;
    }

    public void setPreferenceLevel(Integer preferenceLevel) {
        this.preferenceLevel = preferenceLevel;
    }

    public Integer getPreferenceLevel() {
        return preferenceLevel;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("userId", getUserId())
                .append("styleCode", getStyleCode())
                .append("styleName", getStyleName())
                .append("preferenceLevel", getPreferenceLevel())
                .append("createTime", getCreateTime())
                .toString();
    }
}

