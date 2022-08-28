package com.suxiaowen.project.system.service;

import com.suxiaowen.project.system.domain.QuestionHelp;

import java.util.List;
import java.util.Map;

/**
 * 互助平台服务
 */
public interface IHelpCenterService {
    void createTask(QuestionHelp task);

    QuestionHelp findTask(String taskId);

    void setCounts(String taskId, Long count);

    QuestionHelp findTaskByQuestion(String questionnaireId);

    void updateTask(QuestionHelp task);

    void deleteTask(String taskId);

    List<Map<String, Object>> listAll();

    void invite(Long userId, String questionnaireId);
}
