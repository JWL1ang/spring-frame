/*
 * Copyright (c) 2021 vjin.top All rights reserved.
 * created by JW_Liang at 2021/2/7 12:55:11
 */

package top.vjin.frame.core.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 一对数据
 *
 * @author JW_Liang
 * @date 2021-02-07
 */
@Getter
@AllArgsConstructor
public class PairVo<A, B> {

    /** 数据a */
    private final A a;

    /** 数据b */
    private final B b;
}
