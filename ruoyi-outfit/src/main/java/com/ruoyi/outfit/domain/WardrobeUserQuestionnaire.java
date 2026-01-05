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
 * 用户问卷记录实体类
 *
 * @author fanjiaxing
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@TableName("wardrobe_user_questionnaire")
public class WardrobeUserQuestionnaire extends BaseEntity {

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
     * 问题编码
     */
    @Excel(name = "问题编码")
    private String questionCode;

    /**
     * 问题内容
     */
    @Excel(name = "问题内容")
    private String questionContent;

    /**
     * 回答内容
     */
    @Excel(name = "回答内容")
    private String answerContent;
}
