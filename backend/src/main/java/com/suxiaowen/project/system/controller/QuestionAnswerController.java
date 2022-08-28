package com.suxiaowen.project.system.controller;

import com.suxiaowen.common.utils.ServletUtils;
import com.suxiaowen.common.utils.ip.IpUtils;
import com.suxiaowen.common.utils.uuid.IdUtils;
import com.suxiaowen.framework.web.controller.BaseController;
import com.suxiaowen.framework.web.domain.AjaxResult;
import com.suxiaowen.project.system.domain.Questionnaire;
import com.suxiaowen.project.system.domain.QuestionnaireAnswer;
import com.suxiaowen.project.system.domain.mongo.QAnswer;
import com.suxiaowen.project.system.domain.mongo.Question;
import com.suxiaowen.project.system.domain.mongo.QuestionnaireStructure;
import com.suxiaowen.project.system.service.IQuestionnaireAnswerService;
import com.suxiaowen.project.system.service.IQuestionnaireService;
import com.suxiaowen.project.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 问卷填写、列答卷列表、查看/修改/删除答卷数据、答卷数据请求
 * 本类中所有代码都要检查是否具有管理员或问卷填写/设计者权限（角色） 非管理员只能对自己的问卷进行操作
 */
@RestController
@RequestMapping("/answer")
public class QuestionAnswerController extends BaseController {
    @Autowired
    IQuestionnaireAnswerService answerService;
    @Autowired
    IQuestionnaireService questionnaireService;
    @Autowired
    ISysUserService userService;

    /**
     * （问卷填写者）提交答卷
     *
     * @param id   问卷id
     * @param data 答卷，题目Document ID与答案的映射
     * @return 是否成功
     */
    @PostMapping("{id}")
    public AjaxResult submitAnswer(@PathVariable String id, @RequestBody Map<String, String> data) {
        // 首先检索MySQL，判断该问卷(id)是否存在，不存在报错;
        //     ！！这里还要检查对于这个问卷(id)、这个填写者（userId），在MySQL中是否存在answerStatus="2"的行，如果有则复用QuestionnaireAnswer对象，不再新建
        //     之后继续查询，获得问卷基础信息，检索MongoDB的 MONGODB_COLLECTION_Q_STRUCT 集合
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
        }
        questionnaire = questionnaireService.getQuestionnaire(id);
        if (questionnaire == null || !"1".equals(questionnaire.getQuestionnaireStatus())) {
            return AjaxResult.success("问卷不存在或无效").put("err", 404);
        }
        if (questionnaire.getDueTime() != null && new Date().after(questionnaire.getDueTime())) {
            return AjaxResult.success("问卷不在有效期内").put("err", 404);
        }
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
        questionnaireAnswer = answerService.searchInviteTask(questionnaire.getQuestionnaireId(), getUserId());
        boolean insertFlag = false;
        if (questionnaireAnswer == null) {
            questionnaireAnswer = new QuestionnaireAnswer();
            questionnaireAnswer.setAnswerId(IdUtils.randomUUID());
            questionnaireAnswer.setUserId(getUserId());
            questionnaireAnswer.setQuestionnaireId(questionnaire.getQuestionnaireId());
            questionnaireAnswer.setStarFlag("0");
            insertFlag = true;
        }
        questionnaireAnswer.setCreateTime(new Date());
        questionnaireAnswer.setFillIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
        questionnaireAnswer.setDetail(documentId);
        questionnaireAnswer.setAnswerStatus("0");
        answerService.insertOrUpdateAnswerInfo(questionnaireAnswer, insertFlag);
        return AjaxResult.success();
    }

    /**
     * 获取答卷的基础信息
     *
     * @param answerId 答卷ID
     * @return QuestionnaireAnswer对象
     */
    @GetMapping("detail/{answerId}")
    public AjaxResult answerDetail(@PathVariable String answerId) {
        //  首先由answerId找到QuestionnaireAnswer
        //  判断符合权限要求（问卷设计者身份查看问卷id是不是自己的，问卷填写者查看填写人是不是自己），不符合要求返回err=404
        //  直接返回QuestionnaireAnswer作为data
        QuestionnaireAnswer questionnaireAnswer = answerService.getQuestionnaireAnswerByAnswerId(answerId);
        if (questionnaireAnswer == null || "1".equals(questionnaireAnswer.getAnswerStatus())) {
            return AjaxResult.success("答卷不存在").put("err", 404);
        }
        Long userId = getUserId();
        if (userService.isSimpleUser(getUsername())) {
            Long fillId = questionnaireAnswer.getUserId();
            if (!userId.equals(fillId)) {
                return AjaxResult.success("答卷不存在").put("err", 404);
            }
        }
        if (userService.isRootOrAdmin(getUsername())) {
            String questionnaireId = questionnaireAnswer.getQuestionnaireId();
            Questionnaire questionnaire = questionnaireService.getQuestionnaire(questionnaireId);
            Long creatorId = questionnaire.getUserId();
            if (!userId.equals(creatorId)) {
                return AjaxResult.success("答卷不存在").put("err", 404);
            }
        }
        return AjaxResult.success(questionnaireAnswer);
    }

    /**
     * （所有人）获取答卷数据详情
     *
     * @param docId 答卷的文档ID
     * @return QAnswer
     */
    @GetMapping("detail/doc/{docId}")
    public AjaxResult answerDetailDoc(@PathVariable String docId) {
        //  直接到 MONGODB_COLLECTION_Q_ANSWER 集合查找 QAnswer 并返回（不校验身份了）
        QAnswer qAnswer = answerService.getQAnswerByDocID(docId);
        return AjaxResult.success(qAnswer);
    }

    /**
     * （所有人）修改答卷数据详情
     *
     * @param answerId 答卷ID
     * @return QAnswer
     */
    @PostMapping("modify/{answerId}")
    public AjaxResult modifyAnswer(@PathVariable String answerId, @RequestBody Map<String, String> answer) {
        // 首先由answerId找到QuestionnaireAnswer判断是否符合权限要求，
        // 问卷填写者需要由questionnaireId找到Questionnaire判断是否允许修改（问卷设计者、管理员不受约束），
        // 最后到 MONGODB_COLLECTION_Q_ANSWER 集合更新 QAnswer
        QuestionnaireAnswer questionnaireAnswer = answerService.getQuestionnaireAnswerByAnswerId(answerId);
        if (questionnaireAnswer == null || "1".equals(questionnaireAnswer.getAnswerStatus())) {
            return AjaxResult.success("答卷不存在").put("err", 404);
        }
        // 如果不是管理员 且不是问卷设计者 (那就是普通用户)
        if (userService.isSimpleUser(getUsername()) && !userService.isDesigner(getUsername())) {
            // 先看是不是自己填写的问卷
            if (!getUserId().equals(questionnaireAnswer.getUserId())) {
                return AjaxResult.success("没有修改权限").put("err", 403);
            }
            // 从答卷提取问卷id, 然后从mysql提取questionnaire, 判断是否允许修改
            String questionnaireId = questionnaireAnswer.getQuestionnaireId();
            Questionnaire questionnaire = questionnaireService.getQuestionnaire(questionnaireId);
            if (!"1".equals(questionnaire.getModifyFlag())) {
                // 若不是问卷设计者
                return AjaxResult.success("没有修改权限").put("err", 403);
            }
        }
        QAnswer qAnswer = new QAnswer();
        qAnswer.setAnswers(answer);
        // 是否需要判断是否更新成功
        answerService.modifyAnswer(questionnaireAnswer.getDetail(), qAnswer);
        return AjaxResult.success();
    }

    /**
     * （问卷填写/设计者）若答卷不为星标，将答卷设置为星标问卷，否则取消星标
     *
     * @param id 答卷id
     * @return 是否成功
     */
    @GetMapping("star/{id}")
    public AjaxResult starAnswer(@PathVariable String id) {
        // 若答卷不为星标，将答卷设置为星标问卷，否则取消星标
        QuestionnaireAnswer answer = answerService.getQuestionnaireAnswerByAnswerId(id);
        if (answer == null || "1".equals(answer.getAnswerStatus())) {
            return AjaxResult.success("答卷不存在").put("err", 404);
        }
        answerService.updateStarFlag(answer);
        return success();
    }

    /**
     * （问卷设计者）将答卷设置为无效答卷，不可撤销
     *
     * @param id 答卷id
     * @return 是否成功
     */
    @DeleteMapping("{id}")
    public AjaxResult disableAnswer(@PathVariable String id) {
        // 只有管理员和问卷设计者可以操作
        if (userService.isSimpleUser(getUsername()) && !userService.isDesigner(getUsername())) {
            return AjaxResult.success("没有修改权限").put("err", 403);
        }
        // 将答卷设置为无效答卷，不可撤销，同时填写的数据会被删除
        QuestionnaireAnswer answer = answerService.getQuestionnaireAnswerByAnswerId(id);
        if (answer == null || "1".equals(answer.getAnswerStatus())) {
            return AjaxResult.success("答卷不存在").put("err", 404);
        }
        boolean ret = answerService.setUnavailableAnswer(answer);
        return success().put("success", ret);
    }

    /**
     * （问卷设计者）列出问卷的所有有效答卷
     *
     * @param questionnaireId 问卷ID
     * @return List of QuestionnaireAnswer
     */
    @GetMapping("question/{questionnaireId}")
    public AjaxResult listQuestionAnswers(@PathVariable String questionnaireId) {
        // 列出问卷的所有有效答卷，对问卷设计者要求问卷必须为自己的，不允许问卷填写者访问
        if (userService.isSimpleUser(getUsername()) && !userService.isDesigner(getUsername())) {
            return AjaxResult.success("没有访问权限").put("err", 403);
        }
        Questionnaire questionnaire = questionnaireService.getQuestionnaire(questionnaireId);
        if (questionnaire == null || !"0".equals(questionnaire.getDelFlag())) {
            return AjaxResult.success("问卷不存在").put("err", 404);
        }
        // 问卷设计者先看是不是自己的问卷
        if (userService.isDesigner(getUsername()) && !getUserId().equals(questionnaire.getUserId())) {
            return AjaxResult.success("没有访问权限").put("err", 403);
        }
        List<QuestionnaireAnswer> list = answerService.listAnswerInfoListByQuestionnaireId(questionnaireId);
        return AjaxResult.success(list);
    }

    /**
     * （问卷填写者）列出用户填写的所有答卷
     *
     * @param userId 问卷ID
     * @param type   类型：all|star
     * @return List of QuestionnaireAnswer
     */
    @GetMapping("user/{userId}")
    public AjaxResult listUserAnswers(@PathVariable Long userId, @RequestParam String type) {
        // 列出用户填写的所有答卷，不允许问卷设计者访问
        if (userService.isDesigner(getUsername()) && !userService.isSimpleUser(getUsername())) {
            return AjaxResult.success("没有访问权限").put("err", 403);
        }
        List<QuestionnaireAnswer> list = answerService.listAnswerInfoListByUserId(userId);
        if (type.equals("star")) {
            list = list.stream().filter((answer) -> "1".equals(answer.getStarFlag())).collect(Collectors.toList());
        }
        List<Map<String, Object>> result = new LinkedList<>();
        for (QuestionnaireAnswer answer : list) {
            Map<String, Object> item = new HashMap<>();
            Questionnaire questionnaire = questionnaireService.getQuestionnaire(answer.getQuestionnaireId());
            item.put("questionnaire", questionnaire);
            item.put("answer", answer);
            result.add(item);
        }
        return AjaxResult.success(result);
    }

    /**
     * （问卷设计者）对问卷数据进行查询筛选
     *
     * @param questionnaireId 问卷ID
     * @param query           questionId: detail
     *                        detail为筛选的逻辑
     *                        * C 文本：该题目答案中包含指定文本
     *                        * T：该题目已填写（有非空文本）
     * @param type            类型：all(全部满足)|any(满足任一)
     * @return 筛选后的 List of QuestionnaireAnswer
     */
    @PostMapping("query/{questionnaireId}")
    public AjaxResult queryAnswers(@PathVariable String questionnaireId,
                                   @RequestBody Map<String, String> query,
                                   @RequestParam String type) {
        if (userService.isSimpleUser(getUsername()) && !userService.isDesigner(getUsername())) {
            return AjaxResult.success("没有访问权限").put("err", 403);
        }
        // 对问卷的所有数据进行筛选，条件为query，返回筛选后的 List<QuestionnaireAnswer>
        Questionnaire questionnaire = questionnaireService.getQuestionnaire(questionnaireId);
        if (questionnaire == null || !"0".equals(questionnaire.getDelFlag())) {
            return AjaxResult.success("问卷不存在").put("err", 404);
        }
        // 问卷设计者先看是不是自己的问卷
        if (userService.isDesigner(getUsername()) && !getUserId().equals(questionnaire.getUserId())) {
            return AjaxResult.success("没有访问权限").put("err", 403);
        }
        //先拿到所有答卷数据
        List<QuestionnaireAnswer> list = answerService.listAnswerInfoListByQuestionnaireId(questionnaireId);
        list = answerService.filterAnswerInfo(list, query, type);
        List<Map<String, Object>> result = answerService.statisticAnswers(list,
                questionnaireService.getQuestionnaireStructure(questionnaireId));
        return AjaxResult.success(result);
    }

    /**
     * 统计问卷的所有有效答卷数量
     *
     * @param questionnaireId 问卷ID
     * @return count
     */
    @GetMapping("count/{questionnaireId}")
    public AjaxResult countQuestionAnswers(@PathVariable String questionnaireId) {
        // 统计问卷的所有有效答卷数量, 所有角色均可访问
        List<QuestionnaireAnswer> list = answerService.listAnswerInfoListByQuestionnaireId(questionnaireId);
        return success().put("count", list.size());
    }

    /**
     * 查找本人受邀填写（answer_status=2）的问卷列表
     *
     * @return list of QuestionnaireAnswer
     */
    @GetMapping("todo")
    public AjaxResult todo() {
        // 查找本人受邀填写（answer_status=2）的问卷列表
        if (userService.isDesigner(getUsername()) && !userService.isSimpleUser(getUsername())) {
            return AjaxResult.success("没有访问权限").put("err", 403);
        }
        List<Map<String, Object>> list = answerService.listInvitedAnswerInfoListByUserId(getUserId());
        return AjaxResult.success(list);
    }
}
