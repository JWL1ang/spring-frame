/*
 * Copyright (c) 2021 vjin.top All rights reserved.
 * created by JW_Liang at 2021/2/7 13:52:19
 */

package top.vjin.frame.cache.config;

import lombok.Data;
import org.redisson.spring.cache.CacheConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * redisson 配置
 *
 * @author JW_Liang
 * @date 2021-02-07
 */
@Data
@ConfigurationProperties(prefix = "frame.cache")
public class CacheProperties {

    /** 缓存配置 */
    private Map<String, CacheData> springCacheConfig = new HashMap<>();

    /**
     * 缓存属性
     */
    public static class CacheData extends CacheConfig {

        /**
         * 设置数据存放时间,如果未0则永久存放
         */
        public Duration getTtlDuration() {
            return Duration.ofMillis(getTTL());
        }

        /**
         * 设置数据存放时间,如果未0则永久存放
         */
        public void setTtlDuration(Duration ttlDuration) {
            setTTL(ttlDuration.toMillis());
        }

        /**
         * 设置数据空闲时最大存放时间,如果未0则永久存放
         */
        public Duration getMaxIdleDuration() {
            return Duration.ofMillis(getMaxIdleTime());
        }

        /**
         * 设置数据空闲时最大存放时间,如果未0则永久存放
         */
        public void setMaxIdleDuration(Duration maxIdleDuration) {
            setMaxIdleTime(maxIdleDuration.toMillis());
        }
    }
}
