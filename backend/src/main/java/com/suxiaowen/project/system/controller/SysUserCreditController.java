package com.suxiaowen.project.system.controller;

import com.suxiaowen.framework.web.controller.BaseController;
import com.suxiaowen.framework.web.domain.AjaxResult;
import com.suxiaowen.project.system.service.ISysUserCreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system/user/credit")
public class SysUserCreditController extends BaseController {
    @Autowired
    public ISysUserCreditService creditService;

    @GetMapping
    public AjaxResult userCredit() {
        return AjaxResult.success().put("credit", creditService.getCredit(getUserId()));
    }

    @PutMapping("/recharge")
    public AjaxResult rechargeCredit(Long credit) {
        if (creditService.rechargeCredit(getUserId(), credit)) {
            return success();
        }
        return AjaxResult.error("充值失败");
    }
}
