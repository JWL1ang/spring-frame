/*
 * Copyright (c) 2021 vjin.top All rights reserved.
 * created by JW_Liang at 2021/2/7 13:28:53
 */

package top.vjin.frame.core.service;

import top.vjin.frame.core.exception.FrameException;

import java.util.function.Supplier;

/**
 * @author JW_Liang
 * @date 2021-02-07
 */
public interface LockSdk {

    /**
     * 获取分布式锁
     *
     * @param waitSecond  等待时间
     * @param leaseSecond 锁定时间
     * @param path        欲锁定路径,路径完全一致则锁定，注意:名称将转toString被换为字符串
     * @return 锁类
     */
    Lock get(long waitSecond, long leaseSecond, Object... path);

    /**
     * 分布式锁类
     */
    interface Lock {

        /**
         * 锁定然后执行一些动作
         *
         * @param action 要执行的动作
         * @return 如果成功返回true, 如果失败返回false
         */
        boolean tryLocking(Runnable action);

        /**
         * 锁定然后执行一些动作
         *
         * @param action 要执行的动作
         * @throws LockException 如果锁定失败
         */
        void lock(Runnable action);

        /**
         * 锁定然后执行一些动作
         *
         * @param action 要执行的动作
         * @return 如果锁定成功返回动作返回值
         * @throws LockException 如果锁定失败
         */
        <RETURN> RETURN lock(Supplier<RETURN> action);


        /**
         * 锁定后开启新或加入事务,然后执行一些动作
         *
         * @param action 要执行的动作
         * @throws LockException 如果锁定失败
         */
        void lockInTx(Runnable action);

        /**
         * 锁定后开启或加入事务,然后执行一些动作
         *
         * @param action 要执行的动作
         * @return 如果锁定成功返回动作返回值
         * @throws LockException 如果锁定失败
         */
        <RETURN> RETURN lockInTx(Supplier<RETURN> action);


        /**
         * 锁定后开启新事务,然后执行一些动作
         *
         * @param action 要执行的动作
         * @throws LockException 如果锁定失败
         */
        void lockInNewTx(Runnable action);

        /**
         * 锁定后开启新事务,然后执行一些动作
         *
         * @param action 要执行的动作
         * @return 如果锁定成功返回动作返回值, 否则配出异常
         * @throws LockException 如果锁定失败
         */
        <RETURN> RETURN lockInNewTx(Supplier<RETURN> action);
    }

    /**
     * 锁定失败异常
     */
    class LockException extends FrameException {

        private static final long serialVersionUID = -3019154567521175779L;

        public LockException(String message) {
            super(message);
        }

        public LockException(String message, Throwable cause) {
            super(message, cause);
        }

        public LockException(Throwable cause) {
            super(cause);
        }

        public LockException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }
}
