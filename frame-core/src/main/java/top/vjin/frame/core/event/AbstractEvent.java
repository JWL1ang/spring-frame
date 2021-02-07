/*
 * Copyright (c) 2021 vjin.top All rights reserved.
 * created by JW_Liang at 2021/2/7 11:40:40
 */

package top.vjin.frame.core.event;

import org.springframework.context.ApplicationEvent;

/**
 * 抽象事件
 *
 * @param <T> 事件源类型,继AbstractEvent类的子类必须定义一个属性: @Getter private T source;
 * @author JW_Liang
 * @date 2021-02-07
 */
public abstract class AbstractEvent<T> extends ApplicationEvent {

    protected AbstractEvent() {
        super(true);
    }

    /**
     * 获取事件源
     *
     * @return 事件源对象
     */
    @Override
    public abstract T getSource();
}
