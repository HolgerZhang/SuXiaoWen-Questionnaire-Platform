package com.suxiaowen.project.system.service;

import com.suxiaowen.project.system.domain.QuestionnaireAnswer;
import com.suxiaowen.project.system.domain.mongo.QAnswer;
import com.suxiaowen.project.system.domain.mongo.QuestionnaireStructure;

import java.util.List;
import java.util.Map;

/**
 * @author 张昊
 * @description 针对表【questionnaire_answer(答卷表)】的数据库操作Service
 * @createDate 2022-06-23 19:49:38
 */
public interface IQuestionnaireAnswerService {

    /**
     * @param qAnswer 一个qAnswer对象,用于存入mongodb
     * @return mongodb返回的documentId
     */
    String saveAnswer(QAnswer qAnswer);


    /**
     * @param docId documentId
     * @return mongodb 中docId对应的qAnswer
     */
    QAnswer getQAnswerByDocID(String docId);

    /**
     * @param answerId UUID
     * @return mySql(?)中的questionnaireAnswer
     */
    QuestionnaireAnswer getQuestionnaireAnswerByAnswerId(String answerId);

    /**
     * @param detail  mongodb 中 QAnswer的docId
     * @param qAnswer 新的数据
     * @return 是否成功?
     */
    boolean modifyAnswer(String detail, QAnswer qAnswer);

    int countUserAnswerQuestionTimes(Long userId, String questionId);

    QuestionnaireAnswer searchInviteTask(String questionnaireId, Long userId);

    void insertOrUpdateAnswerInfo(QuestionnaireAnswer questionnaireAnswer, boolean insertFlag);

    void updateStarFlag(QuestionnaireAnswer answer);

    boolean setUnavailableAnswer(QuestionnaireAnswer answer);

    List<QuestionnaireAnswer> listAnswerInfoListByQuestionnaireId(String questionnaireId);

    List<QuestionnaireAnswer> listAnswerInfoListByUserId(Long userId);

    List<Map<String, Object>> listInvitedAnswerInfoListByUserId(Long userId);

    List<QuestionnaireAnswer> filterAnswerInfo(List<QuestionnaireAnswer> list, Map<String, String> query, String type);

    void delQuestionnaireAnswerByAnswerId(String answerId);

    List<Map<String, Object>> statisticAnswers(List<QuestionnaireAnswer> list, QuestionnaireStructure structure);

}
