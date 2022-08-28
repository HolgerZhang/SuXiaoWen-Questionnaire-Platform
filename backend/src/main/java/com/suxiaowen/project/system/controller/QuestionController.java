package com.suxiaowen.project.system.controller;

import com.suxiaowen.common.utils.uuid.IdUtils;
import com.suxiaowen.framework.web.controller.BaseController;
import com.suxiaowen.framework.web.domain.AjaxResult;
import com.suxiaowen.project.system.domain.Questionnaire;
import com.suxiaowen.project.system.domain.QuestionnaireAnswer;
import com.suxiaowen.project.system.domain.mongo.QLogic;
import com.suxiaowen.project.system.domain.mongo.Question;
import com.suxiaowen.project.system.domain.mongo.QuestionnaireStructure;
import com.suxiaowen.project.system.service.IQuestionnaireAnswerService;
import com.suxiaowen.project.system.service.IQuestionnaireService;
import com.suxiaowen.project.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 问卷创建、设计、列表、删除、状态设置、星标
 * 本类中所有代码都要检查是否具有管理员或问卷设计者权限（角色）
 * 非管理员只能对自己的问卷进行操作
 */
@RestController
@RequestMapping("/question")
public class QuestionController extends BaseController {
    @Autowired
    IQuestionnaireService questionnaireService;
    @Autowired
    ISysUserService userService;
    @Autowired
    IQuestionnaireAnswerService answerService;

    /**
     * 获取问卷基础数据
     *
     * @param id 问卷id
     * @return 问卷数据
     */
    @GetMapping("{id}")
    public AjaxResult getQuestionnaire(@PathVariable String id) {
        //     首先检索MySQL，判断该问卷是否存在，不存在报错
        //     返回基础信息Questionnaire
        //     否则提示错误信息
        Questionnaire questionnaire = null;

        if (!questionnaireService.isQuestionnaireExist(id)) {
            return AjaxResult.success("问卷不存在").put("err", 404);
        } else {
            questionnaire = questionnaireService.getQuestionnaire(id);
        }
        AjaxResult ajax = AjaxResult.success();
        ajax.put("question", questionnaire);
        return ajax;
    }

    /**
     * 获取问卷详细数据
     *
     * @param id 问卷id
     * @return 问卷数据
     */
    @GetMapping("detail/{id}")
    public AjaxResult getQuestionnaireDetail(@PathVariable String id) {
        // 首先检索MySQL，判断该问卷是否存在，不存在报错
        //     之后继续查询，获得问卷基础信息，检索MongoDB的 MONGODB_COLLECTION_Q_STRUCT 集合
        //     获取问卷的结构信息。返回结构信息QuestionnaireStructure
        //     否则提示错误信息
        QuestionnaireStructure structure = null;
        if (!questionnaireService.isQuestionnaireExist(id)) {
            return AjaxResult.success("问卷不存在").put("err", 404);
        } else {
            structure = questionnaireService.getQuestionnaireStructure(id);
        }
        AjaxResult ajax = AjaxResult.success();
        ajax.put("structure", structure);
        return ajax;
    }

    /**
     * 新建问卷
     *
     * @param data 问卷信息，至少提供问卷标题，至多提供问卷标题、问卷简介
     * @return 返回创建的问卷对象
     */
    @PostMapping("new")
    public AjaxResult newQuestionnaire(@RequestBody Map<String, String> data) {
        Questionnaire questionnaire = new Questionnaire();
        // questionnaireId=UUID, title=data.title, introduction=data.introduction|''
        //     userId=getUserId(), createTime=DateTime.Now,其余默认设置
        //     生成新的QuestionnaireStructure对象保存至MongoDB的 MONGODB_COLLECTION_Q_STRUCT 集合，
        //     文档ID保存到 questionnaire.structure，
        //     返回创建的问卷Questionnaire对象
        questionnaire.setQuestionnaireId(IdUtils.randomUUID());
        questionnaire.setTitle(data.get("title"));
        questionnaire.setIntroduction(data.getOrDefault("introduction", ""));
        questionnaire.setUserId(getUserId());
        questionnaire.setCreateTime(new Date());
        questionnaire.setDelFlag("0");
        questionnaire.setStarFlag("0");
        questionnaire.setAnonymousFlag("0");
        questionnaire.setModifyFlag("0");
        questionnaire.setQuestionnaireStatus("1");
        QuestionnaireStructure structure = new QuestionnaireStructure();
        String docId = questionnaireService.saveStructure(structure);
        questionnaire.setStructure(docId);
        questionnaireService.saveQuestionnaire(questionnaire);
        return AjaxResult.success("创建成功", questionnaire);
    }

    /**
     * 修改问卷基础属性
     *
     * @param questionnaire 问卷对象
     * @return 是否成功
     */
    @PostMapping("modify/basic")
    public AjaxResult modifyQuestionnaire(@RequestBody Questionnaire questionnaire) {
        // 根据传入的questionnaire，修改问卷基础属性
        questionnaireService.modifyQuestionnaire(questionnaire);
        return AjaxResult.success();
    }

    /**
     * 修改问卷结构
     *
     * @param structure 问卷结构
     * @return 是否成功
     */
    @PostMapping("modify/structure")
    public AjaxResult modifyQuestionnaireStructure(@RequestBody QuestionnaireStructure structure) {
        // 根据传入的structure，修改问卷结构
        for (Question question : structure.getQuestions()) {
            if (question.getId() == null) {
                question.setId(IdUtils.fastSimpleUUID());
            }
        }
        for (QLogic logic : structure.getLogics()) {
            if (logic.getId() == null) {
                logic.setId(IdUtils.fastSimpleUUID());
            }
        }
        questionnaireService.modifyStructure(structure);
        return success();
    }

    /**
     * 列某用户作为**问卷设计者**的某些类型的问卷列表
     *
     * @param userId 用户ID
     * @param type   all|star|deleted
     * @return 问卷列表(List of Questionnaire)
     */
    @GetMapping("list/{userId}")
    public AjaxResult listUserQuestionnaire(@PathVariable String userId, @RequestParam String type) {
        // 非管理员只能看自己的问卷（即userId==getUserId()）
        //     列出该用户所发布的问卷基础信息，all为全部（不含已删除的），star为星标问卷，deleted为已删除的问卷
        //     返回List<Questionnaire>
        List<Questionnaire> list1 = null;
        if (userService.isRootOrAdmin(getUsername())) {
            list1 = questionnaireService.getAllQuestionnaire();
        } else {
            list1 = questionnaireService.getUserQuestionnaireById(getUserId());
        }
        List<Questionnaire> res = new ArrayList<Questionnaire>();
        for (Questionnaire q : list1) {
            String delFlag = q.getDelFlag();
            String starFlag = q.getStarFlag();
            if ("all".equals(type)) {
                if ("0".equals(delFlag)) {
                    res.add(q);
                }
            } else if ("star".equals(type)) {
                if ("1".equals(starFlag)) {
                    res.add(q);
                }
            } else {
                if ("1".equals(delFlag)) {
                    res.add(q);
                }
            }
        }
        return AjaxResult.success(res);
    }

    /**
     * 若问卷不为星标，将问卷设置为星标问卷，否则取消星标
     *
     * @param id 问卷id
     * @return 是否成功
     */
    @GetMapping("star/{id}")
    public AjaxResult starQuestionnaire(@PathVariable String id) {
        // 若问卷不为星标，将问卷设置为星标问卷，否则取消星标
        Questionnaire questionnaire = questionnaireService.getQuestionnaire(id);
        if ("0".equals(questionnaire.getStarFlag())) {
            questionnaire.setStarFlag("1");
        } else {
            questionnaire.setStarFlag("0");
        }
        questionnaireService.modifyQuestionnaire(questionnaire);
        return AjaxResult.success("切换星标成功");
    }

    /**
     * 删除问卷，保留问卷结构和收集到的数据
     *
     * @param id 问卷id
     * @return 是否成功
     */
    @GetMapping("delete/{id}")
    public AjaxResult deleteQuestionnaire(@PathVariable String id) {
        // 将问卷删除标记置1，保留问卷结构和收集到的数据
        Questionnaire questionnaire = questionnaireService.getQuestionnaire(id);
        questionnaire.setDelFlag("1");
        questionnaireService.modifyQuestionnaire(questionnaire);
        return AjaxResult.success("删除问卷成功");
    }

    /**
     * 彻底删除问卷，前提是问卷删除标记为1，且没有收集到的数据才能彻底删除
     *
     * @param id 问卷id
     * @return 是否成功
     */
    @DeleteMapping("{id}")
    public AjaxResult deleteQuestionnaireCompletely(@PathVariable String id) {
        // 前提是问卷删除标记为1，且没有收集到的数据才能彻底删除
        Questionnaire questionnaire = questionnaireService.getQuestionnaire(id);
        if ("0".equals(questionnaire.getDelFlag())) {
            return AjaxResult.success("问卷未被删除,不可清理").put("err", 403);
        } else if (0 == answerService.listAnswerInfoListByQuestionnaireId(questionnaire.getQuestionnaireId()).size()) {
            //无答卷
            return AjaxResult.success("问卷仍有答卷存在,不可清理").put("err", 403);
        } else {
            questionnaireService.delQuestionnaireById(questionnaire.getQuestionnaireId());
        }
        return AjaxResult.success("问卷已彻底删除");
    }

    /**
     * 删除问卷收集到的数据
     *
     * @param id 问卷id
     * @return 是否成功
     */
    @GetMapping("clear/{id}")
    public AjaxResult clearQuestionnaireData(@PathVariable String id) {
        // 前提是问卷删除标记为1，删除问卷收集到的数据
        Questionnaire questionnaire = questionnaireService.getQuestionnaire(id);
        if ("0".equals(questionnaire.getDelFlag())) {
            return AjaxResult.success("问卷未被删除,不可清理数据").put("err", 403);
        } else {
            List<QuestionnaireAnswer> list = answerService.listAnswerInfoListByQuestionnaireId(questionnaire.getQuestionnaireId());
            for (QuestionnaireAnswer qa : list) {
                answerService.delQuestionnaireAnswerByAnswerId(qa.getAnswerId());
            }
        }
        return AjaxResult.success("删除所有答卷");
    }

    /**
     * 恢复问卷
     *
     * @param id 问卷id
     * @return 是否成功
     */
    @GetMapping("recover/{id}")
    public AjaxResult recoverQuestionnaire(@PathVariable String id) {
        // 将问卷删除标记置0
        Questionnaire questionnaire = questionnaireService.getQuestionnaire(id);
        questionnaire.setDelFlag("0");
        questionnaireService.modifyQuestionnaire(questionnaire);
        return AjaxResult.success("撤销删除成功");
    }

}
