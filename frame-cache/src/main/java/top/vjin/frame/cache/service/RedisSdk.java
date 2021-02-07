/*
 * Copyright (c) 2021 vjin.top All rights reserved.
 * created by JW_Liang at 2021/2/7 13:35:11
 */

package top.vjin.frame.cache.service;

import lombok.Setter;
import org.redisson.Redisson;
import org.redisson.api.RAtomicDouble;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.DoubleSupplier;
import java.util.function.LongSupplier;

/**
 * Redis 服务
 *
 * @author JW_Liang
 * @date 2021-02-07
 */
@Service
@Setter(onMethod = @__(@Autowired))
public class RedisSdk {

    private Redisson redissonClient;

    /**
     * 获取redisson 客户端
     */
    public RedissonClient getRedissonClient() {
        return redissonClient;
    }

    /**
     * 获得原子整数
     *
     * @param moduleName 模块名称
     * @param targetName 目标对象名称
     * @param init       初始化操作,如果值不存在时触发本lambda,lambda返回一个整数将作为初始值
     * @return 分布式原子整数
     */
    public RAtomicLong getAtomicLong(String moduleName, String targetName, Consumer<RAtomicLong> init) {
        RAtomicLong atomicLong = redissonClient.getAtomicLong(getKey(moduleName, targetName));
        if (!atomicLong.isExists()) init.accept(atomicLong);
        return atomicLong;
    }

    /**
     * 获得原子整数
     *
     * @param moduleName 模块名称
     * @param targetName 目标对象名称
     * @param ttl        有效期
     * @param init       初始化操作,如果值不存在时触发本lambda,lambda返回一个整数将作为初始值
     * @return 分布式原子整数
     */
    public RAtomicLong getAtomicLong(String moduleName, String targetName, Duration ttl, LongSupplier init) {
        RAtomicLong atomicLong = redissonClient.getAtomicLong(getKey(moduleName, targetName));
        if (!atomicLong.isExists()) {
            if (atomicLong.compareAndSet(0, init.getAsLong())) atomicLong.expire(ttl.toMillis(), TimeUnit.MILLISECONDS);
        }
        return atomicLong;
    }

    /**
     * 获得原子小数
     *
     * @param moduleName 模块名称
     * @param targetName 目标对象名称
     * @param init       初始化操作,如果值不存在时触发本lambda,lambda返回一个小数将作为初始值
     * @return 分布式原子小数
     */
    public RAtomicDouble getAtomicDouble(String moduleName, String targetName, Consumer<RAtomicDouble> init) {
        RAtomicDouble atomicDouble = redissonClient.getAtomicDouble(getKey(moduleName, targetName));
        if (!atomicDouble.isExists()) init.accept(atomicDouble);
        return atomicDouble;
    }

    /**
     * 获得原子小数
     *
     * @param moduleName 模块名称
     * @param targetName 目标对象名称
     * @param ttl        有效期
     * @param init       初始化操作,如果值不存在时触发本lambda,lambda返回一个整数将作为初始值
     * @return 分布式原子小数
     */
    public RAtomicDouble getAtomicDouble(String moduleName, String targetName, Duration ttl, DoubleSupplier init) {
        RAtomicDouble atomicDouble = redissonClient.getAtomicDouble(getKey(moduleName, targetName));
        if (!atomicDouble.isExists()) {
            if (atomicDouble.compareAndSet(0, init.getAsDouble()))
                atomicDouble.expire(ttl.toMillis(), TimeUnit.MILLISECONDS);
        }
        return atomicDouble;
    }

    /**
     * 获得一个对象桶
     *
     * @param moduleName 模块名称
     * @param targetName 目标对象名称
     */
    private <V> RBucket<V> getBucket(String moduleName, String targetName) {
        return redissonClient.getBucket(getKey(moduleName, targetName));
    }

    /**
     * 获取一个值
     *
     * @param moduleName 模块名称
     * @param targetName 目标对象名称
     */
    @SuppressWarnings("unchecked")
    public <V> V get(String moduleName, String targetName) {
        return (V) getBucket(moduleName, targetName).get();
    }

    /**
     * 获取有效期
     *
     * @param moduleName 模块名称
     * @param targetName 目标对象名称
     */
    public Duration getExpire(String moduleName, String targetName) {
        return Duration.ofMillis(getBucket(moduleName, targetName).remainTimeToLive());
    }

    /**
     * 设置有效期
     *
     * @param moduleName 模块名称
     * @param targetName 目标对象名称
     * @param expire     过期时间
     */
    public boolean setExpire(String moduleName, String targetName, Duration expire) {
        return getBucket(moduleName, targetName).expire(expire.toMillis(), TimeUnit.MILLISECONDS);
    }

    /**
     * 设置一个值
     *
     * @param moduleName 模块名称
     * @param targetName 目标对象名称
     * @param value      设置的值
     */
    public void set(String moduleName, String targetName, Object value) {
        getBucket(moduleName, targetName).set(value);
    }

    /**
     * 设置一个值
     *
     * @param moduleName 模块名称
     * @param targetName 目标对象名称
     * @param value      设置的值
     * @param expire     过期时间
     */
    public void set(String moduleName, String targetName, Object value, Duration expire) {
        getBucket(moduleName, targetName).set(value, expire.toMillis(), TimeUnit.MILLISECONDS);
    }


    /**
     * 删除一个值
     *
     * @param moduleName 模块名称
     * @param targetName 目标对象名称
     */
    public boolean delete(String moduleName, String targetName) {
        return getBucket(moduleName, targetName).delete();
    }


    /**
     * 获取key值，将按照 应用名称:模块名称:目标对象 分组生成key，直接操作redis template时务必使用本方法生成key
     *
     * @param moduleName 模块名称
     * @param targetName 目标对象名称
     * @return 返回key
     */
    public String getKey(String moduleName, String targetName) {
        return moduleName + ":" + targetName;
    }
}
