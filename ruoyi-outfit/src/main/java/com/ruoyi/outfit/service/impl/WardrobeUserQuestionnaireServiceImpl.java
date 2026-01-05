package com.ruoyi.outfit.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.outfit.mapper.WardrobeUserQuestionnaireMapper;
import com.ruoyi.outfit.domain.WardrobeUserQuestionnaire;
import com.ruoyi.outfit.service.IWardrobeUserQuestionnaireService;

/**
 * 用户问卷记录Service业务层处理
 *
 * @author ruoyi
 */
@Service
public class WardrobeUserQuestionnaireServiceImpl implements IWardrobeUserQuestionnaireService {
    @Autowired
    private WardrobeUserQuestionnaireMapper wardrobeUserQuestionnaireMapper;

    /**
     * 查询用户问卷记录
     *
     * @param id 用户问卷记录主键
     * @return 用户问卷记录
     */
    @Override
    public WardrobeUserQuestionnaire selectWardrobeUserQuestionnaireById(Long id) {
        return wardrobeUserQuestionnaireMapper.selectWardrobeUserQuestionnaireById(id);
    }

    /**
     * 查询用户问卷记录列表
     *
     * @param wardrobeUserQuestionnaire 用户问卷记录
     * @return 用户问卷记录
     */
    @Override
    public List<WardrobeUserQuestionnaire> selectWardrobeUserQuestionnaireList(WardrobeUserQuestionnaire wardrobeUserQuestionnaire) {
        return wardrobeUserQuestionnaireMapper.selectWardrobeUserQuestionnaireList(wardrobeUserQuestionnaire);
    }

    /**
     * 根据用户ID查询问卷记录
     *
     * @param userId 用户ID
     * @return 用户问卷记录集合
     */
    @Override
    public List<WardrobeUserQuestionnaire> selectWardrobeUserQuestionnaireByUserId(Long userId) {
        return wardrobeUserQuestionnaireMapper.selectWardrobeUserQuestionnaireByUserId(userId);
    }

    /**
     * 新增用户问卷记录
     *
     * @param wardrobeUserQuestionnaire 用户问卷记录
     * @return 结果
     */
    @Override
    public int insertWardrobeUserQuestionnaire(WardrobeUserQuestionnaire wardrobeUserQuestionnaire) {
        return wardrobeUserQuestionnaireMapper.insertWardrobeUserQuestionnaire(wardrobeUserQuestionnaire);
    }

    /**
     * 批量保存用户问卷记录
     *
     * @param userId 用户ID
     * @param questionnaireList 问卷记录列表
     * @return 结果
     */
    @Override
    @Transactional
    public int saveUserQuestionnaire(Long userId, List<WardrobeUserQuestionnaire> questionnaireList) {
        // 先删除用户原有的问卷记录
        wardrobeUserQuestionnaireMapper.deleteWardrobeUserQuestionnaireByUserId(userId);
        
        if (questionnaireList != null && !questionnaireList.isEmpty()) {
            // 设置用户ID
            for (WardrobeUserQuestionnaire questionnaire : questionnaireList) {
                questionnaire.setUserId(userId);
            }
            // 批量插入新的问卷记录
            return wardrobeUserQuestionnaireMapper.batchInsertWardrobeUserQuestionnaire(questionnaireList);
        }
        return 0;
    }

    /**
     * 修改用户问卷记录
     *
     * @param wardrobeUserQuestionnaire 用户问卷记录
     * @return 结果
     */
    @Override
    public int updateWardrobeUserQuestionnaire(WardrobeUserQuestionnaire wardrobeUserQuestionnaire) {
        return wardrobeUserQuestionnaireMapper.updateWardrobeUserQuestionnaire(wardrobeUserQuestionnaire);
    }

    /**
     * 批量删除用户问卷记录
     *
     * @param ids 需要删除的用户问卷记录主键
     * @return 结果
     */
    @Override
    public int deleteWardrobeUserQuestionnaireByIds(Long[] ids) {
        return wardrobeUserQuestionnaireMapper.deleteWardrobeUserQuestionnaireByIds(ids);
    }

    /**
     * 删除用户问卷记录信息
     *
     * @param id 用户问卷记录主键
     * @return 结果
     */
    @Override
    public int deleteWardrobeUserQuestionnaireById(Long id) {
        return wardrobeUserQuestionnaireMapper.deleteWardrobeUserQuestionnaireById(id);
    }

    /**
     * 根据用户ID删除问卷记录
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public int deleteWardrobeUserQuestionnaireByUserId(Long userId) {
        return wardrobeUserQuestionnaireMapper.deleteWardrobeUserQuestionnaireByUserId(userId);
    }
}

