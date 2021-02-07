/*
 * Copyright (c) 2021 vjin.top All rights reserved.
 * created by JW_Liang at 2021/2/7 12:38:46
 */

package top.vjin.frame.core.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 列表响应对象
 *
 * @author JW_Liang
 * @date 2021-02-07
 */
@Getter
@Setter
@ApiModel(description = "列表响应对象")
public class ListRet<T> extends Ret<List<T>> {

    private static final long serialVersionUID = 608505373661651019L;

    /**
     * 构建分页响应对象
     *
     * @param list 内容
     * @param <T>  类型
     * @return 分页响应对象
     */
    public static <T> ListRet<T> of(List<T> list) {
        ListRet<T> ret = new ListRet<>();
        ret.asOk(list);
        return ret;
    }

    /**
     * 转换内容并构建分页响应对象
     *
     * @param collection 内容
     * @param converter  类型转换器
     * @param <T>        类型
     * @return 分页响应对象
     */
    public static <T, R> ListRet<R> map(Collection<T> collection, Function<T, R> converter) {
        return of(collection.stream().map(converter).collect(Collectors.toList()));
    }
}
