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
 * 用户场景偏好实体类
 *
 * @author ruoyi
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@TableName("wardrobe_user_scene")
public class WardrobeUserScene extends BaseEntity {

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
     * 场景编码
     */
    @Excel(name = "场景编码")
    private String sceneCode;

    /**
     * 场景名称
     */
    @Excel(name = "场景名称")
    private String sceneName;

    /**
     * 使用频率(1-5)
     */
    @Excel(name = "使用频率")
    private Integer frequency;
}
