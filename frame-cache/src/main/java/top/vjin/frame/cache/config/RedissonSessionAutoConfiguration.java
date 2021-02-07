/*
 * Copyright (c) 2021 vjin.top All rights reserved.
 * created by JW_Liang at 2021/2/7 13:53:21
 */

package top.vjin.frame.cache.config;

import org.redisson.spring.session.config.EnableRedissonHttpSession;
import org.redisson.spring.session.config.RedissonHttpSessionConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;

/**
 * @author JW_Liang
 * @date 2021-02-07
 */
@Configuration
@ConditionalOnMissingBean(RedissonHttpSessionConfiguration.class)
@EnableRedissonHttpSession
public class RedissonSessionAutoConfiguration {
}
