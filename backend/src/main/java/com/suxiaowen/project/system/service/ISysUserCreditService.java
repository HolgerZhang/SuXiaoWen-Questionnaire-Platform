package com.suxiaowen.project.system.service;

public interface ISysUserCreditService {
    public Long getCredit(Long userId);

    public boolean rechargeCredit(Long userId, Long newCredit);
}
