package com.suxiaowen.project.system.service.impl;

import com.suxiaowen.project.system.domain.Questionnaire;
import com.suxiaowen.project.system.domain.mongo.QuestionnaireStructure;
import com.suxiaowen.project.system.mapper.QuestionnaireAnswerMapper;
import com.suxiaowen.project.system.mapper.QuestionnaireMapper;
import com.suxiaowen.project.system.service.IQuestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.suxiaowen.common.constant.Constants.MONGODB_COLLECTION_Q_STRUCT;

/**
 * @author 张昊
 * @description 针对表【questionnaire(问卷表)】的数据库操作Service实现
 * @createDate 2022-06-23 19:49:38
 */
@Service
public class QuestionnaireServiceImpl implements IQuestionnaireService {
    @Autowired
    QuestionnaireMapper questionnaireMapper;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    QuestionnaireAnswerMapper answerMapper;

    /**
     * @param id QuestionnaireId
     * @return
     */
    @Override
    public boolean isQuestionnaireExist(String id) {
        if (questionnaireMapper.selectByPrimaryKey(id) == null) {
            return false;
        } else return true;
    }

    @Override
    public Questionnaire getQuestionnaire(String id) {
        return questionnaireMapper.selectByPrimaryKey(id);
    }

    /**
     * @param id QuestionnaireId
     * @return
     */
    @Override
    public QuestionnaireStructure getQuestionnaireStructure(String id) {
        Questionnaire questionnaire = questionnaireMapper.selectByPrimaryKey(id);
        String docId = questionnaire.getStructure();
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(docId));
        return mongoTemplate.findOne(query, QuestionnaireStructure.class, MONGODB_COLLECTION_Q_STRUCT);
    }

    @Override
    public String saveStructure(QuestionnaireStructure structure) {
        structure = mongoTemplate.save(structure, MONGODB_COLLECTION_Q_STRUCT);
        return structure.getId();
    }

    @Override
    public void modifyQuestionnaire(Questionnaire questionnaire) {
        if (isQuestionnaireExist(questionnaire.getQuestionnaireId())) {
            questionnaire.setUserId(null);
            questionnaire.setCreateTime(null);
            questionnaireMapper.updateByPrimaryKeySelective(questionnaire);
        } else {
            questionnaireMapper.insertSelective(questionnaire);
        }
    }

    @Override
    public void modifyStructure(QuestionnaireStructure structure) {
        // 需不需要考虑之前不存在
        mongoTemplate.save(structure, MONGODB_COLLECTION_Q_STRUCT);
    }

    @Override
    public List<Questionnaire> getAllQuestionnaire() {
        return questionnaireMapper.getAllRecord();
    }

    @Override
    public List<Questionnaire> getUserQuestionnaireById(Long userId) {
        return questionnaireMapper.selectAllByUserId(userId);
    }

    @Override
    public void delQuestionnaireById(String questionnaireId) {
        questionnaireMapper.deleteByPrimaryKey(questionnaireId);
    }

    @Override
    public void saveQuestionnaire(Questionnaire questionnaire) {
        questionnaireMapper.insert(questionnaire);
    }


}
