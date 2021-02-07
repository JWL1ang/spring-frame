/*
 * Copyright (c) 2021 vjin.top All rights reserved.
 * created by JW_Liang at 2021/2/7 18:16:1
 */

package top.vjin.frame.mq.listener;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.event.TransactionalEventListener;
import top.vjin.frame.mq.event.MqWithTxEvent;
import top.vjin.frame.mq.service.MqSdk;

/**
 * 跟随事务一起发送事件监听器
 *
 * @author JW_Liang
 * @date 2021-02-07
 */
@Setter(onMethod = @__(@Autowired))
public class MqWithTxEventListener {

    private MqSdk mqSdk;

    @Async
    @TransactionalEventListener
    public void toQueue(MqWithTxEvent.ToQueue event) {
        mqSdk.send(event.getQueue(), event.getSource());
    }

    @Async
    @TransactionalEventListener
    public void toExchange(MqWithTxEvent.ToExchange event) {
        mqSdk.send(event.getExchange(), event.getRoutingKey(), event.getSource());
    }
}
