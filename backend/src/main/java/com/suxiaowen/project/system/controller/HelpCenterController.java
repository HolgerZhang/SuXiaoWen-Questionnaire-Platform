package com.suxiaowen.project.system.controller;

import com.suxiaowen.framework.web.controller.BaseController;
import com.suxiaowen.framework.web.domain.AjaxResult;
import com.suxiaowen.project.system.domain.QuestionHelp;
import com.suxiaowen.project.system.domain.Questionnaire;
import com.suxiaowen.project.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 互助平台：发布互助任务，获取互助任务推荐，完成互助任务
 */
@RestController
@RequestMapping("/help-center")
public class HelpCenterController extends BaseController {
    @Autowired
    IHelpCenterService helpCenterService;

    @Autowired
    ISysUserCreditService creditService;

    @Autowired
    IQuestionnaireService questionnaireService;

    @Autowired
    ISysUserService userService;

    @Autowired
    IQuestionnaireAnswerService answerService;

    /**
     * （设计者）发布任务
     *
     * @param data 任务详情，其中questionnaireId、price、amount有效
     * @return 操作是否成功
     */
    @PostMapping("task")
    public AjaxResult create(@RequestBody QuestionHelp data) {
        // 验证用户身份
        if (userService.isSimpleUser(getUsername())) {
            return AjaxResult.success("您没有权限").put("err", 401);
        }
        // 验证问卷是否存在且有效
        if (!questionnaireService.isQuestionnaireExist(data.getQuestionnaireId())) {
            return AjaxResult.success("问卷不存在").put("err", 404);
        }
        Questionnaire q = questionnaireService.getQuestionnaire(data.getQuestionnaireId());
        if (!"0".equals(q.getDelFlag())) {
            return AjaxResult.success("问卷无效").put("err", 404);
        }
        if (!q.getUserId().equals(getUserId()) && !userService.isRootOrAdmin(getUsername())) {
            return AjaxResult.success("您没有权限,不是您发布的问卷").put("err", 401);
        }
        //price*amount是否少于用户所拥有的积分数量
        long score = creditService.getCredit(getUserId());
        long aimScore = data.getPrice() * data.getAmount();
        if (score < aimScore) {
            return AjaxResult.success("积分不足").put("err", 403);
        }
        //并扣除相应积分，发布问卷互助任务
        helpCenterService.createTask(data);
        creditService.rechargeCredit(getUserId(), -aimScore);
        return success();
    }

    /**
     * （填写者）完成任务
     *
     * @param taskId 任务ID
     * @return 是否成功
     */
    @GetMapping("task/finish")
    public AjaxResult finish(@RequestParam String taskId) {
        if (!userService.isSimpleUser(getUsername())) {
            return AjaxResult.success("您没有权限").put("err", 401);
        }
        QuestionHelp task = helpCenterService.findTask(taskId);
        // 检查任务的counts还小于amount
        if (task == null || !"0".equals(task.getEnabled()) || task.getCounts() >= task.getAmount()) {
            return AjaxResult.success("任务不存在或无效").put("err", 404);
        }
        // 该用户是否为第一次填写,不则不新增积分，不增加任务完成数量counts
        if (answerService.countUserAnswerQuestionTimes(getUserId(), task.getQuestionnaireId()) > 0) {
            return success("您已经填写过该问卷，本次填写不获得积分");
        }
        // 若满足则账户积分加上price，增加任务完成数量counts，
        creditService.rechargeCredit(getUserId(), task.getPrice());
        helpCenterService.setCounts(taskId, task.getCounts() + 1);
        return success();
    }

    /**
     * （填写者）完成任务
     *
     * @param questionnaireId 问卷ID
     * @return 是否成功
     */
    @GetMapping("task/finish/question")
    public AjaxResult finishQ(@RequestParam String questionnaireId) {
        if (!userService.isSimpleUser(getUsername())) {
            return AjaxResult.success("您没有权限").put("err", 401);
        }
        QuestionHelp task = helpCenterService.findTaskByQuestion(questionnaireId);
        // 检查问卷是否有对应的互助任务
        if (task == null || !"0".equals(task.getEnabled()) || task.getCounts() >= task.getAmount()) {
            return AjaxResult.success("任务不存在或无效").put("err", 404);
        }
        // 该用户是否为第一次填写,不则不新增积分，不增加任务完成数量counts
        if (answerService.countUserAnswerQuestionTimes(getUserId(), task.getQuestionnaireId()) > 1) {
            return success("您已经填写过该问卷，本次填写不获得积分");
        }
        // 若满足则账户积分加上price，增加任务完成数量counts，
        creditService.rechargeCredit(getUserId(), task.getPrice());
        helpCenterService.setCounts(task.getTaskId(), task.getCounts() + 1);
        return success();
    }

    /**
     * (管理员) 调整可用状态
     *
     * @param taskId 任务ID
     * @return 是否成功
     */
    @GetMapping("task/available")
    public AjaxResult available(@RequestParam String taskId) {
        if (!userService.isRootOrAdmin(getUsername())) {
            return AjaxResult.success("您没有权限").put("err", 401);
        }
        // 若任务为可用，则设置为禁用，否则还原可用状态
        QuestionHelp task = helpCenterService.findTask(taskId);
        if (task == null) {
            return AjaxResult.success("任务不存在").put("err", 404);
        }
        if ("0".equals(task.getEnabled())) {
            task.setEnabled("1");
        } else {
            task.setEnabled("0");
        }
        helpCenterService.updateTask(task);
        return success();
    }

    /**
     * （设计者）删除任务
     *
     * @param taskId 任务ID
     * @return 操作是否成功
     */
    @DeleteMapping("task")
    public AjaxResult delete(@RequestParam String taskId) {
        // 验证用户身份
        if (userService.isSimpleUser(getUsername())) {
            return AjaxResult.success("您没有权限").put("err", 401);
        }
        // 验证问卷是否存在且有效
        QuestionHelp task = helpCenterService.findTask(taskId);
        if (task == null || !"0".equals(task.getEnabled())) {
            return AjaxResult.success("任务不存在或无效").put("err", 404);
        }
        Questionnaire q = questionnaireService.getQuestionnaire(task.getQuestionnaireId());
        if (!Objects.equals(getUserId(), q.getUserId()) && !getUserId().equals(q.getUserId())) {
            return AjaxResult.success("您没有权限").put("err", 401);
        }
        // 若互助任务的目标已达到（counts>=amount），直接删除；否则退还0.6*price*(amount-counts)的积分（向上取整）再删除。
        if (task.getCounts() < task.getAmount()) {
            long refund = (long) (0.6 * task.getPrice() * (task.getAmount()) - task.getCounts());
            creditService.rechargeCredit(getUserId(), refund);
        }
        helpCenterService.deleteTask(taskId);
        return success();
    }

    /**
     * 发送邀请，向data指定的用户推送问卷邀请
     *
     * @param taskId 互助任务Id
     * @param data   type: desc
     *               其中type可以是：email、username、phone和studentId
     *               desc为内容，查找时使用模糊匹配（like）
     * @return 成功的数量
     */
    @PostMapping("invite/{taskId}")
    public AjaxResult invite(@PathVariable String taskId, @RequestBody Map<String, String> data) {
        // 验证用户身份
        if (userService.isSimpleUser(getUsername())) {
            return AjaxResult.success("您没有权限").put("err", 401);
        }
        // 验证问卷是否存在且有效
        QuestionHelp task = helpCenterService.findTask(taskId);
        if (task == null || !"0".equals(task.getEnabled())) {
            return AjaxResult.success("任务不存在或无效").put("err", 404);
        }
        // 发送邀请，向data指定的用户推送问卷邀请，具体流程为：
        // 查找用户列表，对于查找到的用户，向questionnaire_answer表中新增行，
        // answer_status=2，fill_ip留空，其余按照实际填写
        List<Long> userIds = new LinkedList<>(), _users = null;
        for (Map.Entry<String, String> entry : data.entrySet()) {
            String type = entry.getKey();
            String desc = entry.getValue();
            switch (type) {
                case "email":
                    _users = userService.findEmailLike(desc);
                    break;
                case "username":
                    _users = userService.findNameLike(desc);
                    break;
                case "phone":
                    _users = userService.findPhoneLike(desc);
                    break;
                case "studentId":
                    _users = userService.findStudentIdLike(desc);
                    break;
            }
            if (_users != null) {
                userIds.addAll(_users);
            }
        }
        for (Long userId : userIds) {
            System.err.println(userId);
            helpCenterService.invite(userId, task.getQuestionnaireId());
        }
        return success();
    }

    /**
     * 列出所有互助任务
     *
     * @return 互助任务的ID列表
     */
    @GetMapping("list")
    public AjaxResult list() {
        //列出所有互助任务
        List<Map<String, Object>> taskIds = helpCenterService.listAll();
        return AjaxResult.success(taskIds);
    }

    /**
     * 根据taskId查找问卷Id
     *
     * @param taskId 互助任务ID
     * @return 互助任务对应的问卷ID，不存在返回null
     */
    @GetMapping("task/{taskId}")
    public AjaxResult getTaskQuestionId(@PathVariable String taskId) {
        // 根据taskId查找问卷Id
        QuestionHelp task = helpCenterService.findTask(taskId);
        if (task == null || !"0".equals(task.getEnabled())) {
            return AjaxResult.success("任务不存在或无效").put("err", 404);
        }
        return AjaxResult.success().put("id", task.getQuestionnaireId());
    }

    /**
     * 根据问卷ID查找任务是否存在
     *
     * @param question 问卷ID
     * @return 返回布尔值，若存在返回任务Id
     */
    @GetMapping("exist/{question}")
    public AjaxResult existTaskQuestionId(@PathVariable String question) {
        // 根据taskId查找问卷Id
        QuestionHelp task = helpCenterService.findTaskByQuestion(question);
        AjaxResult ret = AjaxResult.success().put("exist", task != null && "0".equals(task.getEnabled()));
        if (task != null && "0".equals(task.getEnabled())) {
            ret.put("taskId", task.getTaskId());
        }
        return ret;
    }

}
