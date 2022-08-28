package com.suxiaowen.project.system.controller;

import com.suxiaowen.common.utils.ServletUtils;
import com.suxiaowen.common.utils.ip.IpUtils;
import com.suxiaowen.common.utils.uuid.IdUtils;
import com.suxiaowen.framework.web.domain.AjaxResult;
import com.suxiaowen.project.system.domain.Questionnaire;
import com.suxiaowen.project.system.domain.QuestionnaireAnswer;
import com.suxiaowen.project.system.domain.mongo.QAnswer;
import com.suxiaowen.project.system.domain.mongo.Question;
import com.suxiaowen.project.system.domain.mongo.QuestionnaireStructure;
import com.suxiaowen.project.system.service.IQuestionnaireAnswerService;
import com.suxiaowen.project.system.service.IQuestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

/**
 * 非登录状态下的问卷访问与答卷提交
 */
@RestController
@RequestMapping("/question-anonymous")
public class QuestionAnonymousController {
    @Autowired
    IQuestionnaireService questionnaireService;
    @Autowired
    IQuestionnaireAnswerService answerService;

    /**
     * 获取问卷数据
     *
     * @param id 问卷id
     * @return 问卷数据
     */
    @GetMapping("question/{id}")
    public AjaxResult getQuestionnaire(@PathVariable String id) {
        //     首先检索MySQL，判断该问卷是否存在，不存在报错
        //     之后继续查询，获得问卷基础信息，判断是否允许匿名访问
        //     若存在且可以访问，检索MongoDB的 MONGODB_COLLECTION_Q_STRUCT 集合
        //     获取问卷的结构信息。返回两样东西：问卷基础信息Questionnaire和结构信息QuestionnaireStructure
        //     否则提示错误信息

        Questionnaire questionnaire = null;
        QuestionnaireStructure structure = null;

        if (!questionnaireService.isQuestionnaireExist(id)) {
            return AjaxResult.success("问卷不存在").put("err", 404);
        } else {

            questionnaire = questionnaireService.getQuestionnaire(id);
            if ("0".equals(questionnaire.getAnonymousFlag())) {
                return AjaxResult.success("禁止匿名访问").put("err", 401);
            } else {
                // 检索MongoDb
                structure = questionnaireService.getQuestionnaireStructure(id);
            }
        }

        AjaxResult ajax = AjaxResult.success();
        ajax.put("question", questionnaire);
        ajax.put("structure", structure);
        return ajax;
    }

    /**
     * 提交答卷
     *
     * @param id   问卷id
     * @param data 答卷，题目Document ID与答案的映射
     * @return 是否成功
     */
    @PostMapping("answer/{id}")
    public AjaxResult submitAnswer(@PathVariable String id, @RequestBody Map<String, String> data) {
        //     首先检索MySQL，判断该问卷是否存在，不存在报错
        //     之后继续查询，获得问卷基础信息，判断是否允许匿名访问
        //     若存在且可以访问，检索MongoDB的 MONGODB_COLLECTION_Q_STRUCT 集合
        //     获取问卷的结构信息。
        //     根据问卷的结构信息检查data的正确性，无误后生成 QAnswer 对象保存至MongoDB的 MONGODB_COLLECTION_Q_ANSWER 集合
        //     并装配QuestionnaireAnswer对象：answer_id=UUID, 生成answerId、createTime，获取fillIp，userId置null，
        //     detail为 QAnswer 对象保存时获得的文档ID，其余默认。返回成功信息
        //     否则提示错误信息

        Questionnaire questionnaire = null;
        QuestionnaireStructure structure = null;
        QuestionnaireAnswer questionnaireAnswer = null;

        if (!questionnaireService.isQuestionnaireExist(id)) {
            return AjaxResult.success("问卷不存在").put("err", 404);
        } else {

            questionnaire = questionnaireService.getQuestionnaire(id);
            if (questionnaire == null || !"1".equals(questionnaire.getQuestionnaireStatus())) {
                return AjaxResult.success("问卷不存在或无效").put("err", 404);
            }
            if ("0".equals(questionnaire.getAnonymousFlag())) {
                return AjaxResult.success("禁止匿名访问").put("err", 401);
            } else {
                // 检索MongoDb, 获取structure
                structure = questionnaireService.getQuestionnaireStructure(id);

                // 核对data正确性
                for (Question q : structure.getQuestions()) {
                    if (q.getRequired()) {
                        if (data.get(q.getId()) == null) {
                            return AjaxResult.success("答卷结构错误").put("err", 403);
                        }

                    }
                }
                // 保存到 mongoDb, 保存返回的id值
                QAnswer qAnswer = new QAnswer();
                qAnswer.setAnswers(data);
                String documentId = answerService.saveAnswer(qAnswer);
                // 创建questionnarieAnswer
                questionnaireAnswer = new QuestionnaireAnswer();
                questionnaireAnswer.setAnswerId(IdUtils.randomUUID());
                questionnaireAnswer.setQuestionnaireId(id);
                questionnaireAnswer.setCreateTime(new Date());
                questionnaireAnswer.setFillIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
                questionnaireAnswer.setUserId(null);
                questionnaireAnswer.setDetail(documentId);
                answerService.insertOrUpdateAnswerInfo(questionnaireAnswer, true);
            }
        }

        return AjaxResult.success();
    }

}
