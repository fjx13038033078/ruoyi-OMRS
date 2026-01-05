package com.ruoyi.outfit.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 用户场景偏好对象 wardrobe_user_scene
 *
 * @author ruoyi
 */
public class WardrobeUserScene extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 场景编码 */
    @Excel(name = "场景编码")
    private String sceneCode;

    /** 场景名称 */
    @Excel(name = "场景名称")
    private String sceneName;

    /** 使用频率(1-5) */
    @Excel(name = "使用频率")
    private Integer frequency;

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

    public void setSceneCode(String sceneCode) {
        this.sceneCode = sceneCode;
    }

    public String getSceneCode() {
        return sceneCode;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public String getSceneName() {
        return sceneName;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public Integer getFrequency() {
        return frequency;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("userId", getUserId())
                .append("sceneCode", getSceneCode())
                .append("sceneName", getSceneName())
                .append("frequency", getFrequency())
                .append("createTime", getCreateTime())
                .toString();
    }
}

