/*
 * Copyright (c) 2021 vjin.top All rights reserved.
 * created by JW_Liang at 2021/2/7 18:20:17
 */

package top.vjin.frame.mq.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import top.vjin.frame.mq.listener.MqWithTxEventListener;
import top.vjin.frame.mq.service.MqSdk;
import top.vjin.frame.mq.service.impl.MqSdkImpl;

/**
 * 消息队列自动配置类
 *
 * @author JW_Liang
 * @date 2021-02-07
 */
@Configuration
@ComponentScan(value = "top.vjin.frame.mq", lazyInit = true)
public class MqAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public MqSdk mqSdk() {
        return new MqSdkImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public MqWithTxEventListener mqWithTxEventListener() {
        return new MqWithTxEventListener();
    }

    // @Bean
    // @ConditionalOnMissingBean
    // public MqRegisterSupport mqRegisterSupport() {
    //     return new MqRegisterSupport();
    // }
    //
    // @Bean
    // @ConditionalOnMissingBean
    // public MqConsumerSupport mqConsumerSupport() {
    //     return new MqConsumerSupport();
    // }
}
