package com.suxiaowen.project.system.service.impl;

import com.suxiaowen.project.system.domain.SysUser;
import com.suxiaowen.project.system.mapper.SysUserMapper;
import com.suxiaowen.project.system.service.ISysUserCreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserCreditServiceImpl implements ISysUserCreditService {
    @Autowired
    private SysUserMapper userMapper;

    @Override
    public Long getCredit(Long userId) {
        SysUser user = userMapper.selectUserById(userId);
        return user.getCredit();
    }

    @Override
    public boolean rechargeCredit(Long userId, Long newCredit) {
        if (newCredit > 0 && newCredit < 1000) {
            SysUser user = userMapper.selectUserById(userId);
            int ret = userMapper.updateUserCredit(userId, user.getCredit() + newCredit);
            return ret > 0;
        }
        return false;
    }
}
