package com.suxiaowen.project.system.mapper;

import com.suxiaowen.project.system.domain.Questionnaire;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author honor
 * @description 针对表【questionnaire(问卷表)】的数据库操作Mapper
 * @createDate 2022-06-24 01:54:15
 * @Entity com.suxiaowen.project.system.domain.Questionnaire
 */
@Mapper
public interface QuestionnaireMapper {

    int deleteByPrimaryKey(String id);

    int insert(Questionnaire record);

    int insertSelective(Questionnaire record);

    // 如果没有就返回null
    Questionnaire selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Questionnaire record);

    int updateByPrimaryKey(Questionnaire record);

    List<Questionnaire> getAllRecord();

    List<Questionnaire> selectAllByUserId(Long userId);
}
