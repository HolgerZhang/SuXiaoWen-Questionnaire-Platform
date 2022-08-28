package com.suxiaowen.project.system.service.impl;

import com.suxiaowen.common.constant.Constants;
import com.suxiaowen.common.exception.user.UserVerifyException;
import com.suxiaowen.common.utils.StringUtils;
import com.suxiaowen.common.utils.uuid.IdUtils;
import com.suxiaowen.framework.redis.RedisCache;
import com.suxiaowen.project.system.service.IAccountVerifyService;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.suxiaowen.framework.datasource.DynamicDataSourceContextHolder.log;

@Service
public class AccountVerifyServiceImpl implements IAccountVerifyService {
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;  // Free Maker 配置项
    @Autowired
    private JavaMailSender javaMailSender;  // 邮件发送对象
    @Value("${spring.mail.username}")
    private String hostEmail;  // 发送邮箱（配置文件注入）
    @Autowired
    private RedisCache redis;  // Redis

    /**
     * 发送邮箱验证码
     *
     * @param userId 用户ID
     * @param email  目标邮箱
     * @return 是否发送成功
     */
    @Override
    public void sendEmailVerifyCode(Long userId, String userName, String email) {
        String code = generateVerifyCode(userId, email);  // 生成验证码
        String content;
        try {
            // 利用模板生成邮件
            Template template = freeMarkerConfigurer.getConfiguration().getTemplate("email-verify-template.ftl");
            Map<String, Object> model = new HashMap<>();
            model.put("name", userName);
            model.put("code", code);
            content = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
            throw new UserVerifyException("邮件生成异常！" + e.getMessage());
        }
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            // 发送邮件
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
            messageHelper.setFrom(hostEmail);
            messageHelper.setTo(email);
            messageHelper.setSubject("苏小问校园问卷平台 用户验证信息");
            messageHelper.setText(content, true);
            javaMailSender.send(message);
            log.info("邮件成功发送");
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new UserVerifyException("邮件发送异常！" + e.getMessage());
        }
    }

    /**
     * 判断是否位非法的邮件验证码
     *
     * @param userId     用户ID
     * @param email      邮箱
     * @param verifyCode 验证码
     */
    @Override
    public boolean isValidVerifyCode(Long userId, String email, String verifyCode) {
        String key = generateRedisKey(userId, email);
        String code = redis.getCacheObject(key);
        redis.deleteObject(key);
        System.out.println("key:" + key);
        System.out.println("redis:" + code);
        System.out.println("input:" + verifyCode);
        return code != null && code.equalsIgnoreCase(verifyCode);
    }

    /**
     * 根据邮箱生成验证码
     *
     * @param userId
     * @param email  目标邮箱
     * @return 6位验证码
     */
    private @NotNull String generateVerifyCode(Long userId, String email) {
        String key = generateRedisKey(userId, email);
        String code = IdUtils.randomUUID();  // 随机验证码
        // 向redis里存入数据和设置缓存时间
        redis.setCacheObject(key, code, Constants.MAIL_EXPIRATION, TimeUnit.MINUTES);
        return code;
    }

    private String generateRedisKey(Long userId, String email) {
        return Constants.MAIL_VERIFY_CODE_KEY + StringUtils.format("{}-{}", userId, email);
    }
}
