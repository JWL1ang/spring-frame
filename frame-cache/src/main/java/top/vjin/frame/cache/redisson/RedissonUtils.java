/*
 * Copyright (c) 2021 vjin.top All rights reserved.
 * created by JW_Liang at 2021/2/7 13:54:47
 */

package top.vjin.frame.cache.redisson;

import org.redisson.api.RedissonClient;

/**
 * @author JW_Liang
 * @date 2021-02-07
 */
public class RedissonUtils {

    private static RedissonClient redissonClient;

    /**
     * 获得redission客户端
     */
    public static RedissonClient getRedissonClient() {
        return redissonClient;
    }

    /**
     * 设置redission客户端
     */
    public static void setRedissonClient(RedissonClient redissonClient) {
        RedissonUtils.redissonClient = redissonClient;
    }
}
