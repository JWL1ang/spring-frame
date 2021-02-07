/*
 * Copyright (c) 2021 vjin.top All rights reserved.
 * created by JW_Liang at 2021/2/7 11:56:16
 */

package top.vjin.frame.core.config;

import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import top.vjin.frame.core.service.CoreSdk;
import top.vjin.frame.core.service.impl.CoreSdkImpl;
import top.vjin.frame.core.utils.SpringUtils;

/**
 * @author JW_Liang
 * @date 2021-02-07
 */
@Configuration
@EnableConfigurationProperties(CoreProperties.class)
@ComponentScan(value = "top.vjin.frame.core", lazyInit = true)
@EnableScheduling   //启用定时任务
@EnableAsync        //启用异步任务
@EnableRetry        //启用spring重试
public class CoreAutoConfig implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtils.setApplicationContext(applicationContext);
    }

    @Bean
    @ConditionalOnMissingBean
    public CoreSdk coreSdk() {
        return new CoreSdkImpl();
    }

}
