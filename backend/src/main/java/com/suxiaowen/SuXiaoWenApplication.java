package com.suxiaowen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 *
 * @author suxiaowen
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class SuXiaoWenApplication {
    public static void main(String[] args) {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(SuXiaoWenApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  苏小问后端服务启动成功   ლ(´ڡ`ლ)ﾞ  ");
    }
}
