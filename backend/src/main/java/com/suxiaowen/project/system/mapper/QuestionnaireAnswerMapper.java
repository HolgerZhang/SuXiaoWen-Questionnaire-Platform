package com.suxiaowen.project.system.mapper;

import com.suxiaowen.project.system.domain.QuestionnaireAnswer;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author honor
 * @description 针对表【questionnaire_answer(答卷表)】的数据库操作Mapper
 * @createDate 2022-06-24 01:54:15
 * @Entity com.suxiaowen.project.system.domain.QuestionnaireAnswer
 */
@Mapper
public interface QuestionnaireAnswerMapper {

    int deleteByPrimaryKey(String id);

    int insert(QuestionnaireAnswer record);

    int insertSelective(QuestionnaireAnswer record);

    QuestionnaireAnswer selectByPrimaryKey(String id);

    List<QuestionnaireAnswer> selectAllByUserId(Long userId);

    int updateByPrimaryKeySelective(QuestionnaireAnswer record);

    int updateByPrimaryKey(QuestionnaireAnswer record);

    QuestionnaireAnswer searchInviteAnswerByQidUid(Map<String, Object> map);

    List<QuestionnaireAnswer> selectAllByQuestionnaireId(String questionnaireId);

}
