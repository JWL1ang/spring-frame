/*
 * Copyright (c) 2021 vjin.top All rights reserved.
 * created by JW_Liang at 2021/2/7 18:12:31
 */

package top.vjin.frame.mq.service.impl;

import lombok.Setter;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import top.vjin.frame.mq.event.MqWithTxEvent;
import top.vjin.frame.mq.service.MqSdk;

/**
 * 消息队列发送者实现类
 *
 * @author JW_Liang
 * @date 2021-02-07
 */
@Setter(onMethod = @__(@Autowired))
public class MqSdkImpl implements MqSdk {

    private RabbitTemplate rabbitTemplate;
    private ApplicationEventPublisher publisher;

    @Override
    public void send(String exchange, String routingKey, Object data) {
        //发送到指定交换机
        rabbitTemplate.convertAndSend(exchange, routingKey, data);
    }

    @Override
    public void send(String queue, Object data) {
        //直接发送到队列
        rabbitTemplate.convertAndSend(queue, data);
    }

    @Override
    public void sendWithTx(String exchange, String routingKey, Object data) {
        publisher.publishEvent(new MqWithTxEvent.ToExchange(data, exchange, routingKey));
    }

    @Override
    public void sendWithTx(String queue, Object data) {
        publisher.publishEvent(new MqWithTxEvent.ToQueue(data, queue));
    }
}
