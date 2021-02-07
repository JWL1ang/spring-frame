/*
 * Copyright (c) 2021 vjin.top All rights reserved.
 * created by JW_Liang at 2021/2/7 13:24:8
 */

package top.vjin.frame.core.utils;

import org.springframework.beans.BeansException;
import top.vjin.frame.core.exception.FrameException;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.concurrent.Executor;

/**
 * 线程工具类
 *
 * @author JW_Liang
 * @date 2021-02-07
 */
public class Threads {
    private static Executor executor;

    public static void setExecutor(Executor executor) {
        Threads.executor = executor;
    }

    /**
     * 线程休眠，单位：毫秒
     *
     * @param millis 休眠时间
     */
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw FrameException.of("线程休眠被打断:" + e.getMessage(), e);
        }
    }


    /**
     * 启动一个线程
     *
     * @param run 线程内容
     * @return 返回线程对象
     */
    public static Thread start(Runnable run) {
        Thread thread = new Thread(() -> {
            try {
                run.run();
            } catch (Exception e) {
                throw new UndeclaredThrowableException(e);
            }
        });
        thread.start();
        return thread;
    }


    /**
     * 启动一个异步任务,如果可以从spring中获得线程池此使用线程池启动,否则使用线程启动
     *
     * @param run 线程内容
     * @return 返回线程对象
     */
    public static void async(Runnable run) {
        try {
            SpringUtils.getContext().getBean("applicationTaskExecutor", Executor.class).execute(run);
        } catch (BeansException e) {
            start(run);
        }
    }
}
