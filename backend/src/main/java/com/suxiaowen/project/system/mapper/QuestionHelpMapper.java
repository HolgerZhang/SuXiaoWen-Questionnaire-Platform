package com.suxiaowen.project.system.mapper;

import com.suxiaowen.project.system.domain.QuestionHelp;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author honor
 * @description 针对表【question_help(互助平台表)】的数据库操作Mapper
 * @createDate 2022-06-24 09:56:33
 * @Entity com.suxiaowen.project.system.domain.QuestionHelp
 */
@Mapper
public interface QuestionHelpMapper {

    int deleteByPrimaryKey(String id);

    int insert(QuestionHelp record);

    int insertSelective(QuestionHelp record);

    QuestionHelp selectByPrimaryKey(String id);

    List<QuestionHelp> selectAllByQuestionnaireId(String questionnaireId);

    int updateByPrimaryKeySelective(QuestionHelp record);

    int updateByPrimaryKey(QuestionHelp record);

    List<QuestionHelp> selectAllAvailableTask();
}
