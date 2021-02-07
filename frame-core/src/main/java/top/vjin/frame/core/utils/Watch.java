/*
 * Copyright (c) 2021 vjin.top All rights reserved.
 * created by JW_Liang at 2021/2/7 13:3:27
 */

package top.vjin.frame.core.utils;


import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 计时器
 *
 * @author JW_Liang
 * @date 2021-02-07
 */
public class Watch {

    private static final BigDecimal SECOND_RATE = BigDecimal.valueOf(1000);

    private static final BigDecimal MINUTE_RATE = BigDecimal.valueOf(1000 * 60);

    /** 开始时间 */
    private long startTime;

    /** 结束时间 */
    private long stopTime;

    protected Watch() {
    }

    /**
     * 开始计时
     *
     * @return 计时器
     */
    public static Watch start() {
        Watch watch = new Watch();
        watch.restart();
        return watch;
    }

    /**
     * 重新开始计时
     */
    public void restart() {
        this.startTime = System.currentTimeMillis();
        this.stopTime = 0;
    }

    /**
     * 结束计时
     */
    public void stop() {
        this.stopTime = System.currentTimeMillis();
    }


    /**
     * 获取秒数
     */
    public double getSecond() {
        return BigDecimal.valueOf(stopTime() - startTime).divide(SECOND_RATE, 2, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * 获取分钟数
     */
    public double getMinute() {
        return BigDecimal.valueOf(stopTime() - startTime).divide(MINUTE_RATE, 2, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * 获取结束时间
     */
    private long stopTime() {
        return stopTime == 0 ? System.currentTimeMillis() : startTime;
    }
}
