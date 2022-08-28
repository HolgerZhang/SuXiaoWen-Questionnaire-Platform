package com.suxiaowen.project.system.service;


public interface IAccountVerifyService {
    public void sendEmailVerifyCode(Long userId, String userName, String email);

    public boolean isValidVerifyCode(Long userId, String email, String verifyCode);
}
