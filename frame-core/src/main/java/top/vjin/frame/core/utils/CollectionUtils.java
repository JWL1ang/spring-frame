/*
 * Copyright (c) 2021 vjin.top All rights reserved.
 * created by JW_Liang at 2021/2/7 13:8:56
 */

package top.vjin.frame.core.utils;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 集合工具类。
 *
 * @author JW_Liang
 * @date 2021-02-07
 */
public class CollectionUtils {
    /**
     * 判断指定的集合是否为空。
     *
     * @param collection 待判断的集合
     * @return 返回指定的集合是否为空。
     */
    public static Boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 判断指定的集合是否不为空。
     *
     * @param collection 待判断的集合
     * @return 返回指定的集合是否不为空。
     */
    public static Boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * 判断指定的数组是否为空。
     *
     * @param array 待判断的数组
     * @return 返回指定的数组是否为空。
     */
    public static Boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 判断指定的数组是否不为空。
     *
     * @param array 待判断的数组
     * @return 返回指定的数组是否不为空。
     */
    public static Boolean isNotEmpty(Object[] array) {
        return !isEmpty(array);
    }

    /**
     * 判断指定的Map是否为空。
     *
     * @param map 待判断的Map
     * @return 返回指定的Map是否为空。
     */
    public static Boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * 判断指定的Map是否不为空。
     *
     * @param map 待判断的Map
     * @return 返回指定的Map是否不为空。
     */
    public static Boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * 移除List中重复的元素。
     *
     * @param <T>  元素类型
     * @param list 列表
     */
    public static <T> void distinct(List<T> list) {
        Set<T> set = new HashSet<>(list);
        list.clear();
        list.addAll(set);
    }

    /**
     * 判断数组中是否包含指定元素。
     *
     * @param <T>           数组类型
     * @param elements      数组
     * @param elementToFind 待查找的元素
     * @return 如果数组中包含指定元素返回true，否则返回false。
     */
    public static <T> Boolean contains(T[] elements, T elementToFind) {
        return isNotEmpty(elements)
                && Stream.of(elements).filter(e -> e.equals(elementToFind)).count() > 0;
    }

    /**
     * 复制集合。
     *
     * @param <T>    集合元素类型
     * @param source 源集合
     * @param target 目标集合
     */
    public static <T> void copy(Collection<T> source, Collection<T> target) {
        target.clear();
        source.stream().forEach(target::add);
    }

    /**
     * 将数组转换为列表。
     *
     * @param <T>      元素类型
     * @param elements 数组
     * @return 返回对应的列表。
     */
    public static <T> List<T> toList(T[] elements) {
        return Stream.of(elements).collect(Collectors.toList());
    }

    /**
     * 连接元素转换为string
     *
     * @param splitter  分隔符，比如“,”
     * @param prefix    前缀，比如“(”
     * @param postfix   后缀，比如“)”
     * @param converter 元素类型转换器
     * @return 返回连接后的字符串
     */
    public static <T, R> String joinToString(Collection<T> collection, String splitter, String prefix, String postfix, Function<T, R> converter) {
        List<String> strings = collection.stream().map(it -> {
            R value = converter.apply(it);
            return value == null ? "null" : value.toString();
        }).collect(Collectors.toList());
        return joinToString(strings, splitter, prefix, postfix);
    }

    /**
     * 连接元素转换为string
     *
     * @param splitter 分隔符，比如“,”
     * @param prefix   前缀，比如“(”
     * @param postfix  后缀，比如“)”
     * @return 返回连接后的字符串
     */
    public static String joinToString(Collection<String> collection, String splitter, String prefix, String postfix) {
        StringBuilder stringBuilder = new StringBuilder(prefix);
        boolean haveElement = false;
        for (String string : collection) {
            if (haveElement) stringBuilder.append(splitter);
            else haveElement = true;
            stringBuilder.append(string);
        }
        stringBuilder.append(postfix);
        return stringBuilder.toString();
    }

    /**
     * 对集合求和
     *
     * @param collection 目标集合
     * @param converter  集合类型转换器
     * @param <T>        集合元素类型
     * @return 返回总和
     */
    public static <T> BigDecimal sum(Collection<T> collection, Function<T, BigDecimal> converter) {
        BigDecimal sum = BigDecimal.ZERO;
        for (T it : collection) {
            sum = sum.add(converter.apply(it));
        }
        return sum;
    }

    /**
     * 私有构造方法。
     */
    private CollectionUtils() {
    }
}
