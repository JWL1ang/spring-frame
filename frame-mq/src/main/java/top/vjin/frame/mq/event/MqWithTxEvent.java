/*
 * Copyright (c) 2021 vjin.top All rights reserved.
 * created by JW_Liang at 2021/2/7 18:16:15
 */

package top.vjin.frame.mq.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.vjin.frame.core.event.AbstractEvent;

/**
 * 伴随事务消息队列事件
 *
 * @author JW_Liang
 * @date 2021-02-07
 */
public class MqWithTxEvent {

    /**
     * 发送给队列事件
     */
    @Getter
    @AllArgsConstructor
    public static class ToQueue extends AbstractEvent<Object> {

        /** 目标源 */
        private Object source;

        /** 队列 */
        private String queue;
    }


    /**
     * 发送给交换机事件
     */
    @Getter
    @AllArgsConstructor
    public static class ToExchange extends AbstractEvent<Object> {

        /** 目标源 */
        private Object source;

        /** 交换机 */
        private String exchange;

        /** 路由键 */
        private String routingKey;
    }
}
