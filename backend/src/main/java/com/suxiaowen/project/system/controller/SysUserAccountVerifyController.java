package com.suxiaowen.project.system.controller;

import com.suxiaowen.common.exception.user.UserVerifyException;
import com.suxiaowen.framework.web.controller.BaseController;
import com.suxiaowen.framework.web.domain.AjaxResult;
import com.suxiaowen.project.system.domain.SysUser;
import com.suxiaowen.project.system.service.IAccountVerifyService;
import com.suxiaowen.project.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system/user/verify")
public class SysUserAccountVerifyController extends BaseController {
    @Autowired
    private ISysUserService userService;

    @Autowired
    private IAccountVerifyService verifyService;

    @GetMapping("/send-mail")
    public AjaxResult sendVerifyCode() {
        SysUser user = userService.selectUserById(getUserId());
        try {
            verifyService.sendEmailVerifyCode(user.getUserId(), user.getUserName(), user.getEmail());
        } catch (UserVerifyException e) {
            return AjaxResult.error(e.getMessage());
        }
        return AjaxResult.success();
    }

    @PutMapping("/submit-code")
    public AjaxResult fillVerifyInfo(String code) {
        SysUser user = userService.selectUserById(getUserId());
        if (verifyService.isValidVerifyCode(user.getUserId(), user.getEmail(), code)) {
            user.setRoleIds(new Long[]{3L, 4L});  // 问卷设计者, 问卷填写者
            userService.updateUser(user);
            return AjaxResult.success();
        }
        return AjaxResult.error("验证信息不正确");
    }

}
