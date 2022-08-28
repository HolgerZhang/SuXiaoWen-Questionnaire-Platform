package com.suxiaowen.project.system.service;

import com.suxiaowen.project.system.domain.Questionnaire;
import com.suxiaowen.project.system.domain.mongo.QuestionnaireStructure;

import java.util.List;

/**
 * @author 张昊
 * @description 针对表【questionnaire(问卷表)】的数据库操作Service
 * @createDate 2022-06-23 19:49:38
 */
public interface IQuestionnaireService {

    /**
     * @param id QuestionnaireId
     * @return 问卷是否存在于数据库中
     */
    boolean isQuestionnaireExist(String id);


    /**
     * @param id QuestionnaireId
     * @return 问卷基本信息
     */
    Questionnaire getQuestionnaire(String id);


    /**
     * @param id QuestionnaireId
     * @return 从mongodb中获取问卷结构信息
     */
    QuestionnaireStructure getQuestionnaireStructure(String id);


    /**
     * @param structure
     * @return DocId
     */
    String saveStructure(QuestionnaireStructure structure);

    /**
     * @param questionnaire 传入 去mysql修改
     */
    void modifyQuestionnaire(Questionnaire questionnaire);

    /**
     * @param structure 给一个带有DocId的structure去里面修改
     */
    void modifyStructure(QuestionnaireStructure structure);

    /**
     * @return
     */
    List<Questionnaire> getAllQuestionnaire();

    /**
     * @param userId
     * @return
     */
    List<Questionnaire> getUserQuestionnaireById(Long userId);

    /**
     * @param questionnaireId
     */
    void delQuestionnaireById(String questionnaireId);

    void saveQuestionnaire(Questionnaire questionnaire);
}
