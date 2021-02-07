/*
 * Copyright (c) 2021 vjin.top All rights reserved.
 * created by JW_Liang at 2021/2/7 11:59:49
 */

package top.vjin.frame.core.utils;

import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Spring工具类。
 *
 * @author JW_Liang
 * @date 2021-02-07
 */
public class SpringUtils {
    private static ApplicationContext context;

    public static void setApplicationContext(ApplicationContext context) {
        SpringUtils.context = context;
    }

    /**
     * 获取Spring容器的应用上下文。
     *
     * @return 返回Spring容器的应用上下文。
     */
    public static ApplicationContext getContext() {
        return context;
    }

    /**
     * 从Spring容器中获取指定名称的Bean。
     *
     * @param <T>      bean类型
     * @param beanName bean名称
     * @return 返回指定名称的bean。
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanName) {
        return (T) context.getBean(beanName);
    }

    /**
     * 从Spring容器中获取指定类型的Bean。
     *
     * @param <T>      bean类型
     * @param beanType bean类型
     * @return 返回指定类型的bean。
     */
    public static <T> T getBean(Class<T> beanType) {
        return context.getBean(beanType);
    }

    /**
     * 获取延迟bean
     *
     * @param beanType bean类型
     * @param <T>      bean类型
     * @return lazy的bean
     */
    public static <T> T getLazyBean(Class<T> beanType) {
        return Lazy.proxy(beanType, () -> getBean(beanType));
    }

    /**
     * 获取延迟bean list
     *
     * @param beanType bean类型
     * @param <T>      bean类型
     * @return lazy的bean
     */
    public static <T> List<T> getLazyBeans(Class<T> beanType) {
        @SuppressWarnings("unchecked")
        List<T> list = Lazy.proxy(List.class, () -> {
            Map<String, T> beans = context.getBeansOfType(beanType);
            return new ArrayList<>(beans.values());
        });
        return list;
    }
}
