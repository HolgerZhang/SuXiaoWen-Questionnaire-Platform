package com.suxiaowen.project.system.controller;

import com.suxiaowen.framework.aspectj.lang.annotation.Log;
import com.suxiaowen.framework.aspectj.lang.enums.BusinessType;
import com.suxiaowen.framework.web.controller.BaseController;
import com.suxiaowen.framework.web.domain.AjaxResult;
import com.suxiaowen.project.system.domain.mongo.UniAccountInfo;
import com.suxiaowen.project.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/system/user/uniAccount")
public class UniAccountController extends BaseController {
    @Autowired
    private ISysUserService userService;

    /**
     * 修改统一身份认证信息
     *
     * @param info 统一身份认证信息
     */
    @Log(title = "统一身份认证信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult updateUniAccountInfo(@Validated @RequestBody UniAccountInfo info) {
        long nowUserId = getUserId();
        //鉴权
        if (!userService.isRootOrAdmin(getUsername()) && !Objects.equals(nowUserId, info.getUserId())) {
            logger.debug(info.toString());
            return error("您没有修改其他人的统一身份信息的权限！");
        }
        userService.setUniAccount(info);
        return success();
    }


    /**
     * 获取统一身份认证信息
     *
     * @return 统一身份认证信息
     */
    @GetMapping
    public AjaxResult getUniAccountInfo() {
        long userId = getUserId();
        UniAccountInfo info = userService.getUniAccount(userId);
        if (info == null) {
            info = new UniAccountInfo();
            info.setUserId(userId);
            info.setStudentId("");
            info.setName("");
            info.setMajor("");
            info.setGrade("");
            userService.setUniAccount(info);
        }
        AjaxResult ajax = AjaxResult.success();
        ajax.put("studentId", info.getStudentId());
        ajax.put("name", info.getName());
        ajax.put("major", info.getMajor());
        ajax.put("grade", info.getGrade());
        return ajax;
    }
}
