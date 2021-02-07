/*
 * Copyright (c) 2021 vjin.top All rights reserved.
 * created by JW_Liang at 2021/2/7 13:42:26
 */

package top.vjin.frame.cache.service.impl;

import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.vjin.frame.core.service.LockSdk;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author JW_Liang
 * @date 2021-02-07
 */
@Slf4j
@Service
@Setter(onMethod = @__(@Autowired))
public class LockSdkImpl implements LockSdk {

    RedissonClient redissonClient;
    LockSdkImpl lockSdkImpl;

    @Override
    public Lock get(long waitSecond, long leaseSecond, Object... path) {
        String key = toKey(path);
        return new LockImpl(redissonClient.getLock(key), waitSecond, leaseSecond);
    }

    /**
     * 加入事务中执行
     *
     * @param action 动作
     * @return 返回返回值
     */
    @Transactional
    public <RETURN> RETURN openOrJoinTransaction(Supplier<RETURN> action) {
        return action.get();
    }

    /**
     * 加入事务中执行
     *
     * @param action 动作
     * @return 返回返回值
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public <RETURN> RETURN openTransaction(Supplier<RETURN> action) {
        return action.get();
    }

    private String toKey(Object... path) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Object it : path) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(it);
            }
            stringBuilder.append(it);
        }
        return stringBuilder.toString();
    }

    @AllArgsConstructor
    class LockImpl implements Lock {

        RLock lock;
        long waitSeconds;
        long leaseSeconds;

        @Override
        public boolean tryLocking(Runnable action) {
            boolean locked = false;
            try {
                locked = lock.tryLock(waitSeconds, leaseSeconds, TimeUnit.SECONDS);
                if (locked) {
                    action.run();
                }
                return locked;
            } catch (InterruptedException e) {
                throw new LockException("锁定期间被唤醒:" + e.getMessage(), e);
            } finally {
                if (locked) {
                    try {
                        lock.unlock();
                    } catch (Exception e) {
                        //解锁失败,忽略异常
                        log.info("解锁失败:{} - {}", e.getClass().getName(), e.getMessage());
                    }
                }
            }
        }

        @Override
        public void lock(Runnable action) {
            lock(() -> {
                action.run();
                return null;
            });
        }

        @Override
        public <RETURN> RETURN lock(Supplier<RETURN> action) {
            boolean locked = false;
            try {
                locked = lock.tryLock(waitSeconds, leaseSeconds, TimeUnit.SECONDS);
                if (locked) {
                    return action.get();
                } else {
                    throw new LockException("分布式锁锁定失败:" + lock.getName());
                }
            } catch (InterruptedException e) {
                throw new LockException("锁定期间被唤醒:" + e.getMessage(), e);
            } finally {
                if (locked) {
                    try {
                        lock.unlock();
                    } catch (Exception e) {
                        //解锁失败,忽略异常
                        log.info("解锁失败:{} - {}", e.getClass().getName(), e.getMessage());
                    }
                }
            }
        }

        @Override
        public void lockInTx(Runnable action) {
            lockInTx(() -> {
                action.run();
                return null;
            });
        }

        @Override
        public <RETURN> RETURN lockInTx(Supplier<RETURN> action) {
            return lock(() -> lockSdkImpl.openOrJoinTransaction(action));
        }

        @Override
        public void lockInNewTx(Runnable action) {
            lockInNewTx(() -> {
                action.run();
                return null;
            });
        }

        @Override
        public <RETURN> RETURN lockInNewTx(Supplier<RETURN> action) {
            return lock(() -> lockSdkImpl.openTransaction(action));
        }
    }
}
