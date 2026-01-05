package com.ruoyi.outfit.mapper;

import java.util.List;
import com.ruoyi.outfit.domain.WardrobeUserQuestionnaire;

/**
 * 用户问卷记录Mapper接口
 *
 * @author ruoyi
 */
public interface WardrobeUserQuestionnaireMapper {
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
     * 批量新增用户问卷记录
     *
     * @param list 用户问卷记录列表
     * @return 结果
     */
    public int batchInsertWardrobeUserQuestionnaire(List<WardrobeUserQuestionnaire> list);

    /**
     * 修改用户问卷记录
     *
     * @param wardrobeUserQuestionnaire 用户问卷记录
     * @return 结果
     */
    public int updateWardrobeUserQuestionnaire(WardrobeUserQuestionnaire wardrobeUserQuestionnaire);

    /**
     * 删除用户问卷记录
     *
     * @param id 用户问卷记录主键
     * @return 结果
     */
    public int deleteWardrobeUserQuestionnaireById(Long id);

    /**
     * 批量删除用户问卷记录
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWardrobeUserQuestionnaireByIds(Long[] ids);

    /**
     * 根据用户ID删除问卷记录
     *
     * @param userId 用户ID
     * @return 结果
     */
    public int deleteWardrobeUserQuestionnaireByUserId(Long userId);
}

