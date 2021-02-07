/*
 * Copyright (c) 2021 vjin.top All rights reserved.
 * created by JW_Liang at 2021/2/7 12:19:7
 */

package top.vjin.frame.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Optional;

/**
 * 消息工具类
 *
 * @author JW_Liang
 * @date 2021-02-07
 */
@Slf4j
public class MessageUtils {

    public static Optional<MessageSource> getMessageSource() {
        return Optional.ofNullable(SpringUtils.getContext())
                .map(it -> it.getBean(MessageSource.class));
    }

    /**
     * 获取消息
     *
     * @param code 消息代码
     * @param args 消息变量
     * @return 消息具体内容, 如果不存在则返回代码
     */
    public static String getMsg(String code, Object... args) {
        return getMessageSource()
                .map(it -> it.getMessage(code, args, code, LocaleContextHolder.getLocale()))
                .orElseGet(() -> {
                    log.warn("消息源获取失败，使用code作为消息。");
                    return code;
                });
    }
}
