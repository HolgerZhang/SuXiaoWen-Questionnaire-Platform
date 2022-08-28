package com.suxiaowen.common.exception.user;

import com.suxiaowen.common.exception.base.BaseException;

public class UserVerifyException extends BaseException {
    public UserVerifyException(String what) {
        super("user", "user.verify.failed", null, what);
    }
}
