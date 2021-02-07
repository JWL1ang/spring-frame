/*
 * Copyright (c) 2021 vjin.top All rights reserved.
 * created by JW_Liang at 2021/2/7 13:51:17
 */

package top.vjin.frame.cache.config;

import org.redisson.api.RedissonClient;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import top.vjin.frame.cache.redisson.RedissonUtils;

/**
 * @author JW_Liang
 * @date 2021-02-07
 */
@Configuration
@EnableCaching
@EnableConfigurationProperties(CacheProperties.class)
@ComponentScan(value = "top.vjin.frame.cache", lazyInit = true)
public class CacheAutoConfiguration implements BeanPostProcessor {

    public CacheAutoConfiguration(RedissonClient redissonClient) {
        //使redisson优先被注册
        RedissonUtils.setRedissonClient(redissonClient);
    }

    /**
     * spring 缓存管理器
     */
    @Bean
    public CacheManager cacheManager(RedissonClient redissonClient, CacheProperties cacheProperties) {
        return new RedissonSpringCacheManager(redissonClient, cacheProperties.getSpringCacheConfig());
    }
}
