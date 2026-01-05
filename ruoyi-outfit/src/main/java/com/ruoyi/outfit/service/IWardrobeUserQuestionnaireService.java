package com.ruoyi.outfit.service;

import java.util.List;
import com.ruoyi.outfit.domain.WardrobeUserQuestionnaire;

/**
 * 用户问卷记录Service接口
 *
 * @author ruoyi
 */
public interface IWardrobeUserQuestionnaireService {
    /**
     * 查询用户问卷记录
     *
     * @param id 用户问卷记录主键
     * @return 用户问卷记录
     */
    public WardrobeUserQuestionnaire selectWardrobeUserQuestionnaireById(Long id);

    /**
     * 查询用户问卷记录列表
     *
     * @param wardrobeUserQuestionnaire 用户问卷记录
     * @return 用户问卷记录集合
     */
    public List<WardrobeUserQuestionnaire> selectWardrobeUserQuestionnaireList(WardrobeUserQuestionnaire wardrobeUserQuestionnaire);

    /**
     * 根据用户ID查询问卷记录
     *
     * @param userId 用户ID
     * @return 用户问卷记录集合
     */
    public List<WardrobeUserQuestionnaire> selectWardrobeUserQuestionnaireByUserId(Long userId);

    /**
     * 新增用户问卷记录
     *
     * @param wardrobeUserQuestionnaire 用户问卷记录
     * @return 结果
     */
    public int insertWardrobeUserQuestionnaire(WardrobeUserQuestionnaire wardrobeUserQuestionnaire);

    /**
     * 批量保存用户问卷记录
     *
     * @param userId 用户ID
     * @param questionnaireList 问卷记录列表
     * @return 结果
     */
    public int saveUserQuestionnaire(Long userId, List<WardrobeUserQuestionnaire> questionnaireList);

    /**
     * 修改用户问卷记录
     *
     * @param wardrobeUserQuestionnaire 用户问卷记录
     * @return 结果
     */
    public int updateWardrobeUserQuestionnaire(WardrobeUserQuestionnaire wardrobeUserQuestionnaire);

    /**
     * 批量删除用户问卷记录
     *
     * @param ids 需要删除的用户问卷记录主键集合
     * @return 结果
     */
    public int deleteWardrobeUserQuestionnaireByIds(Long[] ids);

    /**
     * 删除用户问卷记录信息
     *
     * @param id 用户问卷记录主键
     * @return 结果
     */
    public int deleteWardrobeUserQuestionnaireById(Long id);

    /**
     * 根据用户ID删除问卷记录
     *
     * @param userId 用户ID
     * @return 结果
     */
    public int deleteWardrobeUserQuestionnaireByUserId(Long userId);
}

