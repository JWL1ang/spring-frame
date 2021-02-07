/*
 * Copyright (c) 2021 vjin.top All rights reserved.
 * created by JW_Liang at 2021/2/7 12:1:45
 */

package top.vjin.frame.core.utils;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import top.vjin.frame.core.exception.FrameException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * 懒加载的对象
 *
 * @param <T> 将懒加载的类型
 * @author JW_Liang
 * @date 2021-02-07
 */
public interface Lazy<T> {

    /**
     * 获取值,如果没有加载则立即加载,否则返回原结果
     *
     * @return 懒加载的结果
     */
    T get();

    /**
     * 是否已加载
     *
     * @return 如果已加载返回true，否则返回false
     */
    boolean isLoad();

    /**
     * 获得懒加载实例
     *
     * @param loader 加载器
     * @return 懒加载实例
     */
    static <T> Lazy<T> of(Supplier<T> loader) {
        return new SynchronizedLazyImpl<>(loader);
    }

    /**
     * 懒加载方式代理
     *
     * @param targetClass 目标类型，可以是接口或者普通类，如果是普通类则要求是open(非final)的
     * @param loader      加载器
     * @return 懒加载代理对象
     */
    static <T> T proxy(Class<T> targetClass, Supplier<T> loader) {
        Lazy<T> lazy = of(loader);

        if (targetClass.isInterface()) {
            @SuppressWarnings("unchecked")
            T target = (T) Proxy.newProxyInstance(Lazy.class.getClassLoader(), new Class[]{targetClass}, (proxy, method, args) -> {
                T object = lazy.get();
                try {
                    return method.invoke(object, args);
                } catch (InvocationTargetException e) {
                    throw e.getTargetException() != null ? e.getTargetException() : e;
                }
            });
            return (T) target;
        } else {
            //创建Enhancer对象，类似于JDK动态代理的Proxy类，下一步就是设置几个参数
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(targetClass);

            //设置回调函数
            enhancer.setCallback((MethodInterceptor) (o, method, args, methodProxy) -> {
                T object = lazy.get();
                return methodProxy.invoke(object, args);
            });
            @SuppressWarnings("unchecked")
            T target = (T) enhancer.create();
            return target;
        }
    }

    /**
     * 动态代理list
     *
     * @param loader 加载器
     * @return 列表代理对象
     */
    static <T> List<T> proxyList(Supplier<List<T>> loader) {
        @SuppressWarnings("unchecked")
        Class<List<T>> clazz = (Class<List<T>>) (Class) List.class;
        return proxy(clazz, loader);
    }

    /**
     * 动态代理set
     *
     * @param loader 加载器
     * @return 列表代理对象
     */
    static <T> Set<T> proxySet(Supplier<Set<T>> loader) {
        @SuppressWarnings("unchecked")
        Class<Set<T>> clazz = (Class<Set<T>>) (Class) Set.class;
        return proxy(clazz, loader);
    }

    /**
     * 动态代理set
     *
     * @param loader 加载器
     * @return 列表代理对象
     */
    static <K, V> Map<K, V> proxyMap(Supplier<Map<K, V>> loader) {
        @SuppressWarnings("unchecked")
        Class<Map<K, V>> clazz = (Class<Map<K, V>>) (Class) Map.class;
        return proxy(clazz, loader);
    }
}


/**
 * 使用同步方式线程安全方式实现的懒加载对象
 *
 * @param <T> 值类型
 */
class SynchronizedLazyImpl<T> implements Lazy<T> {

    /** 值 */
    private T value;

    /** 加载器 */
    private Supplier<T> loader;

    /**
     * 构造实例
     *
     * @param loader 加载器
     */
    public SynchronizedLazyImpl(Supplier<T> loader) {
        if (loader == null) throw new FrameException("必须提供加载器");
        this.loader = loader;
    }

    @Override
    public T get() {
        //如果此前已加载则直接返回
        T v = value;
        if (v != null) return v;

        //多线程下排队尝试加载
        synchronized (this) {
            v = value;
            //进入后先判断在排队期间是否被其它线程加载,是则直接返回结果,否则进行懒加载
            if (v != null) return v;

            v = loader.get();
            if (v == null) throw new FrameException("懒加载失败。");

            //如果成懒加载成功,则是否加载器并返回结果
            loader = null;
            value = v;
            return v;
        }
    }

    @Override
    public boolean isLoad() {
        return value != null;
    }
}
