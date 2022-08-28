package com.suxiaowen.project.system.service.impl;

import com.suxiaowen.common.utils.uuid.IdUtils;
import com.suxiaowen.project.system.domain.QuestionHelp;
import com.suxiaowen.project.system.domain.Questionnaire;
import com.suxiaowen.project.system.domain.QuestionnaireAnswer;
import com.suxiaowen.project.system.mapper.QuestionHelpMapper;
import com.suxiaowen.project.system.mapper.QuestionnaireAnswerMapper;
import com.suxiaowen.project.system.mapper.QuestionnaireMapper;
import com.suxiaowen.project.system.service.IHelpCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 实现互助平台的业务逻辑
 */
@Service
public class HelpCenterServiceImpl implements IHelpCenterService {

    @Autowired
    private QuestionHelpMapper helpMapper;

    @Autowired
    private QuestionnaireAnswerMapper answerMapper;

    @Autowired
    private QuestionnaireMapper questionnaireMapper;

    @Override
    public void createTask(QuestionHelp task) {
        task.setTaskId(IdUtils.fastUUID());
        task.setCounts(0L);
        task.setEnabled("0");
        helpMapper.insert(task);
    }

    @Override
    public QuestionHelp findTask(String taskId) {
        return helpMapper.selectByPrimaryKey(taskId);
    }

    public void setCounts(String taskId, Long count) {
        QuestionHelp h = new QuestionHelp();
        h.setTaskId(taskId);
        h.setCounts(count);
        helpMapper.updateByPrimaryKeySelective(h);
    }

    @Override
    public QuestionHelp findTaskByQuestion(String questionnaireId) {
        List<QuestionHelp> tasks = helpMapper.selectAllByQuestionnaireId(questionnaireId);
        if (tasks.size() == 0) {
            return null;
        }
        return tasks.get(0);
    }

    @Override
    public void updateTask(QuestionHelp task) {
        helpMapper.updateByPrimaryKeySelective(task);
    }

    @Override
    public void deleteTask(String taskId) {
        helpMapper.deleteByPrimaryKey(taskId);
    }

    @Override
    public List<Map<String, Object>> listAll() {
        List<QuestionHelp> tasks = helpMapper.selectAllAvailableTask();  //任务的列表
        List<Map<String, Object>> result = new LinkedList<>();
        for (QuestionHelp task : tasks) {
            Map<String, Object> item = new HashMap<>();
            item.put("taskId", task.getTaskId());
            item.put("questionnaireId", task.getQuestionnaireId());
            item.put("amount", task.getAmount());
            item.put("counts", task.getCounts());
            item.put("price", task.getPrice());
            item.put("enabled", task.getEnabled());
            Questionnaire questionnaire = questionnaireMapper.selectByPrimaryKey(task.getQuestionnaireId());
            item.put("questionnaire", questionnaire);
            result.add(item);
        }
        return result;
    }

    @Override
    public void invite(Long userId, String questionnaireId) {
        // answer_status=2，fill_ip留空，其余按照实际填写
        QuestionnaireAnswer answer = new QuestionnaireAnswer();
        answer.setAnswerId(IdUtils.fastUUID());
        answer.setAnswerStatus("2");
        answer.setFillIp("");
        answer.setUserId(userId);
        answer.setQuestionnaireId(questionnaireId);
        answer.setStarFlag("0");
        answer.setCreateTime(new Date());
        answer.setDetail("");
        answerMapper.insert(answer);
    }


}
