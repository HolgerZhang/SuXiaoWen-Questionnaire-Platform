package com.suxiaowen.project.system.service.impl;

import com.mongodb.client.result.DeleteResult;
import com.suxiaowen.project.system.domain.Questionnaire;
import com.suxiaowen.project.system.domain.QuestionnaireAnswer;
import com.suxiaowen.project.system.domain.mongo.QAnswer;
import com.suxiaowen.project.system.domain.mongo.QuestionnaireStructure;
import com.suxiaowen.project.system.mapper.QuestionnaireAnswerMapper;
import com.suxiaowen.project.system.mapper.QuestionnaireMapper;
import com.suxiaowen.project.system.service.IQuestionnaireAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.suxiaowen.common.constant.Constants.MONGODB_COLLECTION_Q_ANSWER;

/**
 * @author 张昊
 * @description 针对表【questionnaire_answer(答卷表)】的数据库操作Service实现
 * @createDate 2022-06-23 19:49:38
 */
@Service
public class QuestionnaireAnswerServiceImpl implements IQuestionnaireAnswerService {
    @Autowired
    QuestionnaireAnswerMapper answerMapper;
    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    QuestionnaireMapper questionnaireMapper;

    @Override
    public String saveAnswer(QAnswer qAnswer) {
        // 存入一个新的Answer
        qAnswer = mongoTemplate.save(qAnswer, MONGODB_COLLECTION_Q_ANSWER);
        return qAnswer.getId();
    }

    @Override
    public QAnswer getQAnswerByDocID(String docId) {
        // 根据DocID获取QA
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(docId));
        return mongoTemplate.findOne(query, QAnswer.class, MONGODB_COLLECTION_Q_ANSWER);
    }

    /**
     * @param answerId UUID
     * @return
     */
    @Override
    public QuestionnaireAnswer getQuestionnaireAnswerByAnswerId(String answerId) {
        // 从mysql中根据answerId获取questionnaireAnswer
        return answerMapper.selectByPrimaryKey(answerId);
    }

    @Override
    public boolean modifyAnswer(String detail, QAnswer qAnswer) {
        // 向mongoDb中修改一个已经存在的QA
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(detail));
        QAnswer answer = mongoTemplate.findOne(query, QAnswer.class, MONGODB_COLLECTION_Q_ANSWER);
        if (answer == null) {
            answer = qAnswer;
            answer.setId(detail);
        } else {
            answer.setAnswers(qAnswer.getAnswers());
        }
        mongoTemplate.save(answer, MONGODB_COLLECTION_Q_ANSWER);
        return true;
    }


    @Override
    public int countUserAnswerQuestionTimes(Long userId, String questionId) {
        List<QuestionnaireAnswer> list = answerMapper.selectAllByUserId(userId);
        int count = 0;
        for (QuestionnaireAnswer answer : list) {
            if (answer.getQuestionnaireId().equals(questionId) && answer.getAnswerStatus().equals("0")) {
                count++;
            }
        }
        return count;
    }

    @Override
    public QuestionnaireAnswer searchInviteTask(String questionnaireId, Long userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("questionnaireId", questionnaireId);
        map.put("userId", userId);
        return answerMapper.searchInviteAnswerByQidUid(map);
    }

    @Override
    public void insertOrUpdateAnswerInfo(QuestionnaireAnswer questionnaireAnswer, boolean insertFlag) {
        if (insertFlag) {
            answerMapper.insertSelective(questionnaireAnswer);
        } else {
            answerMapper.updateByPrimaryKeySelective(questionnaireAnswer);
        }
    }

    @Override
    public void updateStarFlag(QuestionnaireAnswer answer) {
        if ("0".equals(answer.getStarFlag())) {
            answer.setStarFlag("1");
        } else {
            answer.setStarFlag("0");
        }
        answerMapper.updateByPrimaryKeySelective(answer);
    }

    @Override
    public boolean setUnavailableAnswer(QuestionnaireAnswer answer) {
        answer.setAnswerStatus("1");
        String docId = answer.getDetail();
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(docId));
        DeleteResult ret1 = mongoTemplate.remove(query, QAnswer.class, MONGODB_COLLECTION_Q_ANSWER);
        int ret2 = answerMapper.updateByPrimaryKeySelective(answer);
        return ret1.getDeletedCount() > 0 && ret2 > 0;
    }

    @Override
    public List<QuestionnaireAnswer> listAnswerInfoListByQuestionnaireId(String questionnaireId) {
        List<QuestionnaireAnswer> result = answerMapper.selectAllByQuestionnaireId(questionnaireId);
        result = result.stream().filter((answer) -> {
            return !answer.getAnswerStatus().equals("1");
        }).collect(Collectors.toList());
        return result;
    }

    @Override
    public List<QuestionnaireAnswer> listAnswerInfoListByUserId(Long userId) {
        List<QuestionnaireAnswer> result = answerMapper.selectAllByUserId(userId);
        result = result.stream().filter((answer) -> {
            return !answer.getAnswerStatus().equals("1");
        }).collect(Collectors.toList());
        return result;
    }

    @Override
    public List<Map<String, Object>> listInvitedAnswerInfoListByUserId(Long userId) {
        List<QuestionnaireAnswer> result = answerMapper.selectAllByUserId(userId);
        result = result.stream().filter((answer) -> {
            return answer.getAnswerStatus().equals("2");
        }).collect(Collectors.toList());
        List<Map<String, Object>> ret = new LinkedList<>();
        for (QuestionnaireAnswer answer : result) {
            Map<String, Object> item = new HashMap<>();
            item.put("answerId", answer.getAnswerId());
            item.put("fillIp", answer.getFillIp());
            item.put("userId", answer.getUserId());
            item.put("answerStatus", answer.getAnswerStatus());
            item.put("questionnaireId", answer.getQuestionnaireId());
            item.put("createTime", answer.getCreateTime());
            item.put("detail", answer.getDetail());
            item.put("starFlag", answer.getStarFlag());
            Questionnaire questionnaire = questionnaireMapper.selectByPrimaryKey(answer.getQuestionnaireId());
            item.put("questionnaire", questionnaire);
            ret.add(item);
        }
        return ret;
    }

    /**
     * 对问卷数据进行查询筛选
     *
     * @param list  答卷基础信息列表
     * @param query questionId: detail
     *              detail为筛选的逻辑
     *              * C 文本：该题目答案中包含指定文本
     *              * T：该题目已填写（有非空文本）
     * @param type  类型：all(全部满足)|any(满足任一)
     * @return 筛选后的 List of QuestionnaireAnswer
     */
    @Override
    public List<QuestionnaireAnswer> filterAnswerInfo(List<QuestionnaireAnswer> list, Map<String, String> query, String type) {
        if (query.size() == 0) {
            return list;
        }
        list = list.stream().filter((answer) -> {
            Query q = new Query();
            q.addCriteria(Criteria.where("_id").is(answer.getDetail()));
            QAnswer qAnswer = mongoTemplate.findOne(q, QAnswer.class, MONGODB_COLLECTION_Q_ANSWER);
            if (qAnswer == null) {
                return false;
            }
            Map<String, String> answers = qAnswer.getAnswers();
            for (Map.Entry<String, String> entry : query.entrySet()) {
                String[] command = entry.getValue().split(" ");
                String check = answers.getOrDefault(entry.getKey(), null);
                System.out.println(check);
                boolean res;
                switch (command[0]) {
                    case "C":
                        res = check != null && check.contains(command[1]);
                        break;
                    case "T":
                        res = check != null;
                        break;
                    default:
                        res = true;
                }
                switch (type) {
                    case "all":
                        if (!res) return false;
                    case "any":
                        if (res) return true;
                    default:
                        return false;
                }
            }
            switch (type) {
                case "all":
                    return true;
                case "any":
                    return false;
            }
            return false;
        }).collect(Collectors.toList());
        return list;
    }

    @Override
    public void delQuestionnaireAnswerByAnswerId(String answerId) {
        // 要去mongodb删了QAnswer by docId
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(answerId));
        mongoTemplate.remove(query, QAnswer.class, MONGODB_COLLECTION_Q_ANSWER);
    }

    @Override
    public List<Map<String, Object>> statisticAnswers(List<QuestionnaireAnswer> list, QuestionnaireStructure structure) {
        Map<String, Map<String, Object>> _questionCache = new HashMap<>();
        List<Map<String, Object>> result = structure.getQuestions().stream().map(question -> {
            Map<String, Object> item = new HashMap<>();
            item.put("title", question.getTitle());
            item.put("description", question.getDescription());
            item.put("id", question.getId());
            item.put("required", question.getRequired());
            item.put("type", question.getType());
            if (question.getType().equals("text")) {
                item.put("information", new LinkedList<String>());
            } else {
                item.put("_choiceCache", new HashMap<String, Map<String, Object>>());
                item.put("information", question.getInformation().stream().map(choice -> {
                    Map<String, Object> dict = new HashMap<>();
                    dict.put("title", choice);
                    dict.put("num", 0);
                    ((Map<String, Map<String, Object>>) item.get("_choiceCache")).put(choice, dict);
                    return dict;
                }).collect(Collectors.toList()));
            }
            _questionCache.put(question.getId(), item);
            return item;
        }).collect(Collectors.toList());
        for (QuestionnaireAnswer answer : list) {
            QAnswer qAnswer = getQAnswerByDocID(answer.getDetail());
            Map<String, String> answers = qAnswer.getAnswers();
            for (Map.Entry<String, String> entry : answers.entrySet()) {
                String qid = entry.getKey();
                Map<String, Object> question = _questionCache.get(qid);
                if (question == null) {
                    continue;
                }
                if ("text".equals(question.get("type"))) {
                    ((List<String>) question.get("information")).add(entry.getValue());
                } else {
                    Map<String, Map<String, Object>> _choicesCache = (Map<String, Map<String, Object>>) question.get("_choiceCache");
                    if (question.get("type").equals("radio")) {
                        String choice = entry.getValue();
                        Map<String, Object> choiceItem = _choicesCache.get(choice);
                        int num = (int) choiceItem.get("num");
                        choiceItem.put("num", num + 1);
                    } else if (question.get("type").equals("checkbox")) {
                        String[] choices = entry.getValue().split("\\|");
                        for (String choice : choices) {
                            Map<String, Object> choiceItem = _choicesCache.get(choice);
                            int num = (int) choiceItem.get("num");
                            choiceItem.put("num", num + 1);
                        }
                    }
                }
            }
        }
        for (Map<String, Object> r : result) {
            r.remove("_choiceCache");
        }
        return result;
    }
}
