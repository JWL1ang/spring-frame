/*
 * Copyright (c) 2021 vjin.top All rights reserved.
 * created by JW_Liang at 2021/2/7 18:10:37
 */

package top.vjin.frame.mq.service;

/**
 * 消息队列发送者
 *
 * @author JW_Liang
 * @date 2021-02-07
 */
public interface MqSdk {

    /**
     * 发送对象,接收方式：定义一个bean，方法上注解 @RabbitListener(queues = "消息队列名")
     *
     * @param exchange   交换机
     * @param routingKey 路由键,通过交换机和路由键将会对应一个接收方的消息队列。
     * @param data       发送数据,接收方类型定义应一致
     */
    void send(String exchange, String routingKey, Object data);

    /**
     * 发送对象,接收方式：定义一个bean，方法上注解 @RabbitListener(queues = "消息队列名")
     *
     * @param queue 消息队列
     * @param data  发送数据,接收方类型定义应一致
     */
    void send(String queue, Object data);

    /**
     * 跟随事务一起发送对象,如果事务回滚则此次消息不发送,接收方式：定义一个bean，方法上注解 @RabbitListener(queues = "消息队列名")
     *
     * @param exchange   交换机
     * @param routingKey 路由键,通过交换机和路由键将会对应一个接收方的消息队列。
     * @param data       发送数据,接收方类型定义应一致
     */
    void sendWithTx(String exchange, String routingKey, Object data);

    /**
     * 跟随事务一起对象,如果事务回滚则此次消息不发送,接收方式：定义一个bean，方法上注解 @RabbitListener(queues = "消息队列名")
     *
     * @param queue 消息队列
     * @param data  发送数据,接收方类型定义应一致
     */
    void sendWithTx(String queue, Object data);
}
